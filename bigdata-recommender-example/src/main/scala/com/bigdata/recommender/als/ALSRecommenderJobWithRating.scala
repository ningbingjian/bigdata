package com.bigdata.recommender.als

import com.bigdata.common.command.CommandLineBuilder
import com.bigdata.recommender.idtrans.IdTrans
import com.bigdata.zql.ZqlFlowManager
import org.apache.spark.mllib.recommendation.Rating
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

/**
  * Created by ning on 2017/8/11.
  */
object ALSRecommenderJobWithRating {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local[1]")
      .appName("test").getOrCreate()
    import spark.implicits._
    val data = spark.sparkContext.textFile("data/test1.data")
    val ratings = data.map(_.split(",") match {
      case Array(user, item, rate,_*) =>(user, item, rate.toDouble)
    })
    val ratingOuterDF = ratings.toDF("out_userid","out_itemid","rating")
    val userIdTrans = new IdTrans().setInidCol("in_userid")
      .setOutidCol("out_userid")
    val userTransDF = userIdTrans.transform(ratingOuterDF)
    val itemIdTrans = new IdTrans().setInidCol("in_itemid").setOutidCol("out_itemid")
    val itemTransDF = itemIdTrans.transform(ratingOuterDF)
    ratingOuterDF.createOrReplaceTempView("ratingpref")
    userTransDF.createOrReplaceTempView("usertrans")
    itemTransDF.createOrReplaceTempView("itemtrans")
    val zfm = ZqlFlowManager.builder().resource("recommender.zql").build()
    val zqlFlow = zfm.findZqlFlow("recommender.rating_join_trans")
    val transAndRatingDF = zqlFlow.toDF(spark)
    val ratingRDD = transAndRatingDF.select(col("in_userid").as("user"),col("in_itemid").as("product"),col("rating"))
      .as[Rating]
      .rdd
    val alsR = new ALSRecommender(spark)
      .setRecommenderNum(5)
      .setRank(7)
      .setIterNum(5)
      .setRatingRDD(ratingRDD)
    alsR.train(false)
    //alsR.getRecommenderItems.collect.foreach(r => println(r._1 +" ->" + r._2.mkString("::")))
    val recommenderItem = alsR.getRecommenderItems
    val itemTransMap = itemTransDF.rdd.map(row => {
      (row.getAs[Int]("in_itemid"),row.getAs[String]("out_itemid"))
    }).collect().toMap
    val userTransMap = userTransDF.rdd.map(row => {
      (row.getAs[Int]("in_userid"),row.getAs[String]("out_userid"))
    }).collect().toMap
    val itemTransMapBc = spark.sparkContext.broadcast(itemTransMap)
    val userTransMapBc = spark.sparkContext.broadcast(userTransMap)
    val itemsToRecommender = recommenderItem.map{
      case (user,rating) =>
        val userid = userTransMapBc.value(user)
        val items = rating.map{
          case rating:Rating =>
            (itemTransMapBc.value(rating.product),rating.rating)
        }
        (userid,items)
    }
  }
}
