package com.bigdata.recommender.rating

import org.apache.spark.sql.DataFrame

/**
  * Created by ning on 2017/8/3.
  */
trait ActionReader {
    def read():DataFrame
}
