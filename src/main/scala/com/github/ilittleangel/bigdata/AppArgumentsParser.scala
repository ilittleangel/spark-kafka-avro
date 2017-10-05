package com.github.ilittleangel.bigdata

import java.io.File

object AppArgumentParser extends scopt.OptionParser[AppArguments]("Job") {
  head("Job", "1.0.0")
  help("help").text("Print this usage text")
  version("version").text("Show version")

  opt[File]('c', "config")
    .required()
    .valueName("<Configuration File>")
    .action((x, c) => c.copy(conf = x))
    .validate(file => if (file.isFile && file.exists) success else failure("Config file must exist"))
    .validate(file => if (file.canRead) success else failure("Config file must be readeable"))
    .text("Path to the configuration file")

}

case class AppArguments(conf: File = new File("."))