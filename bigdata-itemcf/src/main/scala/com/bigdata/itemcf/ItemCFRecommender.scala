package com.bigdata.itemcf

import com.bigdata.itemcf.cooccurrence.TopElementsQueue
import com.bigdata.itemcf.measures.VectorSimilarityMeasure
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.mllib.linalg.{SparseVector, Vector, Vectors}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.mllib.linalg.{DenseVector, SparseVector, Vectors, Vector => MLV}
import org.apache.spark.rdd.RDD

import scala.beans.BeanProperty
import scala.collection.{immutable, mutable}
import scala.collection.mutable.ListBuffer
import scala.collection.JavaConverters._


case class VectorOrPref(
  vector:Option[MLV] = None,
  userid:Option[Int] = None,
  pref:Option[Double] = None)
case class VectorAndPref(
  vector:Option[MLV] = None,
  userids:Option[Seq[Int]] = None,
  prefs:Option[Seq[Double]] = None)
case class PrefAndSimilarity(itemid:Int,pref:Double,vector:MLV)
case class Item( itemid:Int, value:Double)

class ItemCFRecommender(spark:SparkSession) extends Serializable{ self =>
  import spark.implicits._
  var useridCol = "userid"
  var itemidCol = "itemid"
  var ratingCol = "rating"
  var similarity:VectorSimilarityMeasure = _

  var topSimilarityNum = 200
  var recommenderNum = 200


  private var itemVector:RDD[(Int,MLV)] = _
  private var userVector:RDD[(Int,MLV)] = _
  private var ratingDF:DataFrame = _
  private var normVector:RDD[(Int,MLV)] = _
  private var allNormVector:RDD[(Int,Double)] = _
  private var similarityVector:RDD[(Int,MLV)] = _
  private var topSimilarityVector:RDD[(Int,MLV)] = _
  private var vectorAndPrefRdd:RDD[(Int,VectorAndPref)] = _

  def setUseridCol(useridCol:String):this.type = {
    self.useridCol = useridCol
    self
  }
  def setItemidCol(itemidCol:String):this.type = {
    self.itemidCol = itemidCol
    self
  }
  def setRatingCol(ratingCol:String):this.type = {
    self.ratingCol = ratingCol
    self
  }
  def setSimilarity(similarity:VectorSimilarityMeasure):this.type = {
    self.similarity = similarity
    self
  }
  def setTopSimNum(num:Int) :this.type = {
    this.topSimilarityNum = num
    self
  }
  def setRecommenderNum(recommenderNum:Int):this.type = {
    self.recommenderNum = recommenderNum
    self
  }
  def transform(ratingDF:DataFrame)={
    self.ratingDF = ratingDF
    transItemVector()
    transUserVector()
    normlize()
    calsim()
    topSim()
    partialMultiply()
    recommend()

  }
  def transItemVector()={
    val ratingRDD = ratingDF.select(useridCol,itemidCol,ratingCol).rdd
    // 2 转换为物品向量
    itemVector = ratingRDD.map{
      row:Row =>
        (
          row.getAs[Int](itemidCol),
          (row.getAs[Int](useridCol), row.getAs[Double](ratingCol))
        )
    }
      .groupByKey(200)
      .map{
        case (itemid,ratings) => {
          (itemid,Vectors.sparse(Integer.MAX_VALUE,ratings.toSeq))
        }
      }
    //itemVector.saveAsTextFile("/user/ning/als/itemVector")
  }
  def transUserVector() = {
    val ratingRDD = ratingDF.select(useridCol,itemidCol,ratingCol).rdd

    // 3 转换为用户向量
    userVector = ratingRDD.map{
      row:Row =>
        (
          row.getAs[Int](useridCol) ,
          (row.getAs[Int](itemidCol), row.getAs[Double](ratingCol))
        )
    }.groupByKey(200)
      .map{
        case (userid,ratings) => {
          (userid,Vectors.sparse(Integer.MAX_VALUE,ratings.toSeq))
        }
      }
    //userVector.saveAsTextFile("/user/ning/als/userVector")
  }
  def normlize(): Unit ={
    //向量标准化
    normVector = itemVector.flatMap{
      case (itemid,vector) =>
        val buf = ListBuffer[(Int,MLV)]()
        val normVector = similarity.normalize(vector).asInstanceOf[SparseVector]
        for(i <- 0 until normVector.indices.length){
          val userid = normVector.indices(i)
          val pref = normVector.values(i)
          val vector = Vectors.sparse(Integer.MAX_VALUE,Seq((itemid,pref))).asInstanceOf[SparseVector]
          buf append ((userid,vector))
        }
        buf
    }.reduceByKey((v1,v2) =>{
      EnHanceVectors.merge(v1,v2)
    },1000)
    //normVector.saveAsTextFile("/user/ning/als/normVector")

    allNormVector = itemVector.map{
      case (itemid,vector) =>
        (itemid,similarity.norm(vector))
    }
  }
  def calsim():Unit = {
    val normsMapBc = spark.sparkContext.broadcast(allNormVector.collectAsMap())
    val cooccurrences = spark.sparkContext.accumulator(0)
    val cooccurrencesVector  = normVector.flatMap {
      case (userid,vector:SparseVector) =>
        val result = ListBuffer[(Int,MLV)]()
        val zip = vector.indices zip vector.values
        val sortElements = zip.sortBy(_._1)
        for(n <- 0 until sortElements.length){
          val lb = ListBuffer[(Int,Double)]()
          val occurrenceValueA = sortElements(n)._2
          val occurrenceIndexA = sortElements(n)._1
          for(m <- n until sortElements.length){
            val occurrenceValueB = sortElements(m)._2
            val occurrenceIndexB = sortElements(m)._1
            cooccurrences.add(1)
            val aggregateValue = similarity.aggregate(occurrenceValueA,occurrenceValueB)
            lb.append((occurrenceIndexB,aggregateValue))
          }
          val dots = Vectors.sparse(Integer.MAX_VALUE,lb.toSeq)
          result.append((occurrenceIndexA,dots))
        }
        result
    }
    //cooccurrencesVector.saveAsTextFile("/user/ning/als/cooccurrencesVector")

    similarityVector = cooccurrencesVector.groupByKey().map{
      case (itemid,vectors:Iterable[SparseVector]) =>
        val iter = vectors.iterator
        val dots = iter.next()
        var dotsMap = dots.indices.zip(dots.values).toMap.filter(!_._2.equals(Double.NaN))
        while(iter.hasNext){
          val toAdd = iter.next()
          val toAddMap = toAdd.indices.zip(toAdd.values).toMap.filter(!_._2.equals(Double.NaN))
          for((k,v) <- toAddMap){
              val sum = dotsMap.getOrElse(k,0.0) + v
              dotsMap = dotsMap ++ Map(k -> sum)
          }
        }
        //dotsMap = dotsMap.filter(_._2 != 0.0)
        val numberOfColumns = -1
        val normA = normsMapBc.value(itemid)
        val similarityMap = mutable.Map[Int,Double]()
        for((k,b) <- dotsMap){
          val similarityValue = similarity.similarity(b, normA, normsMapBc.value(k), numberOfColumns)
          if(k != itemid){
            similarityMap(k) = similarityValue
          }
        }
        val (indices,values) = similarityMap.unzip

        (itemid,Vectors.sparse(Integer.MAX_VALUE,indices.toArray,values.toArray))

    }
    //similarityVector.saveAsTextFile("/user/ning/als/similarityVector")
  }

  def topSim()= {
    topSimilarityVector = similarityVector.flatMap{
      case (itemid,v:SparseVector) => {
        val topKQueue = new TopElementsQueue(topSimilarityNum)
        val lb = ListBuffer[(Int,MLV)]()
        for(i <- 0 until v.indices.length){
          val top = topKQueue.top
          val candidateValue = v.values(i)
          if (candidateValue > top.get) {
            top.setIndex(v.indices(i))
            top.set(candidateValue)
            topKQueue.updateTop
          }
          val vector = Vectors.sparse(Integer.MAX_VALUE,Array(itemid),Array(v.values(i)))
          lb append ((v.indices(i),vector))
        }

        val topElements = for (topKSimilarity <- topKQueue.getTopElements.asScala)
          yield (topKSimilarity.index, topKSimilarity.get)
        val topKVectors = Vectors.sparse(Integer.MAX_VALUE,topElements)
        lb append ((itemid,topKVectors))
        lb
      }
    }.reduceByKey{
      case (v1:SparseVector,v2:SparseVector) =>
        val merge = EnHanceVectors.merge(v1,v2).asInstanceOf[SparseVector]
        EnHanceVectors.topK(merge,topSimilarityNum)
    }
    //topSimilarityVector.saveAsTextFile("/user/ning/als/topSimilarityVector")
  }

  def partialMultiply()= {
    val rdd1 = userVector.flatMap {
      case (userid,v:SparseVector) =>
        val lb = ListBuffer[(Int,VectorOrPref)]()
        for(i <- 0 until v.indices.length){
          val vectorOrPref = VectorOrPref(None,Some(userid),Some(v.values(i)))
          lb append ((v.indices(i),vectorOrPref))
        }
        lb
    }
    val rdd2 = topSimilarityVector.map {
      case (itemid,v:SparseVector) =>
        val vectorOrPref = VectorOrPref(Some(v),None,None)
        ((itemid,vectorOrPref))
    }

    val rdd3 = rdd1 union rdd2
    vectorAndPrefRdd = rdd3.groupByKey().map {
      case (itemid,vectorOrPrefs:Iterable[VectorOrPref]) =>
        val userids = ListBuffer[Int]();
        val prefs = ListBuffer[Double]();
        var vector:MLV = null;
        for(vectorOrPref <- vectorOrPrefs){
          if(vectorOrPref.vector == None){
            userids append vectorOrPref.userid.get
            prefs append vectorOrPref.pref.get
          }else{
            vector = vectorOrPref.vector.get
          }
        }
        (itemid,VectorAndPref(Some(vector),Some(userids),Some(prefs)))
    }
    //vectorAndPrefRdd.saveAsTextFile("/user/ning/als/vectorAndPrefRdd")
  }

  def recommend():DataFrame ={
    //vectorAndPrefRdd.collect().foreach(println)
    val prefAndSimilarity = vectorAndPrefRdd.flatMap{
      case (itemid,vectorAndPref:VectorAndPref) =>
        val userids = vectorAndPref.userids.get
        val prefs = vectorAndPref.prefs.get
        val simVector = vectorAndPref.vector.get
        val lb = ListBuffer[(Int,PrefAndSimilarity)]()
        for(i <- 0 until userids.length){
          val userid = userids(i)
          val pref = prefs(i)
          if(pref != Double.NaN){
            lb append ((userid,PrefAndSimilarity(itemid,pref,simVector)))
          }
        }
        lb
    }
    //prefAndSimilarity.collect().foreach(println)
    val recommenderItemsRDD = prefAndSimilarity.groupByKey()
      .map{
        case (userid,prefAndSimilarities:Iterable[PrefAndSimilarity]) =>
          var filterItemids = mutable.Set[Int]()
          var numerators = mutable.Map[Int,Double]()
          var denominators = mutable.Map[Int,Double]()
          for(prefAndSimilarity <- prefAndSimilarities){
            filterItemids.add(prefAndSimilarity.itemid)
            val simVector = prefAndSimilarity.vector.asInstanceOf[SparseVector]
            val pref = prefAndSimilarity.pref
            val indices = simVector.indices
            val values = simVector.values
            for( i <- 0 until indices.length ){
              denominators(indices(i)) = denominators.getOrElse(indices(i),0.0) + values(i)
              numerators(indices(i)) =  numerators.getOrElse(indices(i),0.0) +  values(i) * pref
            }
          }
          val lb = ListBuffer[(Int,Double)]()
          if(numerators != null){
            for((k,v) <- numerators if !filterItemids.contains(k)){
              val predition = v/denominators(k)
              lb append ((k,predition))
            }
          }
          val reItems = lb
            //.sortWith((x,y) => x._2.compareTo(y._2) > 0)
            .take(recommenderNum)
            .map(item => Item(item._1,item._2))

          (userid,reItems)

      }
    recommenderItemsRDD.flatMap(rating => {
      val userid = rating._1
      rating._2.map(item => (userid,item.itemid,item.value))
    }).toDF(useridCol,itemidCol,ratingCol)
  }


}
