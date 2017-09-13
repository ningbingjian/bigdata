import java.util
import java.util.regex.Pattern

import com.bigdata.zql.ZqlFlowManager
import com.jfinal.kit.Kv
import org.apache.spark.sql.SparkSession

import scala.collection.JavaConverters._
/**
  * Created by ning on 2017/7/28.
  */
object App {
  case class User(username:String,age:Int)
  case class Dept(name:String,num_person:Int)
  def main(args: Array[String]): Unit = {
    println(

      "a';'b;c;'     ;'d".split(";").mkString("\n ")
    )
/*    val spark = SparkSession.builder().master("local[1]")
      .appName("app").getOrCreate()
    import spark.implicits._
    val users = Seq(
      User("u1",1),
      User("u2",2),
      User("u3",3),
      User("u4",4),
      User("u5",5),
      User("u6",6),
      User("u7",7),
      User("u8",8),
      User("u9",9),
      User("u10",10)
    )
    val depts = Seq(
      Dept("human_resource",10),
      Dept("tech",15)
    )
    users.toDF().createOrReplaceTempView("user")
    depts.toDF().createOrReplaceTempView("dept")
    val zfm = ZqlFlowManager.builder().resource("user.zql").resource("dept/dept.zql").build()*/
    //Map("username" -> "u1").asJava


   /* val para = new util.HashMap[String,String]()
    para.put("username","1")
    val zql1 = zfm.findZqlFlow("user.sql1",para)
    assert(zql1.toDF(spark).collect()(0).getAs(0) == 11)
    val zql2 = zfm.findZqlFlow("user.sql2",para)
    zql2.toDF(spark).show()

    val zql3 = zfm.findZqlFlow("user.sql3")
    zql3.toDF(spark).show()*/
   val zfm = ZqlFlowManager.builder().resource("user.zql").resource("dept/dept.zql").build()
    val zql4 = zfm.findZqlFlow("dept.count")
    //val regex = """.*(/\*)(.*\r\n)*(\*/)(.*\r\n)*"""
    val regex = """(/\*)(.*\r\n)*.*(\*/)"""
    val pattern = Pattern.compile(regex)
    val s =
      """
        |/*
        |
        |  查询总人数
        |
        |  ----
        |  dfasfdfa
        |  @@@@@
        |   */
        |
        |selct count(*) from user
        |
        |/*
        |
        |  查询总人数
        |
        |  ----
        |  dfasfdfa
        |  @@@@@
        |   */
        |selct count(*) from user11111
        |
        |""".stripMargin

    //println(pattern.matcher(s).matches())
    /*val idx1 = s.indexOf("/*")
    val idx2 = s.indexOf("*/")
    val s1 = s.substring(0,idx1)
    val s2 = s.substring(idx2 + 2)
    println(s1 + s2)*/




  }

}
