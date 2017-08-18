package com.bigdata.recommender.rating

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
  * Created by ning on 2017/8/3.
  */
class SqlActionReader(sql:String,spark:SparkSession) extends ActionReader {
  override def read():DataFrame= {
    spark.sql(sql)
  }
}
