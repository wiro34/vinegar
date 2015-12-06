package jp.co.so_net.vinegar.generator.excel

import jp.co.so_net.vinegar.facade.exceljs._
import jp.co.so_net.vinegar.facade.gherkin3.Feature

object BackgroundPart extends ExcelPart {

  import StyleDefinition._

  val NEWLINE_PATTERN = """(?m)^""".r

  val rowIndex = 4

  def fill(sheet: Worksheet, feature: Feature): Worksheet = {
    sheet.mergeCells(s"B${rowIndex}:D${rowIndex}")
    val row = sheet.getRow(rowIndex)
    row.getCell(1).value = "事前準備"
    row.getCell(1).fill = headerFill
    row.getCell(1).border = headerBorder
    row.getCell(1).alignment = Alignment(vertical = "middle")

    val value = feature.background.steps.map(step => step.text).join("\n")
    row.getCell(2).value = value
    row.getCell(2).border = headerBorder
    row.getCell(2).alignment = Alignment(vertical = "middle", wrapText = true)
    row.height = NEWLINE_PATTERN.findAllIn(value).length * lineHeight
    sheet
  }
}
