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
  var weightCol = "weight"
  var transIdColPrefix = "int_"
  var existRating :DataFrame = _
  var weightReader:WeightReader = _
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
    this
  }
  def setWeightReader(weightReader: WeightReader):this.type = {
    this.weightReader = weightReader
    this
  }
  def existRating(df:DataFrame):this.type = {
    existRating = df
    this
  }
  def transform(df:DataFrame):DataFrame = {
    df.sparkSession.udf.register("getWeight",(name:String) =>{
      weightReader.getWeight(name)
    })
    df.sparkSession.udf.register("rating_compute",(weights:Seq[Double]) => {
      ratingCompute(weights :_*)
    })
    val weightDF = df.select(col(userCol),col(itemCol),col(actionCol),expr(s"getWeight(${actionCol})").as(weightCol))
    def compute(df:DataFrame) = {
      df.groupBy(col(userCol),col(itemCol))
        .agg(expr(s"collect_set(${weightCol})").as(weightCol))
        .select(col(userCol),col(itemCol),expr(s"rating_compute(${weightCol})").as(weightCol))
    }
    var curRatingDF = compute(weightDF)
    if(existRating != null){
      val unionDF = curRatingDF.union(existRating)
      compute(unionDF)
    }else{
      curRatingDF
    }
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
