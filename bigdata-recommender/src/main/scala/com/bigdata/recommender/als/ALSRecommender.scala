package com.bigdata.recommender.als

import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.functions._
import scala.collection.mutable
/**
  * Created by ning on 2017/8/3.
  */
class ALSRecommender(spark:SparkSession) { self =>
  var rank = 50
  var ratingRDD:RDD[Rating] = _
  var lambda:Double =0.01
  var iterNum:Int = 5
  var recommenderNum:Int = 100
  var trainRatio = 1.0
  var trainTimes:Int = 1
  var finalMse = Double.MaxValue
  var finalModel:MatrixFactorizationModel = null
  private var useridCol = "userid"
  private var itemidCol = "itemid"
  private var ratingCol = "rating"
  import spark.implicits._

  def setRank(rank:Int):this.type = {
    self.rank = rank
    self
  }
  def setLambda(lambda:Double):this.type = {
    self.lambda = lambda
    self
  }
  def setIterNum(iterNum:Int):this.type = {
    self.iterNum = iterNum
    self
  }
  def setTrainRaTio(trainRatio:Double) :this.type = {
    self.trainRatio = trainRatio
    self
  }
  def setRecommenderNum(recommenderNum:Int):this.type = {
    self.recommenderNum = recommenderNum
    self
  }
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

 def setRating(ratingDF:DataFrame):this.type = {
    self.ratingRDD = ratingDF.as[Rating].rdd
    self
  }
  def setRatingRDD(rating:RDD[Rating]):this.type = {
    self.ratingRDD = rating
    self
  }
  def train(implicited:Boolean):MatrixFactorizationModel = {
    for (time <- 0 until trainTimes){
      val splitRatingRDD = ratingRDD.randomSplit(Array[Double](trainRatio,1-trainRatio))
      val trainRDD = splitRatingRDD(0)
      trainRDD.foreach(println)
      val model = if(implicited){
        ALS.trainImplicit(trainRDD,rank,iterNum)
      }else{
        ALS.train(trainRDD,rank,iterNum,lambda)
      }
      val mse = getMSE(model)
      if(mse < finalMse){
        finalModel = model
        finalMse = mse
      }
      println(self.toString())
    }
    finalModel
  }
  def transform(ratingDF:DataFrame):DataFrame = {
    train(false,ratingDF)
    getRecommenderItemsDF
  }
  def train(implicited:Boolean,ratingDF: DataFrame):MatrixFactorizationModel = {
    self.ratingRDD = ratingDF.select(col(useridCol).as("user"),col(itemidCol).as("product"),col("rating")).as[Rating]
      .rdd
    for (time <- 0 until trainTimes){
      val splitRatingRDD = ratingRDD.randomSplit(Array[Double](trainRatio,1-trainRatio))
      val trainRDD = splitRatingRDD(0)
      trainRDD.foreach(println)
      val model = if(implicited){
        ALS.trainImplicit(trainRDD,rank,iterNum)
      }else{
        ALS.train(trainRDD,rank,iterNum,lambda)
      }
      val mse = getMSE(model)
      if(mse < finalMse){
        finalModel = model
        finalMse = mse
      }
      println(self.toString())
    }
    finalModel
  }
  def getRecommenderItemsDF:DataFrame = {
    val recommenders = finalModel.recommendProductsForUsers(recommenderNum)
    val finalRecommender = ratingRDD.map(rating => (rating.user,rating))
      .groupByKey(20)
      .join(recommenders)
      .flatMap{
        case (user,(ratingIter,remRatings)) =>
          val ratingItems = ratingIter.map(_.product).toSet
          val finalRating = mutable.Set[Rating]()
          for(remRating <- remRatings){
            if(!ratingItems.contains(remRating.product)){
              finalRating.add(remRating)
            }
          }
          finalRating.toSeq

      }
    finalRecommender.toDS()
      .select(col("user").as(useridCol),col("product").as(itemidCol),col("rating").as(ratingCol))
      .toDF()
  }
  def getRecommenderItems:Dataset[(Int,Seq[Rating])] = {
    val recommenders = finalModel.recommendProductsForUsers(recommenderNum)
    val finalRecommender = ratingRDD.map(rating => (rating.user,rating))
      .groupByKey(20)
      .join(recommenders)
      .map{
        case (user,(ratingIter,remRatings)) =>
          val ratingItems = ratingIter.map(_.product).toSet
          val finalRating = mutable.Set[Rating]()
          for(remRating <- remRatings){
            if(!ratingItems.contains(remRating.product)){
              finalRating.add(remRating)
            }
          }
          (user,finalRating.toSeq.sortWith((r1,r2) =>r1.rating > r2.rating))

      }
    finalRecommender.toDS
  }
  override def toString: String = {
      s"""
        |rank = ${rank},lambda =${lambda},iterNum=${iterNum},mse=${finalMse}
        |
        |
      """.stripMargin
  }
  private def getMSE(model :MatrixFactorizationModel):Double = {
    val userProducts = ratingRDD.map{
      case Rating(user,product,rate) => (user,product)
    }
    val predictions = model.predict(userProducts)
      .map{
        case Rating(user,product,rate) => ((user,product),rate)
      }
    val rateAndPreds = ratingRDD.map{
      case Rating(user,product,rate) =>
        ((user,product),rate)
    }.join(predictions)
    val mean = rateAndPreds.map{
      case ((user,product),(r1,r2)) =>
        val err = r1 - r2
        err * err
    }.mean()

    mean
  }

}
