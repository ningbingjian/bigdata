package com.bigdata.recommender.rating
/**
  * Created by ning on 2017/8/2.
  */
class WeightMemoryReader(weightMap:Map[String,Double]) extends WeightReader{
  override def getWeight(actionName:String):Double = {
    weightMap(actionName)
  }

  override def getWeight(actionName: String, default: Double):Double = {
    weightMap.getOrElse(actionName,default)
  }
}
