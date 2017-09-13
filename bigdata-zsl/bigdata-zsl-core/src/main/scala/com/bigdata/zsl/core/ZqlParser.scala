package com.bigdata.zsl.core

import org.parboiled2.ParserInput
import org.parboiled2._

import scala.collection.immutable
/**
  *
  * Created by ning on 2017/9/1.
  * User:ning
  * Date:2017/9/1
  * tIME:14:56
  *
  */
object ZqlParser {
  trait  Zql{
  }
  case class Load(sql:String) extends Zql
  case class Zqls(zql: immutable.Seq[_ <: Zql])
  def apply(zql:String) = {
    import Parser.DeliveryScheme.Either
    val parser = new ZqlParser(zql)
    parser.zql.run()
  }

  def main(args: Array[String]): Unit = {
    val sql1 =
      """
        |load
      """.stripMargin
    println(ZqlParser(sql1))



  }
}
class ZqlParser(val input:ParserInput) extends Parser with StringBuilding{
  val SPACE = CharPredicate.Printable -- CharPredicate.Visible
  def zql = rule {
    parse ~ EOI
  }
  def parse = rule {
    zeroOrMore(loadParse)/* | zeroOrMore(select) | zeroOrMore(save)*/
  }
  def loadParse = rule {
      load ~ format ~ params ~ schema ~ conditions ~ as ~ table
  }
  def load = rule {
    BLANK ~ "load" ~ BLANK
  }
  def as = rule {
    ""
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
  def BLANK = rule {
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
