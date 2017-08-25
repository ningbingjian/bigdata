package com.bigdata.recommender.rating

import org.apache.spark.sql.SparkSession

/**
  * Created by ning on 2017/8/2.
  */
object RatingJob {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local[1]")
      .appName("test").getOrCreate()
    import spark.implicits._
    Seq(
      ("u1","i1","play"),
      ("u2","i2","play"),
      ("u3","i3","like"),
      ("u3","i3","play")
    ).toDF("userId","itemId","action")
      .createOrReplaceTempView("user_action")
    val df = spark.sql("select * from user_action")
    val weightMap = Map("like" -> 0.3,"play" -> 4.8)
    val wr = new WeightMemoryReader(weightMap)
    val rating = new Rating()
    val transformDF = rating.setUserCol("userId")
      .setItemCol("itemId")
      .setActionCol("action")
      .setWeightReader(wr)
      .transform(df)
    transformDF.show()
  }

}
