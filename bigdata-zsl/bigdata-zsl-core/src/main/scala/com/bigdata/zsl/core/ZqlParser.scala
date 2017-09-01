package com.bigdata.zsl.core

import org.parboiled2.ParserInput
import org.parboiled2._
/**
  *
  * Created by ning on 2017/9/1.
  * User:ning
  * Date:2017/9/1
  * tIME:14:56
  *
  */
object ZqlParser {
  case class Load(sql:String)
  def apply(zql:String) = {
    import Parser.DeliveryScheme.Either
    val parser = new ZqlParser(zql)
    parser.zql.run()
  }

  def main(args: Array[String]): Unit = {
    val sql1 ="   load tv.user as t1_1 ;"
    println(ZqlParser(sql1))
    val sql2 ="  \n load tv.user as t1_1 ; \r\n"
    println(ZqlParser(sql2))
    val sql3 = sql1 + sql2
    println(ZqlParser(sql3))



  }
}
class ZqlParser(val input:ParserInput) extends Parser with StringBuilding{
  val SPACE = CharPredicate.Printable -- CharPredicate.Visible
  def zql = rule {
     sql ~ EOI
  }
  def sql = rule {
    zeroOrMore(load) | zeroOrMore(select) | zeroOrMore(save)
  }
  def load = rule {
      "load" ~ format ~ params ~ schema ~ conditions ~ as ~ table
  }
  def as = rule {
    "as"
  }

  def format = rule {
    ""
  }
  def params = rule {
    ""
  }
  def schema = rule {
    ""
  }
  def conditions = rule {
    ""
  }

  def table = rule {
    hiveTable | jdbcTable
  }

  def hiveTable = rule {
    VARIABLE ~ "." ~ VARIABLE
  }
  def jdbcTable = rule {
    ""
  }

  def select = {
    ""
  }
  def save = rule {
    ""
  }
  def NL = rule { optional('\r') ~ '\n' }

  def MULNL = rule {
    zeroOrMore(NL)
  }
  def GAP = rule {
    OWS ~ MULNL ~ OWS ~ MULNL
  }
  def VARIABLE = rule {
    oneOrMore(CharPredicate.Alpha | CharPredicate.Digit | "_")
  }
  def OWS = rule { zeroOrMore(' ') }

  def SEMICOLON = rule {
    ";"
  }
  def BACKQUOTE = rule {
    "`"
  }

}
