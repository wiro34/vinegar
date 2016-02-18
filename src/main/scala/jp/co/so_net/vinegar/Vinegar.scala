package jp.co.so_net.vinegar

import java.nio.file.{Files, Path, Paths}

import jp.co.so_net.vinegar.builder.Builder
import jp.co.so_net.vinegar.model.Suite
import jp.co.so_net.vinegar.parser.GherkinParser

object Vinegar extends App {
  val name = "vinegar"

  VinegarOptionParser.parse(args, VinegarOption()) match {
    case Some(config) => new Vinegar(config, Builder.ExcelGenerator, new TextFileReader, new ExcelFileWriter).run
    case None => // arguments are bad, error message will have been displayed
  }
}

class Vinegar[T](config: VinegarOption,
                 builder: Builder[T],
                 reader: FileReader,
                 writer: FileWriter[T]) extends Console with SystemTerminator with DirectoryUtil {

  val inputFilepath = Paths.get(config.file.getAbsolutePath)

  def run() = {
    try {
      generateExcel()
    } catch {
      case e: Throwable =>
        errorAndExit("Error: " + e.getMessage)
    }
  }

  private def generateExcel() = {
    GherkinParser.parse(reader.read(inputFilepath.toFile)) match {
      case Right(suite) =>
        writeExcelFile(suite)
      case Left(e) =>
        errorAndExit(e.getMessage)
    }
  }

  private def writeExcelFile(suite: Suite) = {
    val outputFilepath = Paths.get(generateOutputFilename(inputFilepath))
    if (config.force)
      makeDirRecursive(outputFilepath.getParent)
    val sheet = builder.build(suite)
    writer.write(outputFilepath.toString, sheet)
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
    case _ => // nothing to do
  }
}
