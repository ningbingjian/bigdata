package com.bigdata.recommender.rating

import com.bigdata.recommender.rw.Writer
import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.collection._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.functions.col
/**
  * Created by ning on 2017/8/2.
  */

class Rating extends Serializable{
  val paramMap = mutable.Map[String,Any]()
  var existRating :DataFrame = _
  var weightReader:WeightReader = _
  def setUserCol(name:String) :this.type={
    paramMap(Rating.userCol) = name
    this
  }
  def setItemCol(name:String) :this.type = {
    paramMap(Rating.itemCol) = name
    this
  }
  def setActionCol(name:String) :this.type = {
    paramMap(Rating.actionCol) = name
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
    val userId = paramMap(Rating.userCol).asInstanceOf[String]
    val itemId = paramMap(Rating.itemCol).asInstanceOf[String]
    val action = paramMap(Rating.actionCol).asInstanceOf[String]
    val weightDF = df.select(col(userId).as(Rating.userCol),col(itemId).as(Rating.itemCol),col(action).as(Rating.actionCol),expr(s"getWeight(${action})").as(Rating.weightCol))
    def compute(df:DataFrame) = {
      weightDF.groupBy(col(Rating.userCol),col(Rating.itemCol))
        .agg(expr(s"collect_set(${Rating.weightCol})").as(Rating.weightCol))
        .select(col(Rating.userCol),col(Rating.itemCol),expr(s"rating_compute(${Rating.weightCol})").as(Rating.weightCol))
    }

    var curRatingDF = compute(weightDF)
    curRatingDF.show()
    if(existRating != null){
      val unionDF = curRatingDF.union(existRating)
      compute(unionDF)
    }
    curRatingDF
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
  val userCol = "userid"
  val itemCol = "itemid"
  val actionCol = "action"
  val weightCol = "weight"
  val ITEMCF_RATING_LIMIT = 5.0
}
