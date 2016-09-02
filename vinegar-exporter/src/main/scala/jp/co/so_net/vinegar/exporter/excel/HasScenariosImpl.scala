package jp.co.so_net.vinegar.exporter.excel

import com.norbitltd.spoiwo.model._
import jp.co.so_net.vinegar.exporter.HasScenarios
import jp.co.so_net.vinegar.models._
import jp.co.so_net.vinegar.utils.Environment

import scala.annotation.tailrec

private[excel]
trait HasScenariosImpl extends HasScenarios[Sheet] {

  import DefinedStyles._
  import HeightCalculator._
  import StepConversions._

  val givenColumnIndex = 1
  val whenColumnIndex = 2
  val defaultLineSize = 3

  def scenarios = (sheet: Sheet, feature: Feature) => {
    feature.scenarioDefinitions.zipWithIndex
      .foldLeft(columnedSheet(sheet, feature)) {
        case (s, (scenario, index)) =>
          val env = StepEnv(scenarioId = index + 1, stepId = 1)
          givenSteps(scenarioHeader(s, scenario, env.scenarioId), GWTTree(scenario.steps).children)(env)
      }
  }

  case class StepEnv(scenarioId: Int, stepId: Int) {
    val stepIdFormat = """%02d-%03d"""

    def mkId: String = stepIdFormat.format(scenarioId, stepId)

    def increaseStepId(n: Int): StepEnv = copy(stepId = stepId + n)
  }

  private def givenSteps(sheet: Sheet, givenGroups: Seq[GivenGroup])(env: StepEnv): Sheet = {
    givenGroups match {
      case head :: tail =>
        val givenText = head.steps.map(_.mkString).mkString(Environment.NEW_LINE * 2)
        val thenSteps = head.children.map(_.children.length).sum
        val nextSheet = whenSteps(sheet, Some(givenText), head.children)(env)
        givenSteps(
          mergeCells(nextSheet, givenColumnIndex, thenSteps),
          tail)(env.increaseStepId(thenSteps))
      case Nil =>
        sheet
    }
  }

  @tailrec
  private def whenSteps(sheet: Sheet,
                        givenText: Option[String],
                        whenGroup: Seq[WhenGroup])(env: StepEnv): Sheet = {
    whenGroup match {
      case head :: tail =>
        val whenText = head.steps.map(_.mkString).mkString(Environment.NEW_LINE * 2)
        val nextSheet = insertStepRow(sheet, givenText, Some(whenText), head.children)(env)
        whenSteps(
          mergeCells(nextSheet, whenColumnIndex, head.children.length),
          None, tail)(env.increaseStepId(head.children.length))
      case Nil =>
        sheet
    }
  }

  @tailrec
  private def insertStepRow(sheet: Sheet,
                            givenTextOption: Option[String],
                            whenTextOption: Option[String],
                            thenSteps: Seq[Then])(env: StepEnv): Sheet = {
    thenSteps match {
      case head :: tail =>
        val givenText = givenTextOption.getOrElse("")
        val whenText = whenTextOption.getOrElse("")
        val comment = head.comment.getOrElse("")
        val nextSheet = sheet.addRow(Row(
          Cell(value = env.mkId, style = caseIdCellStyle),
          Cell(value = givenText, style = caseCellStyle),
          Cell(value = whenText, style = caseCellStyle),
          Cell(value = head.mkString, style = caseCellStyle),
          Cell(value = "", style = centeredValueCellStyle),
          Cell(value = "", style = centeredValueCellStyle),
          Cell(value = "", style = dateValueCellStyle),
          Cell(value = comment, style = noteCellStyle)
        ).withHeight(
          Seq(
            defaultLineSize,
            calcHeight(givenText),
            calcHeight(whenText),
            calcHeight(comment)
          ).max.lines
        ))
        insertStepRow(nextSheet, None, None, tail)(env.increaseStepId(1))
      case Nil =>
        sheet
    }
  }

  private def columnedSheet(sheet: Sheet, feature: Feature): Sheet = {
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

  private def scenarioHeader(sheet: Sheet, scenario: ScenarioDefinition, id: Int): Sheet = {
    val values = Range(0, 8).map(_ => "")
      .updated(0, "シナリオ #" + id)
      .updated(1, scenario.name)
    val cells = values.zipWithIndex.map { case (value, index) =>
      Cell(value = value, index = index, style = scenarioNameCellStyle)
    }
    sheet.addRow(Row(cells.updated(7, cells.last.withStyle(scenarioNameLastCellStyle))))
  }

  private def mergeCells(sheet: Sheet, columnIndex: Int, rows: Int): Sheet = {
    val rowIndex = sheet.rows.length - rows
    sheet.addMergedRegion(CellRange(rowIndex -> (rowIndex + rows - 1), columnIndex -> columnIndex))
  }

}

private[excel]
object StepConversions {

  implicit class StepText(step: Step) {
    def mkString: String = (step match {
      case _: Then =>
        Seq(Some(step.text), step.argument.map(_.mkString))
      case _ =>
        Seq(Some(step.text), step.argument.map(_.mkString), step.comment.map(_.replaceAll("(?m)^", "# ")))
    }).flatten.mkString(Environment.NEW_LINE)
  }

  implicit class ArgumentText(argument: StepArgument) {
    def mkString: String = argument match {
      case dt: DataTable => ""
      case ds: DocString => ds.text
      case ex: Example => ""
    }
  }

}