package jp.co.so_net.vinegar.generator

import java.nio.charset.Charset
import java.util.Date

import com.norbitltd.spoiwo.model._
import com.norbitltd.spoiwo.natures.xlsx.Model2XlsxConversions._
import jp.co.so_net.vinegar.model.{Given, Case, Scenario, Suite}

trait SheetGenerator {
  def generate(sheet: Sheet): Sheet
}

class ExcelGenerator(suite: Suite) {
  val generators = Seq(
    EmptyRowGenerator,
    new StoryGenerator(suite),
    new DescriptionGenerator(suite),
    new BackgroundGenerator(suite),
    new RemarkGenerator(suite),
    new EnvironmentGenerator(suite),
    EmptyRowGenerator,
    CaseHeaderGenerator,
    new ScenarioListGenerator(suite),
    EmptyRowGenerator
  )

  def writeFile(filename: String): Unit = generate.saveAsXlsx(filename)

  private def generate: Sheet = {
    generators.foldLeft(Sheet(name = "テスト仕様書")) { (sheet, generator) =>
      generator.generate(sheet)
    }
  }
}

object EmptyRowGenerator extends SheetGenerator {
  def generate(sheet: Sheet): Sheet = sheet.addRow(Row())
}

class StoryGenerator(suite: Suite) extends SheetGenerator {

  import DefinedStyles._

  def generate(sheet: Sheet): Sheet = {
    val row = Row(
      Cell(value = "対応ストーリー", index = 0, style = headerCellStyle),
      Cell(value = suite.name, index = 1, style = valueCellStyle)
    ).addCells(
      metaRange.map(i => Cell(value = "", index = i, style = valueCellStyle))
    )
    val rowIndex = sheet.rows.length
    sheet.addRow(row).addMergedRegion(CellRange(rowIndex -> rowIndex, 1 -> metaRange.last))
  }
}

class DescriptionGenerator(suite: Suite) extends SheetGenerator {

  import DefinedStyles._

  def generate(sheet: Sheet): Sheet = {
    val description = suite.description.getOrElse("")
    val row = Row(
      Cell(value = "目的／観点", index = 0, style = headerCellStyle),
      Cell(value = description, index = 1, style = valueCellStyle)
    ).addCells(
      metaRange.map(i => Cell(value = "", index = i, style = valueCellStyle))
    ).withHeight(description.split("\n").length.lines)
    val rowIndex = sheet.rows.length
    sheet.addRow(row).addMergedRegion(CellRange(rowIndex -> rowIndex, 1 -> metaRange.last))
  }
}

class BackgroundGenerator(suite: Suite) extends SheetGenerator {

  import DefinedStyles._

  def generate(sheet: Sheet): Sheet = {
    val background = suite.background.getOrElse("")
    val row = Row(
      Cell(value = "事前準備", index = 0, style = headerCellStyle),
      Cell(value = background, index = 1, style = valueCellStyle)
    ).addCells(
      metaRange.map(i => Cell(value = "", index = i, style = valueCellStyle))
    ).withHeight(background.split("\n").length.lines)
    val rowIndex = sheet.rows.length
    sheet.addRow(row).addMergedRegion(CellRange(rowIndex -> rowIndex, 1 -> metaRange.last))
  }
}

class RemarkGenerator(suite: Suite) extends SheetGenerator {

  import DefinedStyles._

  def generate(sheet: Sheet): Sheet = {
    val remark = suite.remark.getOrElse("")
    val row = Row(
      Cell(value = "備考", index = 0, style = headerCellStyle),
      Cell(value = remark, index = 1, style = valueCellStyle)
    ).addCells(
      metaRange.map(i => Cell(value = "", index = i, style = valueCellStyle))
    ).withHeight(remark.split("\n").length.lines)
    val rowIndex = sheet.rows.length
    sheet.addRow(row).addMergedRegion(CellRange(rowIndex -> rowIndex, 1 -> metaRange.last))
  }
}

class EnvironmentGenerator(suite: Suite) extends SheetGenerator {

  import DefinedStyles._

  val headerRowIndex = 1
  val systemRowIndex = 2
  val browserRowIndex = 3
  val environmentCellRange = Range(4, 7)

  def generate(sheet: Sheet): Sheet = {
    val environmentHeaderRow = sheet.rows(headerRowIndex)
      .addCell(Cell(value = "環境", index = environmentCellRange.head, style = headerCellStyle))
      .addCells(environmentCellRange.tail.map(i => Cell(value = "", index = i, style = valueCellStyle)))
    val systemRow = sheet.rows(systemRowIndex)
      .addCell(Cell(value = "OS / 端末", index = environmentCellRange.head, style = headerCellStyle))
      .addCells(environmentCellRange.tail.map(i => Cell(value = "", index = i, style = valueCellStyle)))
    val browserRow = sheet.rows(browserRowIndex)
      .addCell(Cell(value = "ブラウザ", index = environmentCellRange.head, style = headerCellStyle))
      .addCells(environmentCellRange.tail.map(i => Cell(value = "", index = i, style = valueCellStyle)))
    val rows = sheet.rows
      .updated(headerRowIndex, environmentHeaderRow)
      .updated(systemRowIndex, systemRow)
      .updated(browserRowIndex, browserRow)
    sheet
      .withRows(rows)
      .addMergedRegion(CellRange(headerRowIndex -> headerRowIndex, environmentCellRange.head -> environmentCellRange.last))
      .addMergedRegion(CellRange(systemRowIndex -> systemRowIndex, environmentCellRange.head + 1 -> environmentCellRange.last))
      .addMergedRegion(CellRange(browserRowIndex -> browserRowIndex, environmentCellRange.head + 1 -> environmentCellRange.last))
  }
}

class ColumnsGenerator(suite: Suite) extends SheetGenerator {

  import DefinedStyles._

  def generate(sheet: Sheet): Sheet = {
    val columns = List(
      Column(width = 11, style = centerStyle),
      Column(width = 50, style = wrappedStyle),
      Column(width = 50, style = wrappedStyle),
      Column(width = 70, style = wrappedStyle),
      Column(width = 8, style = centerStyle),
      Column(width = 8, style = centerStyle),
      Column(width = 8, style = centerStyle),
      Column(width = 80, style = wrappedStyle)
    )
    sheet.addColumns(columns)
  }
}

class ScenarioListGenerator(suite: Suite) extends SheetGenerator {
  def generate(sheet: Sheet): Sheet = {
    suite.scenarios
      .map(scenario => new ScenarioGenerator(scenario))
      .foldLeft(sheet)((sheet, generator) => generator.generate(sheet))
  }
}

class ScenarioGenerator(scenario: Scenario) extends SheetGenerator {

  import DefinedStyles._

  def generate(sheet: Sheet): Sheet = {
    val generators =
      ScenarioHeaderGenerator +:
        scenario.cases.map(c => new CaseGenerator(c))
    generators.foldLeft(sheet)((sheet, generator) => generator.generate(sheet))
  }

  object ScenarioHeaderGenerator extends SheetGenerator {
    def generate(sheet: Sheet): Sheet = {
      val values = Range(0, 8).map(_ => "")
        .updated(0, "シナリオ #" + scenario.id)
        .updated(1, scenario.name)
      val cells = values.zipWithIndex.map { case (value, index) =>
        Cell(value = value, index = index, style = scenarioNameCellStyle)
      }
      sheet.addRow(Row(cells.updated(7, cells.last.withStyle(scenarioNameLastCellStyle))))
    }
  }

}

object CaseHeaderGenerator extends SheetGenerator {

  import DefinedStyles._

  def generate(sheet: Sheet): Sheet = {
    val columns = List(
      Column(width = 11, style = centerStyle),
      Column(width = 50, style = wrappedStyle),
      Column(width = 50, style = wrappedStyle),
      Column(width = 70, style = wrappedStyle),
      Column(width = 8, style = centerStyle),
      Column(width = 8, style = centerStyle),
      Column(width = 8, style = centerStyle),
      Column(width = 80, style = wrappedStyle)
    )
    val row = Row(
      Cell(value = "テストID", style = headerCellStyle),
      Cell(value = "Given", style = headerCellStyle),
      Cell(value = "When", style = headerCellStyle),
      Cell(value = "Then", style = headerCellStyle),
      Cell(value = "結果", style = headerCellStyle),
      Cell(value = "確認者", style = headerCellStyle),
      Cell(value = "確認日", style = headerCellStyle),
      Cell(value = "備考", style = headerCellStyle)
    )
    sheet
      .withColumns(columns)
      .addRow(row)
  }
}

class CaseGenerator(given: Given) extends SheetGenerator {

  import DefinedStyles._

  val rowCount = given.children.foldLeft(0)((sum, when) => sum + when.children.length)
  val givenColumn = 1
  val whenColumn = 2
  val commentPrefix = "# "

  def generate(sheet: Sheet): Sheet = {
    val firstRowIndex = sheet.rows.length
    Seq(
      addCaseRows _,
      mergeGivenRows(firstRowIndex) _,
      mergeWhenRows(firstRowIndex) _
    ).foldLeft(sheet)((s, f) => f(s))
  }

  def generateCaseRows: Seq[Row] = {
    given.children.flatMap { w =>
      val firstWhen = w equals given.children.head
      w.children.map { t =>
        val firstThen = t equals w.children.head
        val givenText = if (firstWhen && firstThen) joinTextAndNote(given) else ""
        val whenText = if (firstThen) joinTextAndNote(w) else ""
        Row(
          Cell(value = t.id, style = caseIdCellStyle),
          Cell(value = givenText, style = caseCellStyle),
          Cell(value = whenText, style = caseCellStyle),
          Cell(value = t.text.getOrElse(""), style = caseCellStyle),
          Cell(value = "", style = centeredValueCellStyle),
          Cell(value = "", style = centeredValueCellStyle),
          Cell(value = "", style = dateValueCellStyle),
          Cell(value = "", style = noteCellStyle)
        ).withHeight(calcHeight(t.text).lines)
      }
    }
  }

  def calcHeight(text: Option[String]): Int = {
    val sjis = Charset.forName("SJIS")
    val maxLineLength = 78f
    val minLines = 3

    val toCharLength: PartialFunction[Char, Int] = {
      case c => c.toString.getBytes(sjis).length
    }

    val calcCharLength: (Int => Float) = l => (l - 1) * 0.73f + 1

    val lines = text.map(_.lines.map { line =>
      val lineLength = line.collect(toCharLength).map(calcCharLength).sum.ceil.toInt
      (lineLength / maxLineLength).ceil.toInt
    }.sum).getOrElse(2)

    Seq(lines, minLines).max
  }

  //  calcHeight(Some(str))

  def addCaseRows(sheet: Sheet): Sheet = {
    sheet.addRows(generateCaseRows)
  }

  def mergeGivenRows(firstRowIndex: Int)(sheet: Sheet): Sheet = {
    sheet.addMergedRegion(CellRange(firstRowIndex -> (firstRowIndex + rowCount - 1), givenColumn -> givenColumn))
  }

  def mergeWhenRows(firstRowIndex: Int)(sheet: Sheet): Sheet = {
    given.children.foldLeft((sheet, firstRowIndex)) { case ((s, rowIndex), when) =>
      when.children.length match {
        case thenCount if thenCount > 0 =>
          (s.addMergedRegion(CellRange(rowIndex -> (rowIndex + thenCount - 1), whenColumn -> whenColumn)),
            rowIndex + thenCount)
        case _ =>
          (s, rowIndex)
      }
    }._1
  }

  def joinTextAndNote(c: Case): String = {
    Seq(
      c.text,
      Some("\n\n"),
      c.note.map(_.linesWithSeparators.map(commentPrefix + _)).map(_.reduce(_ + _))
    ).map(_.getOrElse("")).reduce(_ + _).trim
  }
}
