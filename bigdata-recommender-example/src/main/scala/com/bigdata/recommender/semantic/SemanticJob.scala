package com.bigdata.recommender.semantic

import org.apache.spark.sql.SparkSession

/**
  * Created by ning on 2017/8/23.
  */
object SemanticJob {
  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir","D:\\tools\\hadoop-v2\\hadoop-2.6.0")
    val spark = SparkSession.builder()
      .master("local[1]")
      .appName(this.getClass.getName.stripSuffix("$"))
      .getOrCreate()
    import spark.implicits._
    val semantic = new Semantic()
      .setIdCol("id")
      .setFieldCol("title",true,false,0.1)
      .setFieldCol("body",true,false,0.1)
    val inputDF = Seq(
      ("01","中序遍历"," 在中序遍历中，我们递归使用中序遍历访问左子树，然后访问根节点，最后再递归使用中序遍历访问右子树"),
      ("02","后序遍历","在后序遍历中，我们先递归使用后序遍历访问左子树和右子树，最后访问根节点\n左子树->右子树->根节点")
    ).toDF("id","title","body")
    semantic.transform(inputDF)
      .collect()
      .foreach(println)
  }
}
