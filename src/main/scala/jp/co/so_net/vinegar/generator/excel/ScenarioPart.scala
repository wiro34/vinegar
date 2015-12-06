package jp.co.so_net.vinegar.generator.excel

import jp.co.so_net.vinegar.facade.exceljs._
import jp.co.so_net.vinegar.facade.gherkin3.{Feature, ScenarioDefinition, Step}
import jp.co.so_net.vinegar.generator.excel.StyleDefinition._

import scala.scalajs.js

object ScenarioPart extends ExcelPart {
  def fill(sheet: Worksheet, feature: Feature): Worksheet = {
    for ((scenario, i) <- feature.scenarioDefinitions.zipWithIndex) {
      val scenarioId = i + 1
      new HeaderPart(scenario, feature).fill(sheet)
      new StepsPart(scenarioId, scenario, feature).fill(sheet)
    }
    sheet
  }

  class HeaderPart(scenario: ScenarioDefinition, feature: Feature) {
    def fill(sheet: Worksheet): Worksheet = {
      this.fillScenarioName(sheet)
      this.fillStepHeader(sheet)
    }

    private def fillScenarioName(sheet: Worksheet): Worksheet = {
      val scenarioRow = sheet.addRow(js.Array[String]())
      scenarioRow.getCell(1).value = "シナリオ"
      scenarioRow.getCell(1).fill = headerFill
      scenarioRow.getCell(1).border = headerBorder
      scenarioRow.getCell(1).alignment = Alignment(horizontal = "center")

      val rowNumber = scenarioRow._number
      sheet.mergeCells(s"B${rowNumber}:J${rowNumber}")
      scenarioRow.getCell(2).value = scenario.name
      scenarioRow.getCell(2).border = headerBorder

      sheet
    }

    private def fillStepHeader(sheet: Worksheet): Worksheet = {
      sheet.addRow(ColumnDefinition(
        id = "テストID",
        given = "前提",
        when = "確認手順",
        expected = "確認項目",
        result = "結果",
        who = "確認者",
        date = "確認日",
        device = "OS／端末",
        browser = "ブラウザ",
        note = "備考"
      )).eachCell((cell: Cell) => {
        cell.alignment = Alignment(horizontal = "center")
        cell.fill = headerFill
        cell.border = headerBorder
      })
      sheet
    }
  }

  class StepsPart(scenarioId: Int, scenario: ScenarioDefinition, feature: Feature) {
    val Given = """(Given )""".r
    val When = """(When )""".r
    val Then = """(Then )""".r
    val commentStripPattern = """[ \t\x0B\f\r]*#""".r

    def fill(sheet: Worksheet): Worksheet = {
      fillEachStep(sheet, scenario.steps, scenarioId)
      sheet
    }

    private def fillEachStep(sheet: Worksheet, steps: Seq[Step], scenarioId: Int): Unit = {
      def inner(steps: Seq[Step], row: Option[Row] = None, index: Int = 0): Unit = {
        steps.headOption match {
          case Some(step) =>
            val stepId = index + (if (row.isEmpty) 1 else 0)
            val r = row.getOrElse(newRow(sheet, scenarioId, stepId))
            this.getCellByKeyword(r, step.keyword) match {
              case Some((cell, nextRow)) =>
                cell.value = (cell.value + "\n" + step.text).trim
                step.argument.foreach { (argument) =>
                  cell.value = cell.value + "\n" + argument.content
                }
                this.fillComment(sheet, r, step, feature)
                inner(steps.tail, nextRow, stepId)
              case None =>
                inner(steps.tail, Some(r), stepId)
            }

          case None =>
            val row = sheet.addRow(ColumnDefinition())
            val border = CellBorder(top = BorderStyle(style = "thin", color = Color("FF000000")))
            row.eachCell((cell: Cell) => cell.border = border)
        }
      }
      inner(steps)
    }

    private def newRow(sheet: Worksheet, scenarioId: Int, caseId: Int): Row = {
      val row = sheet.addRow(ColumnDefinition(
        id = this.makeTestId(scenarioId, caseId),
        given = "",
        when = "",
        expected = ""
      ))
      row.eachCell((cell: Cell) => cell.border = stepBorder)
      row
    }

    private def makeTestId(upperId: Int, stepId: Int): String = {
      """%03d%04d""".format(upperId, stepId)
    }

    private def getCellByKeyword(row: Row, keyword: String): Option[(Cell, Option[Row])] = keyword match {
      case Given(_) => Some(row.getCell("given"), Some(row))
      case When(_) => Some(row.getCell("when"), Some(row))
      case Then(_) => Some(row.getCell("expected"), None)
      case x =>
        System.err.println(s"[WARN] Unknown keyword: ${x}")
        None
    }

    private def fillComment(sheet: Worksheet, row: Row, step: Step, feature: Feature) = {
      val range = Range(step.location.line, nextStepLine(step))
      val comments = feature.comments
        .filter(c => range.contains(c.location.line))
        .sortBy(c => c.location.line)
        .map { c => c.text }
        .join("\n")

      val cell = row.getCell("note")
      cell.value = commentStripPattern.replaceAllIn(comments, "")
    }

    private def nextStepLine(step: Step): Int = {
      scenario.steps.zipWithIndex.find { case (s, i) => s.equals(step) } match {
        case Some((s, i)) if i < scenario.steps.length - 1 =>
          scenario.steps(i + 1).location.line
        case Some(_) =>
          this.nextScenarioLine
        case None =>
          this.nextScenarioLine
      }
    }

    private def nextScenarioLine: Int = {
      feature.scenarioDefinitions.zipWithIndex.find { case (s, i) => s.equals(scenario) } match {
        case Some((s, i)) if i < feature.scenarioDefinitions.length - 1 =>
          feature.scenarioDefinitions(i + 1).location.line
        case Some((s, i)) =>
          9999 // FIXIT
        case None =>
          System.err.println("Error")
          9999
      }
    }
  }

}