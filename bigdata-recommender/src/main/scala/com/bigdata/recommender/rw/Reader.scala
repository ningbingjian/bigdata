package com.bigdata.recommender.rw

import org.apache.spark.sql.DataFrame

/**
  * Created by ning on 2017/8/2.
  */
trait Reader {
  def read():DataFrame
}
