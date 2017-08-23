package com.bigdata.recommender.semantic

import breeze.linalg.{DenseVector => BDV, SparseVector => BSV, Vector => BV}
import org.apache.spark.ml.linalg.{DenseVector => MDV, SparseVector => MSV, Vector => MLV, Vectors => MLVS}

/**
  * Created by ning on 2017/6/17.
  */
object Similariy{
  def cosine(v1:MLV,v2:MLV):Double={
    val bv1 = asBreeze(v1)
    val bv2 = asBreeze(v2)
    val dot = bv1.dot(bv2)
    val norm = MLVS.norm(v1,2) * MLVS.norm(v2,2)
    val result = dot / norm
    result
  }
  def asBreeze(vector:MLV):BV[Double] = {
    vector match {
      case v:MDV => new BDV[Double](v.values)
      case v:MSV => new BSV[Double](v.indices,v.values,v.size)
    }
  }



}
