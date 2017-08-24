package com.bigdata.recommender.semantic

import breeze.linalg.{DenseVector => BDV, SparseVector => BSV, Vector => BV}
import org.apache.spark.ml.linalg.{DenseVector => MDV, SparseVector => MSV, Vector => MLV, Vectors => MLVS}

/**
  *
  * Created by zhaoshufen
  * User:  zhaoshufen
  * Date: 2017/8/23
  * Time: 22:18
  * To change this setting on:Preferences->editor->File and Code Templates->Include->File Header
  *
  * (1048576,
  * [31814,159744,244308,451013,618417,650162,772719,805420,892626,944019,1004627],
  * [0.0,0.0,0.0,0.0,0.0,0.0,0.4054651081081644,0.0,0.8109302162163288,0.0,0.0])
  *
  * (1048576,
  * [31814,159744,244308,451013,618417,650162,725644,805420,828178,944019,968918,1004627],
  * [0.0,0.0,0.0,0.0,0.0,0.0,0.4054651081081644,0.0,1.2163953243244932,0.0,0.4054651081081644,0.0])
  */
object SimilariySuite {
  def main(args: Array[String]): Unit = {
    val i1 = "31814,159744,244308,451013,618417,650162,772719,805420,892626,944019,1004627"
    val s1 = "0.0,0.0,0.0,0.0,0.0,0.0,0.4054651081081644,0.0,0.8109302162163288,0.0,0.0"
    val value1 = i1.split(",").map(_.toInt) zip  s1.split(",").map(_.toDouble)

    val i2 = "31814,159744,244308,451013,618417,650162,725644,805420,828178,944019,968918,1004627"
    val s2 = "0.0,0.0,0.0,0.0,0.0,0.0,0.4054651081081644,0.0,1.2163953243244932,0.0,0.4054651081081644,0.0"
    val value2 = i2.split(",").map(_.toInt) zip  s2.split(",").map(_.toDouble)


    val v1 = MLVS.sparse(1048576,value1)
    val v2 = MLVS.sparse(1048576,value2)
    val v3 = MLVS.sparse(3,Seq((0,2.0),(1,3.0)))
    val v4 = MLVS.sparse(3,Seq((0,2.0),(1,3.0)))
    println(Similariy.cosine(v1,v2))
  }
}
