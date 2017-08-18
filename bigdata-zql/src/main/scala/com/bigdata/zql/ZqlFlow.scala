package com.bigdata.zql

import java.io.{File, FileInputStream, InputStream}

import com.jfinal.plugin.activerecord.sql.SqlKit
import com.jfinal.template.MemoryStringSource
import org.apache.commons.io.IOUtils
import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.collection.mutable.ListBuffer

/**
  * Created by ning on 2017/7/28.
  */
class ZqlFlowManager (bulder:ZqlFlowManager.Builder){
  val COMMENT_PATTERN = """(/\*)(.*[\r\n|\n|\r].*)*(\*/)"""

  val sqlKit = bulder.sqlKit
  def findZqlFlow(key:String):ZqlFlow = {
    var zqlBlock = getZqlBlock(key)
    var boolean = true
    while(boolean){
      val idx1 = zqlBlock.indexOf("/*")
      val idx2 = zqlBlock.indexOf("*/")
      if(idx1 >  0){
        if(idx2 < 0 ){
          throw new RuntimeException(s"找不到注释匹配符*/:/*的位置:${idx1},zql:${zqlBlock}")
        }else{
          val s1 = zqlBlock.substring(0,idx1)
          val s2 = zqlBlock.substring(idx2 + 2)
          zqlBlock = s1 + s2
        }
      }else{
        if(idx2 > 0 ){
          throw new RuntimeException(s"找不到注释匹配符/*:*/的位置:${idx2},zql:${zqlBlock}")
        }else{
          boolean = false
        }
      }
    }
    val zqls = zqlBlock.trim.split(";").toSeq
    new ZqlFlow(zqls)

  }
  def findZqlFlow(key:String,para:java.util.Map[String,_]):ZqlFlow = {
    val zqlBlock = getZqlBlock(key,para)
    val zqls = zqlBlock.replaceAll(COMMENT_PATTERN,"").trim.split(";").toSeq
        new ZqlFlow(zqls)
  }

  private def getZqlBlock(key: String): String = {
    sqlKit.getSql(key)
  }

  private def getZqlBlock(key: String, para: java.util.Map[String,_]): String = {
    val sp = sqlKit.getSqlPara(key, para)
    val paras = sp.getPara
    var sql = sp.getSql
    for (p <- paras) {
      sql = sql.replace("?", p.toString)
    }
    sql
  }
}
class ZqlFlow(zqls:Seq[String]){
  def toDF(spark:SparkSession): DataFrame ={
    var df:DataFrame = null
    for(zql <- zqls){
      val fields = zql.split(" +az +")
      df = spark.sql(fields(0))
      if(fields.length == 2){
        df.createOrReplaceTempView(fields(1))
      }
    }
    df
  }

}
object ZqlFlowManager {
  def builder(): Builder = new Builder

  class Builder {
    val sqlKit    = new SqlKit(this.getClass.getName.stripSuffix("$"))
    val resources = ListBuffer[String]()
    val files     = ListBuffer[File]()
    val streams   = ListBuffer[InputStream]()
    val zqlBlocks = ListBuffer[String]()

    def resource(resource :String): Builder = {
      resources append resource
      this
    }
    def file(file  : File): Builder = {
      files append file
      this
    }

    def stream(in  : InputStream): Builder = {
      streams append in
      this
    }


    def zqlBlock(block: String): Builder = {
      zqlBlocks append block
      this
    }

    def build(): ZqlFlowManager = {
      for (resource <- resources) {
        val content = IOUtils.toString(this.getClass.getClassLoader.getResourceAsStream(resource))
        sqlKit.addSqlTemplate(new MemoryStringSource(content))
      }
      for(file <- files ){
        val content = IOUtils.toString(new FileInputStream(file))
        sqlKit.addSqlTemplate(new MemoryStringSource(content))
      }
      for(stream <- streams){
        val content = IOUtils.toString(stream)
        sqlKit.addSqlTemplate(new MemoryStringSource(content))
      }
      for(zqlBlock <- zqlBlocks){
        val content = IOUtils.toString(new FileInputStream(zqlBlock))
        sqlKit.addSqlTemplate(new MemoryStringSource(content))
      }
      sqlKit.parseSqlTemplate()
      new ZqlFlowManager(this)
    }
  }

}
