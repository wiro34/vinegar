package jp.co.so_net.vinegar

import jp.co.so_net.vinegar.facade.Require
import jp.co.so_net.vinegar.facade.lodash.Lodash
import jp.co.so_net.vinegar.facade.minimist.Minimist
import jp.co.so_net.vinegar.facade.path.Path
import jp.co.so_net.vinegar.generator.ExcelGenerator

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName
import js.Dynamic.{global => g}

@js.native
class VinegarOptions extends js.Object {
  @JSName("_")
  val _args: js.Array[String] = js.native

  val o: js.UndefOr[String] = js.native

  val out: js.UndefOr[String] = js.native
}

object VinegarMain extends js.JSApp {
  val lodash = Require[Lodash]("lodash")
  val minimist = Require[Minimist]("minimist")
  val fs = g.require("fs")
  val path = Require[Path]("path")

  def main(): Unit = {
    val options = minimist(g.process.argv.slice(2)).asInstanceOf[VinegarOptions]
    if (options._args.length > 0) {
      val infile = options._args(0)
      val outDir = options.o.orElse(options.out).getOrElse(path.join(infile, "../"))
      fs.access(infile, fs.R_OK, (err: Any) => {
        if (!lodash.isObject(err)) generateExcel(infile, outDir)
        else exitWithError(s"File '${infile}' is not found")
      })
    } else {
      usage
    }
  }

  def generateExcel(infile: String, outDir: String): Unit = {
    val outfile = path.join(outDir, path.basename(infile).replaceAll(path.extname(infile), ".xlsx"))
    fs.readFile(infile, "utf8", (err: Any, text: String) => {
      try {
        if (!lodash.isObject(err)) ExcelGenerator.parse(text).generate(outfile)
        else exitWithError("ERROR: " + err)
      } catch {
        case e: Exception =>
          println(e)
          exitWithError(e.getMessage)
      }
    })
  }

  def exitWithError(message: String): Unit = {
    System.err.println(message)
    // System.exit(1)
    g.process.exit(1)
  }

  def usage: Unit = {
    println("Usage: vinegar [options] file")
    println()
    println("  options:")
    println("    -o,--out     output file")
  }
}
