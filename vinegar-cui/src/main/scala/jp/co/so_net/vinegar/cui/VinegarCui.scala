package jp.co.so_net.vinegar.cui

import java.nio.file.{Files, Path, Paths}

import com.norbitltd.spoiwo.model.Sheet
import jp.co.so_net.vinegar.exporter.{Exporters, FileWriter, FileWriters}
import jp.co.so_net.vinegar.parser.GherkinParser

import scala.util.{Failure, Try}

object VinegarCui extends App {
  VinegarOptionParser
    .parse(args, VinegarCuiOption())
    .foreach(config => new VinegarCui(config, new TextFileReader, FileWriters.ExcelFileWriter).run())
}

class VinegarCui(config: VinegarCuiOption,
                 reader: FileReader,
                 writer: FileWriter[Sheet]) extends Console with SystemTerminator with DirectoryUtil {

  val inputFilepath = Paths.get(config.file.getAbsolutePath)
  val outputFilepath = Paths.get(generateOutputFilename(inputFilepath))
  val newLine: String = Try(System.getProperty("line.separator")).getOrElse("\n")

  def run(): Unit = Try {
    if (config.force) makeDirRecursive(outputFilepath.getParent)
    reader
      .read(inputFilepath.toFile)
      .flatMap(text => GherkinParser.parse(text))
      .map(feature => Exporters.ExcelExporter.export(feature))
      .flatMap(sheet => writer.write(outputFilepath.toString, sheet))
  }.flatten match {
    case Failure(e) =>
      val sb = new StringBuilder
      sb.append("Error: " + e.getMessage + newLine)
      e.getStackTrace.foreach(st => sb.append(st + newLine))
      errorAndExit(sb.toString())
    case _ =>
  }

  private def generateOutputFilename(path: Path): String = {
    val filename = path.getFileName.toString
    val basename = filename.lastIndexOf('.') match {
      case i if i >= 0 => filename.substring(0, i)
      case _ => filename
    }
    val outputPath = config.out.map(_.toPath).getOrElse(path.getParent)
    outputPath.resolve(basename + ".xlsx").toString
  }

  private def errorAndExit(message: String, status: Int = -1) = {
    println(message)
    exit(status)
  }
}

trait Console {
  def println(message: String): Unit = Console.err.println(message)
}

trait SystemTerminator {
  def exit(status: Int): Unit = System.exit(status)
}

trait DirectoryUtil {
  def makeDirRecursive(path: Path): Unit = path match {
    case p if Files.notExists(p.getParent) =>
      makeDirRecursive(p.getParent)
    case p if Files.notExists(p) =>
      Files.createDirectory(p)
    case _ =>
  }
}
