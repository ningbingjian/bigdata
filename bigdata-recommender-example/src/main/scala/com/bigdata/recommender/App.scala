package com.bigdata.recommender

import java.util.Properties

import org.apache.spark.sql.{Row, SaveMode, SparkSession}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by ning on 2017/8/23.
  */
object App {
  def main(args: Array[String]): Unit = {

    /*val spark = SparkSession.builder()
      .master("local[1]")
      .appName(this.getClass.getName.stripSuffix("$"))
      .getOrCreate()*/
    //test1(spark)
    //test2(spark)
    //test3(spark)
    test4()
  }
  def test4(){
    val spark = SparkSession.builder().appName("test").master("local[1]")
      .getOrCreate();
    val hiveContext = spark.sqlContext
    import hiveContext.implicits._
    val dataList: List[(Int, String, Int, Int, String, Int, Int, Int, Int)] = List(
      (0, "male", 37, 37, "no", 3, 18, 7, 4),
      (1, "female", 27, 37, "no", 4, 14, 6, 4))/*,
      (0, "female", 32, 37, "yes", 1, 12, 1, 4),
      (0, "male", 57, 37, "yes", 5, 18, 6, 5),
      (0, "male", 22, 37, "no", 2, 17, 6, 3),
      (0, "female", 32, 37, "no", 2, 17, 5, 5),
      (0, "female", 22, 37, "no", 2, 12, 1, 3),
      (0, "male", 57, 15, "yes", 2, 14, 4, 4),
      (0, "female", 32, 15, "yes", 4, 16, 1, 2))*/

    val colArray: Array[String] = Array("affairs", "gender", "age", "yearsmarried", "children", "religiousness", "education", "occupation", "rating")
    val df = dataList.toDF("affairs", "gender", "age", "yearsmarried", "children", "religiousness", "education", "occupation", "rating")
    val props = new Properties();
    props.setProperty("user" , "HwOpsDba")
    props.setProperty("password" , "OPShuanwang2017")
    props.setProperty("batchsize" , "100")
    df.write.mode(SaveMode.Append).jdbc(
      "jdbc:mysql://bigdata-yao.ccey9rf1org0.rds.cn-north-1.amazonaws.com.cn:3306/test",
      "test",
      props)


    /*.options(
      Map(
        "driver" -> "com.mysql.jdbc.Driver",
        "url" -> "bigdata-yao.ccey9rf1org0.rds.cn-north-1.amazonaws.com.cn:3306",
        "dbtable" -> "bigdata-yao.test",
    "user" -> "HwOpsDba",
    "password" -> "OPShuanwang2017",
    "batchsize" -> "1000",
    "truncate" -> "true")).save()*/
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
