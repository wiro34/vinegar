package jp.co.so_net.vinegar.generator

import com.norbitltd.spoiwo.model._
import com.norbitltd.spoiwo.natures.xlsx.Model2XlsxConversions._
import jp.co.so_net.vinegar.model.{Case, Scenario, Suite}

trait SheetGenerator {
  def generate(sheet: Sheet): Sheet
}

class ExcelGenerator(suite: Suite) {
  val generators = Seq(
    EmptyRowGenerator,
    new StoryGenerator(suite),
    new DescriptionGenerator(suite),
    new BackgroundGenerator(suite),
    EmptyRowGenerator,
    CaseHeaderGenerator,
    new ScenarioListGenerator(suite),
    new ColumnsGenerator(suite),
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
    ).withHeight(description.split("\n").length * lineHeight)
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
    ).withHeight(background.split("\n").length * lineHeight)
    val rowIndex = sheet.rows.length
    sheet.addRow(row).addMergedRegion(CellRange(rowIndex -> rowIndex, 1 -> metaRange.last))
  }
}

class ColumnsGenerator(suite: Suite) extends SheetGenerator {

  import DefinedStyles._

  def generate(sheet: Sheet): Sheet = {
    val columns = List(
      Column(index = 0, width = 13, style = centerStyle),
      Column(index = 1, width = 40, style = wrappedStyle),
      Column(index = 2, width = 50, style = wrappedStyle),
      Column(index = 3, width = 50, style = wrappedStyle),
      Column(index = 4, width = 10, style = centerStyle),
      Column(index = 5, width = 10, style = centerStyle),
      Column(index = 6, width = 10, style = centerStyle),
      Column(index = 7, width = 10, style = centerStyle),
      Column(index = 8, width = 10, style = centerStyle),
      Column(index = 9, width = 80, style = wrappedStyle)
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
      val row = Row(
        Cell(value = "シナリオ #" + scenario.id, index = 0, style = scenarioHeaderStyle),
        Cell(value = scenario.name, index = 1, style = scenarioNameCellStyle)
      )
      val rowIndex = sheet.rows.length
      sheet.addRow(row).addMergedRegion(CellRange(rowIndex -> rowIndex, 1 -> 9))
    }
  }

}

object CaseHeaderGenerator extends SheetGenerator {

  import DefinedStyles._

  def generate(sheet: Sheet): Sheet = {
    val row = Row(
      Cell(value = "テストID", index = 0, style = headerCellStyle),
      Cell(value = "Given", index = 1, style = headerCellStyle),
      Cell(value = "When", index = 2, style = headerCellStyle),
      Cell(value = "Then", index = 3, style = headerCellStyle),
      Cell(value = "結果", index = 4, style = headerCellStyle),
      Cell(value = "確認者", index = 5, style = headerCellStyle),
      Cell(value = "確認日", index = 6, style = headerCellStyle),
      Cell(value = "OS / 端末", index = 7, style = headerCellStyle),
      Cell(value = "ブラウザ", index = 8, style = headerCellStyle),
      Cell(value = "備考", index = 9, style = headerCellStyle)
    )
    sheet.addRow(row)
  }
}

class CaseGenerator(c: Case) extends SheetGenerator {

  import DefinedStyles._

  def generate(sheet: Sheet): Sheet = {
    val row = Row(
      Cell(value = c.id, index = 0, style = caseIdCellStyle),
      Cell(value = c.given.getOrElse(""), index = 1, style = caseCellStyle),
      Cell(value = c.when.getOrElse(""), index = 2, style = caseCellStyle),
      Cell(value = c.then.getOrElse(""), index = 3, style = caseCellStyle),
      Cell(value = "", index = 4, style = caseCellStyle),
      Cell(value = "", index = 5, style = caseCellStyle),
      Cell(value = "", index = 6, style = caseCellStyle),
      Cell(value = "", index = 7, style = caseCellStyle),
      Cell(value = "", index = 8, style = caseCellStyle),
      Cell(value = c.note.getOrElse(""), index = 9, style = noteCellStyle)
    )
    sheet.addRow(row)
  }
}


