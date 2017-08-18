package com.bigdata.recommender.als

import org.apache.spark.sql.SparkSession
import org.apache.spark.mllib.recommendation.Rating
import org.apache.spark.mllib.recommendation.ALS
/**
  * Created by ning on 2017/8/3.
  */
object ALSRecommenderJob {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("test").getOrCreate()
    import spark.implicits._
    val data = spark.sparkContext.textFile("data/test.data")
    val ratings = data.map(_.split(",") match {
      case Array(user, item, rate,_*) =>Rating(user.toInt, item.toInt, rate.toDouble)
    })
    val alsR = new ALSRecommender(spark)
      .setRank(1000)
      .setRatingRDD(ratings)
      .setIterNum(10)

    alsR.train(false)
    alsR.getRecommenderItems.collect.foreach(r => println(r._1 +" ->" + r._2.mkString("::")))

  }
}
