package com.bigdata.recommender.idtrans

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import org.apache.spark.sql.{DataFrame, Row, RowFactory, SaveMode}
import org.apache.spark.sql.functions._

import scala.collection.mutable.ListBuffer

/**
  * Created by ning on 2017/8/8.
  */
class IdTrans{ self =>
  var outidCol:String = _
  var inidCol:String = _
  def setOutidCol(outidCol:String):this.type = {
    self.outidCol = outidCol
    self
  }
  def setInidCol(inidCol:String):this.type = {
    self.inidCol = inidCol
    self
  }
  def transform(df:DataFrame):DataFrame = {
    val spark = df.sparkSession
    import spark.implicits._
    val idDF = df.select(col(outidCol)).distinct()
    val transDF = idDF.repartition(1)
        .rdd
        .mapPartitions(rows => {
          val dt = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
          var curid = 0
          val newRows = ListBuffer[(Int,String,String)]()
          for(row <- rows){
            val outid = row.get(0).toString
            curid = curid + 1
            newRows.append((curid,outid,dt))
          }
          newRows.toIterator
        }).toDF(s"${inidCol}",s"${outidCol}","dt")
    transDF
  }

}
