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
    val userTrans = new IdTrans()
      .setOutidCol("userId")
      .setInidCol("in_userid")

    val itemTrans = new IdTrans()
      .setOutidCol("itemId")
      .setInidCol("in_itemid")
    //userTrans.transform(recordDF).show()
    //itemTrans.transform(recordDF).show()

    val usertransDF = userTrans.transform(recordDF)
    usertransDF.show()
    val afterUserTransDF = userTrans.attach(usertransDF,recordDF)
    afterUserTransDF.show()
    val itemtransDF = itemTrans.transform(recordDF)
    itemtransDF.show()
    val afteritemTransDF = itemTrans.attach(itemtransDF,afterUserTransDF)
    afteritemTransDF.show()
    val inidDF = afteritemTransDF.select(userTrans.inidCol,itemTrans.inidCol)
    inidDF.show()
    val userReverseDF = userTrans.reverseTransform(usertransDF,inidDF)
    userReverseDF.show()
    val itemReverseDF = itemTrans.reverseTransform(itemtransDF,userReverseDF)
    itemReverseDF.show()






  }
}
