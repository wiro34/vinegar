package jp.co.so_net.vinegar.generator

import jp.co.so_net.vinegar.facade.Require
import jp.co.so_net.vinegar.facade.exceljs._
import jp.co.so_net.vinegar.facade.gherkin3.{Feature, Gherkin, Parser}
import jp.co.so_net.vinegar.facade.path.Path
import jp.co.so_net.vinegar.generator.excel._

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{global => g}

class ParserError(message: String) extends RuntimeException(message)

class ExcelGenerator(feature: Feature) {
  val path = Require[Path]("path")

  private val parts = Seq(
    EmptyRowPart,
    StoryPart,
    DescriptionPart,
    BackgroundPart,
    // CreateDatePart,
    // AuthorPart,
    EmptyRowPart,
    ScenarioPart
  )

  def generate(filename: String) = {
    val workbook = ExcelJS.newInstance[Workbook](g.exceljs.Workbook)
    val sheet = workbook.addWorksheet("テスト仕様書")
    ColumnDefinition.defineColumns(sheet)
    parts.foldLeft(sheet) { (sheet, p) => p.fill(sheet, feature) }
    workbook.xlsx.writeFile(filename)
  }
}

object ExcelGenerator {
  def parse(gherkin: String): ExcelGenerator = {
    try {
      val parser = Gherkin.newInstance[Parser](g.gherkin.Parser)
      val feature = parser.parse(gherkin)
      new ExcelGenerator(feature)
    } catch {
      case e: js.JavaScriptException =>
      println("hoge")
        throw new ParserError(e.toString)
    }
  }
}
