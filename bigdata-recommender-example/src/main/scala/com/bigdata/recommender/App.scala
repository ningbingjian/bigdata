package com.bigdata.recommender

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.Row
import scala.collection.mutable.ArrayBuffer

/**
  * Created by ning on 2017/8/23.
  */
object App {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local[1]")
      .appName(this.getClass.getName.stripSuffix("$"))
      .getOrCreate()
    //test1(spark)
    //test2(spark)
    test3(spark)
  }

  /**
  * 正确的写法
  * @param spark
    */
  def test3(spark:SparkSession): Unit ={
    import spark.implicits._
    val ab = ArrayBuffer[(String,Double)]()
    ab.append(("1",1.0))
    ab.append(("2",2.0))
    val df = Seq(ab.toArray).toDF("w")
    df.rdd.map(r=>{
      val weights = r.getAs[Seq[Row]]("w")
      val items = weights.map(_.get(1))
      //val items = weights.map(_.get(1))
      items
    }).collect().foreach(println)
  }

  /**
    * 执行报错 java.lang.ClassCastException:
    * org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema
    * cannot be cast to scala.Tuple2
    * @param spark
    */

  def test2(spark:SparkSession): Unit ={
    import spark.implicits._
    val ab = ArrayBuffer[(String,Double)]()
    ab.append(("1",1.0))
    ab.append(("2",2.0))
    val df = Seq(ab.toArray).toDF("w")
    df.rdd.map(r=>{
      val weights = r.getAs[Seq[(String,Double)]]("w")
      val items = weights.map(_._2)
      items
    }).collect().foreach(println)
  }
  def test1(spark:SparkSession): Unit ={
    import spark.implicits._
    val ab = ArrayBuffer[Double]()
    ab.append(1.0)
    ab.append(2.0)
    val df = Seq(ab.toArray).toDF("w")
    df.rdd.map(r=>{
      val weights = r.getAs[Seq[Double]]("w")
      val items = weights
      items
    }).collect().foreach(println)
  }
}
