package com.bigdata.recommender.rating

import org.apache.spark.sql.SparkSession

/**
  *
  * Created by ning on 2017/9/7.
  * User:ning
  * Date:2017/9/7
  * tIME:17:46
  *
  */
object App {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("App")
      .master("local[1]")
      .getOrCreate()
    import spark.implicits._
    Seq(1,2,3,4,5).toDF("num").createOrReplaceTempView("numbers")
    val r = spark.sql(
      """
        |
        |select avg(num) from numbers
      """.stripMargin)
      .collect().mkString("\n")
    println(r)
  }

}
