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
    //ir2.transform(recordDF).show()

    val ir1transDF = ir1.transform(recordDF)
    ir1transDF.show()
    val ir1AfterTransDF = ir1.attach(ir1transDF,recordDF)
    ir1AfterTransDF.show()
    val ir2transDF = ir2.transform(recordDF)
    ir2transDF.show()
    val ir2AfterTransDF = ir2.attach(ir2transDF,ir1AfterTransDF)
    ir2AfterTransDF.show()
    val inidDF = ir2AfterTransDF.select(ir1.inidCol,ir2.inidCol)
    inidDF.show()
    val ir1ReverseDF = ir1.reverseTransform(ir1transDF,inidDF)
    ir1ReverseDF.show()
    val ir2ReverseDF = ir2.reverseTransform(ir2transDF,ir1ReverseDF)
    ir2ReverseDF.show()






  }
}
