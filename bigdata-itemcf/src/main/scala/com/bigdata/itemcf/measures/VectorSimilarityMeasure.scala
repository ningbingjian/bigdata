package com.bigdata.itemcf.measures

import com.bigdata.itemcf.EnHanceSparseVector
import org.apache.spark.mllib.linalg.{SparseVector, Vectors, Vector => MLV}


/**
  * Created by zhaoshufen on 2017/5/1.
  */
abstract class VectorSimilarityMeasure extends Serializable{
  implicit def toEnhance(vector:SparseVector) = new EnHanceSparseVector(vector)

  val NO_NORM = 0.0
  def norm(vector: MLV): Double
  def normalize(vector:MLV):MLV
  def aggregate(nonZeroValueA: Double, nonZeroValueB: Double):Double
  def similarity(summedAggregations: Double, normA: Double, normB: Double, numberOfColumns: Int): Double
}

class CosineSimilarity extends VectorSimilarityMeasure{
  override def norm(vector: MLV) = NO_NORM
  override def normalize(vector: MLV):MLV = {
    vector match {
      case v:SparseVector => v.normalize()
      case _ => throw new UnsupportedOperationException("unsupport vector :" +vector.getClass)
    }
  }

  override def similarity(summedAggregations: Double,
                          normA: Double,
                          normB: Double,
                          numberOfColumns: Int):Double = summedAggregations
  override def aggregate(nonZeroValueA: Double, nonZeroValueB: Double) : Double = {
    nonZeroValueA * nonZeroValueB
  }
}

class PearsonCorrelationSimilarity extends CosineSimilarity{
  override def normalize(vector: MLV):MLV = {
    vector match {
      case v:SparseVector =>
        val avg = Vectors.norm(vector,1) / vector.numNonzeros
        v.eachValue(v => v-avg)
        v.normalize()
    }

  }
}



