package jp.co.so_net.vinegar.builder.excel

import com.norbitltd.spoiwo.model._
import jp.co.so_net.vinegar.builder.DefinedStyles
import jp.co.so_net.vinegar.model.{Then, WhenGroup, Scenario, Suite}

import scala.annotation.tailrec

trait ScenariosPart {
  self: ExcelGenerator =>

  import DefinedStyles._
  import jp.co.so_net.vinegar.newLine

  def scenarios(): This = new This(parts :+ scenariosPart _)

  private def scenariosPart(sheet: Sheet, suite: Suite): Sheet = {
    suite.scenarios.zipWithIndex
      .foldLeft(columnedSheet(sheet, suite)) {
        case (s, (scenario, index)) =>
          scenario.groups.foldLeft(scenarioHeader(s, scenario, index + 1)) { (sheet, group) =>
            val givenText = group.steps.map(_.mkString).mkString(newLine * 2)
            hoge(sheet, Some(givenText), group.children)
          }
      }
  }

  def hoge(sheet: Sheet, givenText: Option[String], whenGroup: Seq[WhenGroup]): Sheet = {
    whenGroup match {
      case head :: tail =>
        val whenText = head.steps.map(_.mkString).mkString(newLine * 2)
        hoge(thenText(sheet, givenText, Some(whenText), head.children), None, tail)
      case Nil =>
        sheet
    }
  }

  @tailrec
  private def thenText(sheet: Sheet, givenText: Option[String], whenText: Option[String], thenSteps: Seq[Then]): Sheet = {
    thenSteps match {
      case head :: tail =>
        val nextSheet = sheet.addRow(Row(
          Cell(value = "id", style = caseIdCellStyle),
          Cell(value = givenText.getOrElse(""), style = caseCellStyle),
          Cell(value = whenText.getOrElse(""), style = caseCellStyle),
          Cell(value = head.mkString, style = caseCellStyle),
          Cell(value = "", style = centeredValueCellStyle),
          Cell(value = "", style = centeredValueCellStyle),
          Cell(value = "", style = dateValueCellStyle),
          Cell(value = head.comment.getOrElse(""), style = noteCellStyle),
          Cell(value = "erase", style = noteCellStyle)
        ))
        thenText(nextSheet, None, None, tail)
      case Nil =>
        sheet
    }
  }

  private def columnedSheet(sheet: Sheet, suite: Suite): Sheet = {
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
    sheet.addColumns(columns)
      .withColumns(columns)
      .addRow(row)
  }

  private def scenarioHeader(sheet: Sheet, scenario: Scenario, id: Int): Sheet = {
    val values = Range(0, 8).map(_ => "")
      .updated(0, "シナリオ #" + id)
      .updated(1, scenario.name)
    val cells = values.zipWithIndex.map { case (value, index) =>
      Cell(value = value, index = index, style = scenarioNameCellStyle)
    }
    sheet.addRow(Row(cells.updated(7, cells.last.withStyle(scenarioNameLastCellStyle))))
  }

  //class CaseGenerator(given: Given) extends SheetGenerator {
  //
  //  import DefinedStyles._
  //
  //  val rowCount = given.children.foldLeft(0)((sum, when) => sum + when.children.length)
  //  val givenColumn = 1
  //  val whenColumn = 2
  //  val commentPrefix = "# "
  //  val minLines = 3
  //
  //  def generate(sheet: Sheet): Sheet = {
  //    val firstRowIndex = sheet.rows.length
  //    Seq(
  //      addCaseRows _,
  //      mergeGivenRows(firstRowIndex) _,
  //      mergeWhenRows(firstRowIndex) _
  //    ).foldLeft(sheet)((s, f) => f(s))
  //  }
  //
  //  def generateCaseRows: Seq[Row] = given.children.flatMap { w =>
  //    val firstWhen = w equals given.children.head
  //    w.children.map { t =>
  //      val firstThen = t equals w.children.head
  //      val givenText = if (firstWhen && firstThen) joinTextAndNote(given) else ""
  //      val whenText = if (firstThen) joinTextAndNote(w) else ""
  //      val thenText = t.text.getOrElse("")
  //      val noteText = t.comment.getOrElse("")
  //      Row(
  //        Cell(value = t.id, style = caseIdCellStyle),
  //        Cell(value = givenText, style = caseCellStyle),
  //        Cell(value = whenText, style = caseCellStyle),
  //        Cell(value = thenText, style = caseCellStyle),
  //        Cell(value = "", style = centeredValueCellStyle),
  //        Cell(value = "", style = centeredValueCellStyle),
  //        Cell(value = "", style = dateValueCellStyle),
  //        Cell(value = noteText, style = noteCellStyle)
  //      ).withHeight(
  //        Seq(
  //          minLines,
  //          calcHeight(thenText),
  //          calcHeight(whenText),
  //          calcHeight(noteText)
  //        ).max.lines
  //      )
  //    }
  //  }
  //
  //  private def calcHeight(text: String): Int = {
  //    val sjis = Charset.forName("SJIS")
  //    val maxLineLength = 78f
  //
  //    val toCharLength: PartialFunction[Char, Int] = {
  //      case c => c.toString.getBytes(sjis).length
  //    }
  //
  //    val calcCharLength: (Int => Float) = l => (l - 1) * 0.73f + 1
  //
  //    text.lines.map { line =>
  //      val lineLength = line.collect(toCharLength).map(calcCharLength).sum.ceil.toInt
  //      (lineLength / maxLineLength).ceil.toInt
  //    }.sum
  //  }
  //
  //  def addCaseRows(sheet: Sheet): Sheet = {
  //    sheet.addRows(generateCaseRows)
  //  }
  //
  //  def mergeGivenRows(firstRowIndex: Int)(sheet: Sheet): Sheet = {
  //    sheet.addMergedRegion(CellRange(firstRowIndex -> (firstRowIndex + rowCount - 1), givenColumn -> givenColumn))
  //  }
  //
  //  def mergeWhenRows(firstRowIndex: Int)(sheet: Sheet): Sheet = {
  //    given.children.foldLeft((sheet, firstRowIndex)) { case ((s, rowIndex), when) =>
  //      when.children.length match {
  //        case thenCount if thenCount > 0 =>
  //          (s.addMergedRegion(CellRange(rowIndex -> (rowIndex + thenCount - 1), whenColumn -> whenColumn)),
  //            rowIndex + thenCount)
  //        case _ =>
  //          (s, rowIndex)
  //      }
  //    }._1
  //  }
  //
  //  def joinTextAndNote(c: Case): String = {
  //    Seq(
  //      c.text,
  //      Some("\n\n"),
  //      c.comment.map(_.linesWithSeparators.map(commentPrefix + _)).map(_.reduce(_ + _))
  //    ).map(_.getOrElse("")).reduce(_ + _).trim
  //  }


  //    val generators =
  //      ScenarioHeaderGenerator +:
  //        scenario.cases.map(c => new CaseGenerator(c))
  //    generators.foldLeft(sheet)((sheet, generator) => generator.generate(sheet))
  //  }
  //


  //class ScenarioListGenerator(suite: Suite) extends SheetGenerator {
  //  def generate(sheet: Sheet): Sheet = {
  //  }
  //}
  //
  //class ScenarioGenerator(scenario: Scenario) extends SheetGenerator {
  //
  //  import DefinedStyles._
  //
  //  def generate(sheet: Sheet): Sheet = {
  //    val generators =
  //      ScenarioHeaderGenerator +:
  //        scenario.cases.map(c => new CaseGenerator(c))
  //    generators.foldLeft(sheet)((sheet, generator) => generator.generate(sheet))
  //  }
  //
  //}
  //
  //object CaseHeaderGenerator extends SheetGenerator {
  //
  //  import DefinedStyles._
  //
  //  def generate(sheet: Sheet): Sheet = {
  //  }
  //}
  //
  //class CaseGenerator(given: Given) extends SheetGenerator {
  //
  //  import DefinedStyles._
  //
  //  val rowCount = given.children.foldLeft(0)((sum, when) => sum + when.children.length)
  //  val givenColumn = 1
  //  val whenColumn = 2
  //  val commentPrefix = "# "
  //  val minLines = 3
  //
  //  def generate(sheet: Sheet): Sheet = {
  //    val firstRowIndex = sheet.rows.length
  //    Seq(
  //      addCaseRows _,
  //      mergeGivenRows(firstRowIndex) _,
  //      mergeWhenRows(firstRowIndex) _
  //    ).foldLeft(sheet)((s, f) => f(s))
  //  }
  //
  //  def generateCaseRows: Seq[Row] = given.children.flatMap { w =>
  //    val firstWhen = w equals given.children.head
  //    w.children.map { t =>
  //      val firstThen = t equals w.children.head
  //      val givenText = if (firstWhen && firstThen) joinTextAndNote(given) else ""
  //      val whenText = if (firstThen) joinTextAndNote(w) else ""
  //      val thenText = t.text.getOrElse("")
  //      val noteText = t.comment.getOrElse("")
  //      Row(
  //        Cell(value = t.id, style = caseIdCellStyle),
  //        Cell(value = givenText, style = caseCellStyle),
  //        Cell(value = whenText, style = caseCellStyle),
  //        Cell(value = thenText, style = caseCellStyle),
  //        Cell(value = "", style = centeredValueCellStyle),
  //        Cell(value = "", style = centeredValueCellStyle),
  //        Cell(value = "", style = dateValueCellStyle),
  //        Cell(value = noteText, style = noteCellStyle)
  //      ).withHeight(
  //        Seq(
  //          minLines,
  //          calcHeight(thenText),
  //          calcHeight(whenText),
  //          calcHeight(noteText)
  //        ).max.lines
  //      )
  //    }
  //  }
  //
  //  private def calcHeight(text: String): Int = {
  //    val sjis = Charset.forName("SJIS")
  //    val maxLineLength = 78f
  //
  //    val toCharLength: PartialFunction[Char, Int] = {
  //      case c => c.toString.getBytes(sjis).length
  //    }
  //
  //    val calcCharLength: (Int => Float) = l => (l - 1) * 0.73f + 1
  //
  //    text.lines.map { line =>
  //      val lineLength = line.collect(toCharLength).map(calcCharLength).sum.ceil.toInt
  //      (lineLength / maxLineLength).ceil.toInt
  //    }.sum
  //  }
  //
  //  def addCaseRows(sheet: Sheet): Sheet = {
  //    sheet.addRows(generateCaseRows)
  //  }
  //
  //  def mergeGivenRows(firstRowIndex: Int)(sheet: Sheet): Sheet = {
  //    sheet.addMergedRegion(CellRange(firstRowIndex -> (firstRowIndex + rowCount - 1), givenColumn -> givenColumn))
  //  }
  //
  //  def mergeWhenRows(firstRowIndex: Int)(sheet: Sheet): Sheet = {
  //    given.children.foldLeft((sheet, firstRowIndex)) { case ((s, rowIndex), when) =>
  //      when.children.length match {
  //        case thenCount if thenCount > 0 =>
  //          (s.addMergedRegion(CellRange(rowIndex -> (rowIndex + thenCount - 1), whenColumn -> whenColumn)),
  //            rowIndex + thenCount)
  //        case _ =>
  //          (s, rowIndex)
  //      }
  //    }._1
  //  }
  //
  //  def joinTextAndNote(c: Case): String = {
  //    Seq(
  //      c.text,
  //      Some("\n\n"),
  //      c.comment.map(_.linesWithSeparators.map(commentPrefix + _)).map(_.reduce(_ + _))
  //    ).map(_.getOrElse("")).reduce(_ + _).trim
  //  }
  //}
}
