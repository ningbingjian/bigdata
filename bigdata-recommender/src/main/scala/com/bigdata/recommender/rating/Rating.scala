package com.bigdata.recommender.rating

import com.bigdata.recommender.rw.Writer
import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.collection._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.functions.col
/**
  * Created by ning on 2017/8/2.
  */

class Rating extends Serializable{ self =>
  var userCol = "userid"
  var itemCol = "itemid"
  var actionCol = "action"
  var existRating :DataFrame = _
  var weightReader:WeightReader = _
  var inputUserCol = "userid"
  var inputItemCol = "itemid"
  var ratingCol = "rating"

  def setInputUserCol(userCol:String):this.type = {
    self.inputUserCol = userCol
    self
  }
  def setInputItemCol(itemCol:String):this.type = {
    self.inputItemCol = itemCol
    self
  }
  def setUserCol(name:String) :this.type={
    self.userCol = name
    self
  }
  def setItemCol(name:String) :this.type = {
    self.itemCol = name
    self
  }
  def setActionCol(name:String) :this.type = {
    self.actionCol = name
    self
  }
  def setRatingCol(ratingCol:String):this.type = {
    self.ratingCol = ratingCol
    self
  }
  def setWeightReader(weightReader: WeightReader):this.type = {
    self.weightReader = weightReader
    self
  }
  def existRating(df:DataFrame):this.type = {
    existRating = df
    self
  }
  def transform(df:DataFrame):DataFrame = {
    df.sparkSession.udf.register("getWeight",(name:String) =>{
      weightReader.getWeight(name)
    })
    df.sparkSession.udf.register("rating_compute",(weights:Seq[Double]) => {
      ratingCompute(weights :_*)
    })
    println(df.schema)
    val weightDF = df.select(col(inputUserCol).as(userCol),col(inputItemCol).as(itemCol),expr(s"getWeight(${actionCol})").as(ratingCol))
    val unionDF = if(existRating != null) weightDF union existRating else weightDF
    compute(unionDF)
  }
  private def compute(df:DataFrame):DataFrame = {
    val weightsDF = df.groupBy(col(userCol),col(itemCol))
      .agg(collect_list(ratingCol).as(ratingCol))
      val ratingDF =  weightsDF.select(col(userCol),col(itemCol),expr(s"rating_compute(${ratingCol})").as(ratingCol))
    ratingDF
  }
  private def ratingCompute(ratings:Double*) = {
    var sum = 0.0
    for (rating <- ratings if rating != null) {
      var curRating = rating
      val preRatio = sum / Rating.ITEMCF_RATING_LIMIT
      val curRatio = rating / Rating.ITEMCF_RATING_LIMIT
      if ((preRatio + curRatio) > 1.0) {
        curRating = curRating * (1 - curRatio)
      }
      sum = Math.min(sum + curRating, Rating.ITEMCF_RATING_LIMIT)
    }
    sum = BigDecimal(sum).setScale(4, BigDecimal.RoundingMode.HALF_UP).toDouble
    sum
  }

}
object Rating{
  def apply(): Rating = new Rating()
  val ITEMCF_RATING_LIMIT = 5.0
}
