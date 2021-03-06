package com.bigdata.recommender.idtrans

import java.text.SimpleDateFormat
import java.util.Date

import org.apache.spark.sql.{DataFrame, Row, RowFactory, SaveMode}
import org.apache.spark.sql.functions._

import scala.collection.mutable.ListBuffer

/**
  * Created by ning on 2017/8/8.
  */
class IdTrans extends Serializable{ self =>
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

  /**
    *
    * @param df
    * @return 返回第一个df是映射关系 ，第二个DF是将int id添加到原来的DF
    */
  def transform(df:DataFrame):DataFrame = {
    val spark = df.sparkSession
    val idDF = df.select(col(outidCol)).distinct()
    //idTransWithOnePartiton(idDF)
    idTransWithPartitionIndex(idDF)
  }
  private def idTransWithPartitionIndex(idDF:DataFrame):DataFrame = {
    import idDF.sparkSession.implicits._
    val numPartitions = 5
    val step = (Integer.MAX_VALUE - 10000 * 10000) / (numPartitions + 1)
    idDF.rdd.repartition(numPartitions).mapPartitionsWithIndex{
      case (idx,iter) =>
        val buf = ListBuffer[(Int,String)]()
        val ids = iter.toSeq.map(_.get(0).toString).sorted
        var curIdx =  step * idx + 1
        for(id <- ids ){
          buf.append((curIdx,id))
          curIdx = curIdx + 1
        }
        buf.toIterator
    }.toDF(s"${inidCol}",s"${outidCol}")
  }
  private def idTransWithOnePartiton(idDF:DataFrame):DataFrame = {
    import idDF.sparkSession.implicits._
    val transDF = idDF.repartition(1)
      .rdd
      .map(_.get(0).toString)
      .mapPartitions(ids => {
        val sortIds = ids.toSeq.sorted
        var curid = 0
        val newRows = ListBuffer[(Int,String)]()
        for(id <- sortIds){
          val outid = id
          curid = curid + 1
          newRows.append((curid,outid))
        }
        newRows.toIterator
      }).toDF(s"${inidCol}",s"${outidCol}")
    transDF
  }


  /**
    *
    * @param df
    * @return 返回第一个df是映射关系 ，第二个DF是将int id添加到原来的DF
    */
  def transformAndAttach(df:DataFrame):DataFrame = {
    val transDF = transform(df)
    val afterDF = df.alias("t1").join(transDF.alias("t2"),outidCol)
    //  .select("t1.*",s"t2.${inidCol}")
    afterDF

  }

  /**
    * 将转换好的内部id列附上原来的DataFrame
    * @param transDF
    * @param df
    * @return
    */
  def attach(transDF:DataFrame,df:DataFrame):DataFrame = {
    val afterDF = df.alias("t1").join(transDF.alias("t2"),outidCol)
    afterDF
  }

  /**
    * 将内部的int类型的id 反向转换回外部id
    * @param transDF
    * @param df
    * @return
    */
  def reverseTransform(transDF:DataFrame,df:DataFrame):DataFrame = {
    df.alias("t1").join(transDF.alias("t2"),inidCol)
    //  .select("t1.*",s"t2.${outidCol}")
  }







}
