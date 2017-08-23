package com.bigdata.recommender.idtrans

import org.apache.spark.sql.SparkSession

/**
  * Created by ning on 2017/8/10.
  */
object IdTransJob {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local[1]")
      .appName(this.getClass.getName.stripSuffix("$"))
      .getOrCreate()
    import spark.implicits._
    val recordDF = Seq(
      ("u1","i1","2000"),
      ("u1","i2","2000"),
      ("u1","i3","2000"),
      ("u2","i1","2000"),
      ("u2","i2","2000"),
      ("u3","i1","2000")

    ).toDF("userId","itemId","year")
    val ir1 = new IdTrans()
      .setOutidCol("userId")
      .setInidCol("in_userid")

    val ir2 = new IdTrans()
      .setOutidCol("itemId")
      .setInidCol("in_itemid")
    //ir1.transform(recordDF).show()
    ir2.transform(recordDF).show()

  }
}
