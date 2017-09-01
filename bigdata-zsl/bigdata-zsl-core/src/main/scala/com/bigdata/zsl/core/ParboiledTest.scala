package com.bigdata.zsl.core
import shapeless.{HList, HNil}

import scala.annotation.tailrec
import scala.io.StdIn
import scala.util.{Failure, Success}

/**
  *
  * Created by ning on 2017/8/31.
  * User:ning
  * Date:2017/8/31
  * tIME:15:23
  *
  */
object ParboiledTest {
  import org.parboiled2._

  class Calculator(val input: ParserInput) extends Parser {
    def InputLine = rule { Expression ~ EOI }

    def Expression: Rule1[Int] = rule {
      Term ~ zeroOrMore(
        '+' ~ Term ~> ((_: Int) + _)
          | '-' ~ Term ~> ((_: Int) - _))
    }

    def Term = rule {
      Factor ~ zeroOrMore(
        '*' ~ Factor ~> ((_: Int) * _)
          | '/' ~ Factor ~> ((_: Int) / _))
    }

    def Factor = rule { Number | Parens }

    def Parens = rule { '(' ~ Expression ~ ')' }

    def Number = rule { capture(Digits) ~> (_.toInt) }

    def Digits = rule { oneOrMore(CharPredicate.Digit) }


  }

  class Test1(val input:ParserInput) extends Parser{
    def InputLine = rule { EXpr ~ EOI }
    def EXpr  = rule {
      'a' ~ ('b' ~ 'c' | 'b' ~ 'd')
    }
  }
  class Test2(val input:ParserInput) extends Parser{
    def InputLine = rule { Expr ~ EOI }
    def Expr  = rule {
      selects | load
    }
    def selects = rule {
      str("select") ~ oneOrMore(space)
    }
    def load = rule {
      "load" ~ oneOrMore(space)
    }

    def space = rule {
      " "
    }

    def Number = rule { capture(Digits) ~> (_.toInt) }

    def Digits = rule { oneOrMore(CharPredicate.Digit) }
  }
  object Calculator2 extends App {
    repl()

    @tailrec
    def repl(): Unit = {
      // TODO: Replace next three lines with `scala.Predef.readLine(text: String, args: Any*)`
      // once BUG https://issues.scala-lang.org/browse/SI-8167 is fixed
      print("---\nEnter calculator expression > ")
      Console.out.flush()
      StdIn.readLine() match {
        case "" =>
        case line =>
          val parser = new Calculator2(line)
          parser.InputLine.run() match {
            case Success(exprAst)       => println("Result: " + eval(exprAst))
            case Failure(e: ParseError) => println("Expression is not valid: " + parser.formatError(e))
            case Failure(e)             => println("Unexpected error during parsing run: " + e)
          }
          repl()
      }
    }

    def eval(expr: Expr): Int =
      expr match {
        case Value(v)             => v.toInt
        case Addition(a, b)       => eval(a) + eval(b)
        case Subtraction(a, b)   => eval(a) - eval(b)
        case Multiplication(a, b) => eval(a) * eval(b)
        case Division(a, b)       => eval(a) / eval(b)
      }

    // our abstract syntax tree model
    sealed trait Expr
    case class Value(value: String) extends Expr
    case class Addition(lhs: Expr, rhs: Expr) extends Expr
    case class Subtraction(lhs: Expr, rhs: Expr) extends Expr
    case class Multiplication(lhs: Expr, rhs: Expr) extends Expr
    case class Division(lhs: Expr, rhs: Expr) extends Expr
  }
  class Calculator2(val input: ParserInput) extends Parser {
      import Calculator2._

      def InputLine = rule { Expression ~ EOI }

      def Expression: Rule1[Expr] = rule {
        Term ~ zeroOrMore(
          '+' ~ Term ~> Addition
            | '-' ~ Term ~> Subtraction)
      }

      def Term = rule {
        Factor ~ zeroOrMore(
          '*' ~ Factor ~> Multiplication
            | '/' ~ Factor ~> Division)
      }

      def Factor = rule { Number | Parens }

      def Parens = rule { '(' ~ Expression ~ ')' }

      def Number = rule { capture(Digits) ~> Value }

      def Digits = rule { oneOrMore(CharPredicate.Digit) }
}

  def main(args: Array[String]): Unit = {
    //  println(new Calculator("1+1+1+2+1*2").InputLine.run()) // evaluates to `scala.util.Success(2)`
    //println(new Test1("abc").InputLine.run())
    //println(new Test1("abb").InputLine.run())
    println(new Test2("select             ").InputLine.run())
    println(new Test2("load             ").InputLine.run())
  }
}
