package jp.co.so_net.vinegar

import java.io.{FileNotFoundException, File}
import java.nio.file.{Files, Path, Paths}

import generator.ExcelGenerator

object VinegarMain extends App {
  val name = "vinegar"

  VinegarOptionParser.parse(args, VinegarOption()) match {
    case Some(config) =>
      try {
        generateExcel(config)
      } catch {
        case e: Throwable => errorAndExit(e.toString)
      }
    case None => // arguments are bad, error message will have been displayed
  }

  def generateExcel(config: VinegarOption) = {
    val path = Paths.get(config.file.getAbsolutePath)
    try {
      val gherkin = loadFile(path.toFile)
      VinegarDto.parse(gherkin) match {
        case Right(suite) =>
          if (config.force)
            makeDirRecursive(path.getParent)
          new ExcelGenerator(suite).writeFile(generateOutputFilename(path))
        case Left(e) =>
          errorAndExit(e.getMessage)
      }
    } catch {
      case e: FileNotFoundException => errorAndExit(path + ": No such file or directory")
    }
  }

  def loadFile(file: File) = io.Source.fromFile(file).mkString

  def generateOutputFilename(path: Path): String = {
    val filename = path.getFileName.toString
    val basename = filename.lastIndexOf('.') match {
      case i if i >= 0 => filename.substring(0, i)
      case _ => filename
    }
    path.getParent.resolve(basename + ".xlsx").toString
  }

  private def makeDirRecursive(path: Path): Unit = path match {
    case p if Files.notExists(p.getParent) =>
      makeDirRecursive(p.getParent)
    case p if Files.notExists(p) =>
      Files.createDirectory(p)
    case _ =>
    // nothing to do
  }

  def errorAndExit(message: String, status: Int = -1) = {
    System.err.println(message)
    System.exit(status)
  }
}
