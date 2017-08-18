package com.bigdata.common.command

import org.apache.commons.cli.{BasicParser, CommandLine, HelpFormatter, Option, Options, ParseException}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by ning on 2017/4/10.
  */
class CommandLineBuilder {
  val opts = ArrayBuffer[Option]()
  def opt(
    shortOpt:String,
    longOpt:String,
    hasArg:Boolean = true,
    description:String = "",
    require:Boolean = true): this.type ={
    val opt = new Option(shortOpt, longOpt , hasArg , description )
    opt.setRequired(require)
    opts.append(opt)
    this
  }
  def build(args: Array[String]): CommandLine ={
    val options: Options = new Options
    var cmd:CommandLine = null
    val parser = new BasicParser
    val formatter = new HelpFormatter
    for(opt <- opts){
      options.addOption(opt)
    }
    try {
      cmd = parser.parse(options, args)
    }catch {
      case e: ParseException => {
        System.out.println(e.getMessage)
        formatter.printHelp("utility-name", options)
        System.exit(1)
      }
    }
    cmd
  }
}
