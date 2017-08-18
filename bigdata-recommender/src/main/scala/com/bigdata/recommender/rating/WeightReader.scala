package com.bigdata.recommender.rating

/**
  * Created by ning on 2017/8/2.
  */
abstract class WeightReader extends Serializable{
  def getWeight(actionName:String):Double
  def getWeight(actionName:String,default:Double):Double
}
