package com.bigdata.itemcf.example

import com.bigdata.itemcf.measures.PearsonCorrelationSimilarity
import org.apache.spark.mllib.recommendation.Rating
import org.apache.spark.sql.SparkSession
import com.bigdata.itemcf.ItemCFRecommender

/**
  * Created by ning on 2017/8/17.
  */
object ItemCFRecommenderJob {
  def main(args: Array[String]): Unit = {
    val input =  "data/test.csv"
    val spark = SparkSession.builder()
        .master("local[1]")
        .appName(this.getClass.getName.stripSuffix("$")).getOrCreate()

    import spark.implicits._
    val data = spark.sparkContext.textFile(input)
    val ratings = data.map(_.split(",") match {
      case Array(user, item, rate,_*) =>Rating(user.toInt, item.toInt, rate.toDouble)
    })
    val ratingDF = ratings.toDF()
    val itemCFRecommender = new ItemCFRecommender(spark)
    itemCFRecommender.setUseridCol("user")
      .setItemidCol("product")
      .setRatingCol("rating")
      .similarity(new PearsonCorrelationSimilarity())
    val items = itemCFRecommender.transform(ratingDF)
    //items.saveAsTextFile(output)
    items.show()

  }
}
