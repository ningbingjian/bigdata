package com.bigdata.recommender.rw
import org.apache.spark.sql.{DataFrame, SaveMode}

/**
  * Created by ning on 2017/8/2.
  */
class HiveWriter extends Writer{
  var partitions :Seq[String] = _
  var format:String = "orc"

  var table: String = _
  var model:SaveMode = SaveMode.Append
  val customSave = (df:DataFrame) => Unit
  var isCustomSave = false

  def partitions(partitions:String *):this.type = {
    this.partitions = partitions.toSeq
    this
  }
  def model(saveMode: SaveMode) :this.type = {
    this.model = saveMode
    this
  }
  def format(format:String) :this.type = {
    this.format = format
    this
  }

  override def write(df: DataFrame):Unit = {
    if(isCustomSave){
      customSave(df)
      return
    }else{
      val df1 = df.write.format(format).mode(model)
      val df2 = if(partitions != null && partitions.size != 0){
        df1.partitionBy(partitions :_*)
      }else{
        df1
      }
      df2.saveAsTable(table)

    }

  }
}
object HiveWriter{
  val FORMAT_ORC = "orc"
  val FORMAT_PAQUET = "parquet"
}
