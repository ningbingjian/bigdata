package com.bigdata.recommender.als

import org.apache.spark.sql.SparkSession
import org.apache.spark.mllib.recommendation.Rating
import org.apache.spark.mllib.recommendation.ALS

/**
  * Created by ning on 2017/8/3.
  *
  * nohup  /hwdata/spark2.x/bin/spark-submit \
  * --class com.bigdata.recommender.als.ALSRecommenderJob \
  * --master yarn \
  * --num-executors 20 \
  * --executor-memory 4G    \
  * bigdata-recommender-example-0.1.jar yarn  \
  * /user/ning/als/in/* /user/ning/als/out , &> log.log &
  *
  */
  *
  */
object ALSRecommenderJob {
  def main(args: Array[String]): Unit = {
    val (master,input,output,ratingDelimiter) = (
      args(0),
      args(1),
      args(2),
      args(3)
    )
    val spark = SparkSession.builder()
      .appName(this.getClass.getName.stripSuffix("$"))
      .master(master)
      .getOrCreate()
    import spark.implicits._
    val data = spark.sparkContext.textFile(input)
    val ratings = data.map(_.split(ratingDelimiter) match {
      case Array(user, item, rate,_*) =>Rating(user.toInt, item.toInt, rate.toDouble)
    }).repartition(200)
    val alsR = new ALSRecommender(spark)
      .setRank(15)
      .setRatingRDD(ratings)
      .setIterNum(10)


    alsR.train(false)
    alsR.getRecommenderItems.rdd.saveAsTextFile(output)

  }
}
