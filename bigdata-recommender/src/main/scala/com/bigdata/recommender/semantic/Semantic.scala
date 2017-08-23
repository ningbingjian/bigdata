package com.bigdata.recommender.semantic

import java.io.{ByteArrayInputStream, File}

import com.bigdata.recommender.ik.IKwordExtracter
import org.apache.spark.ml.feature.{HashingTF, IDF}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.collection.mutable
import scala.collection.JavaConverters._
import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import com.bigdata.common.dfs.DfsUtil
import org.apache.spark.ml.linalg.{SparseVector, Vector => MLV}
/**
  * Created by ning on 2017/8/22.
  */
class Semantic extends Serializable{ self =>
  private val SYMBOL_NEWLINE = "\n"
  private val rawfeaturesCol = "raw_features"
  private val featuresCol = "features"
  private val wordCol = "word"
  private val weightCol = "weight"
  private var idCol = "id"
  private var coosimDir = "coosimDir"
  private var numFeatures = 1 << 20
  private var numWordPerDocument = 5000
  private var numTopSimilariyDocument = 200
  private val fieldColSetting = mutable.Map[String,(Boolean,Boolean,Double)]()
  def setIdCol(idCol:String): this.type ={
    self.idCol = idCol
    self
  }

  /**
    *
    * @param fieldCol 字段名称
    * @param needSplitWord  是否需要分词
    * @param iterableType 是否是可迭代类型 如果是迭代类型且不用分词，遍历取每个元素当作词即可
    * @param weight 该字段的权重  如果是-1 表示不需要进行权重处理
    * @return
    */
  def setFieldCol(fieldCol:String,needSplitWord:Boolean = true,iterableType:Boolean = false,weight:Double = -1): this.type ={
    fieldColSetting(fieldCol)=(needSplitWord,iterableType,weight)
    self
  }

  /**
    * 设置共生矩阵的输出目录 由于相似度计算结果巨大，无法直接在内存中处理
    * @param coosimDir
    * @return
    */
  def setCoosimDir(coosimDir:String):this.type = {
    self.coosimDir = coosimDir
    self
  }
  def transform(inputDF:DataFrame) = {
    val spark = inputDF.sparkSession
    val wordDF = toWordDF(inputDF)
    val wordWeightRDD = toWordWeigthRDD(wordDF)
    val wordWeightBc = inputDF.sparkSession.sparkContext.broadcast(wordWeightRDD.collectAsMap())
    val tfidfDF = calTfIdf(wordDF)
    val topFeatureRDD = calSim(tfidfDF)
    externalCompute(topFeatureRDD,spark)
  }
  def calTfIdf(wordDF:DataFrame): DataFrame ={
    val hashingTF = new HashingTF()
      .setNumFeatures(numFeatures)
      .setInputCol(wordCol)
      .setOutputCol(rawfeaturesCol)
    val rawfeaturesDF = hashingTF.transform(wordDF)
    rawfeaturesDF.collect().foreach(println)
    val idfModel = new IDF()
      .setInputCol(rawfeaturesCol)
      .setOutputCol(featuresCol)
      .fit(rawfeaturesDF)
    val tfidfDF = idfModel.transform(rawfeaturesDF)
    tfidfDF.collect().foreach(println)
    tfidfDF
  }
  def calSim(tfidfDF:DataFrame):RDD[(Int,Iterable[(String,MLV)])] = {
    val topFeatureRDD = tfidfDF.rdd.flatMap{
      case row =>
        val (features,words,id) = (
          row.getAs[SparseVector](featuresCol),
          row.getAs[Seq[String]](wordCol),
          row.getAs[String](idCol))

        //val sparseFeatures = features.toSparse

        //val zipFeature = sparseFeatures.indices.zip(sparseFeatures.values)
        val zipFeature = features.indices.zip(features.values)
        val buf = ListBuffer[(Int,(String,MLV))]()
        for((indice,value) <- zipFeature){
          buf append ((indice,(id,features)))
        }
        buf
    }
      .groupByKey()
      .filter(_._2.size < numWordPerDocument)
    topFeatureRDD.collect.foreach(println)
    topFeatureRDD
  }
  def externalCompute(topFeatureRDD:RDD[(Int,Iterable[(String,MLV)])],spark:SparkSession) ={
    DfsUtil.deleteQuiet(coosimDir)
    val coosimDirBc = spark.sparkContext.broadcast(coosimDir)
    topFeatureRDD.mapPartitionsWithIndex{
      case (idx,iter) =>
        val ops = DfsUtil.openFile(coosimDirBc.value + File.separator + idx,true)
        while(iter.hasNext){
          val seq = iter.next()._2.toSeq
          for(i <- 0 until seq.length ){
            for(j <- i + 1 until seq.length){
              val sim = Similariy.cosine(seq(i)._2,seq(j)._2)
              var bytes =  (s"""${seq(i)._1},${seq(j)._1},${sim}""" + SYMBOL_NEWLINE).getBytes("UTF-8")
              var ips = new ByteArrayInputStream(bytes,0,bytes.length)
              DfsUtil.write(ips,ops,false)
              DfsUtil.closeQuiet(ips)
              bytes = (s"""${seq(j)._1},${seq(i)._1},${sim}""" + SYMBOL_NEWLINE).getBytes("UTF-8")
              ips = new ByteArrayInputStream(bytes,0,bytes.length)
              DfsUtil.write(ips,ops,false)
              DfsUtil.closeQuiet(ips)
            }
          }
        }
        DfsUtil.closeQuiet(ops)
        //仅仅为了让程序往下进行 返回结果无意义
        Seq().toIterator
    }.count()
    spark.sparkContext.textFile(coosimDir + File.separator + "*")
      .map{
        case text =>
          val fields = text.split(",")
          (fields(0),(fields(1),fields(2).toDouble))
      }
      .filter(f => f._1 != f._2._1)//exclude self similarity
      .groupByKey()
      .map {
        case (key, values) =>
          val top = values.toSeq.distinct.sortWith((a, b) => a._2 > b._2).take(numTopSimilariyDocument)
          (key,top)
      }
  }

  def toWordWeigthRDD(wordDF:DataFrame): RDD[(String,Map[String,Double])] ={
    wordDF.rdd.map{
      case row =>
        val id = row.getAs(idCol).toString
        val words = row.getAs[Seq[String]](wordCol)
        val weights = row.getAs[Seq[Double]](weightCol)
        val wordWeight = words.zip(weights)
        val wordWeightMap = mutable.Map[String,ArrayBuffer[Double]]()
        for((k,v) <- wordWeight){
          val weights = wordWeightMap.getOrElse(k,ArrayBuffer[Double]())
          weights.append(v)
          wordWeightMap(k) = weights
        }
        val finalWordWeight = mutable.Map[String,Double]()
        for((k,v) <- wordWeightMap){
          val avgWeight = v.sum/v.size
          finalWordWeight(k) = avgWeight
        }
        (id,finalWordWeight.toMap)
    }

  }
  def toWordDF(inputDF:DataFrame):DataFrame={
    import inputDF.sparkSession.implicits._
    val wordRDD = inputDF.rdd.map{
      case row =>
        val id = row.getAs(idCol).toString
        val wordWeight = ListBuffer[Double]()
        val allWords = ListBuffer[String]()
        for((fieldCol,(needSplitWord,iterableType,weight)) <- fieldColSetting){
          val fieldValue = row.getAs[Any](fieldCol)
          val words =  if(needSplitWord){
            val content = {
              if(iterableType){
                fieldValue.asInstanceOf[Traversable[String]].mkString(" ")
              }else{
                fieldValue.asInstanceOf[String]
              }
            }
            IKwordExtracter.extractWords(content).asScala
          }else{
              if(iterableType){
                fieldValue.asInstanceOf[Traversable[String]]
              }else{
                Seq(fieldValue.asInstanceOf[String])
              }
          }
          val weights = words.map(m=>weight)
          wordWeight.append(weights.toSeq:_*)
          allWords.append(words.toSeq:_*)
        }
        (id,allWords,wordWeight)
    }
     wordRDD.toDF(idCol,wordCol,weightCol)
  }
}
