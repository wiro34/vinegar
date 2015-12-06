package jp.co.so_net.vinegar.generator.excel

import jp.co.so_net.vinegar.facade.exceljs._
import jp.co.so_net.vinegar.facade.gherkin3.Feature

object StoryPart extends ExcelPart {

  import StyleDefinition._

  val rowIndex = 2

  def fill(sheet: Worksheet, feature: Feature): Worksheet = {
    sheet.mergeCells(s"B${rowIndex}:D${rowIndex}")
    val row = sheet.getRow(rowIndex)
    row.getCell(1).value = "対応ストーリー"
    row.getCell(1).fill = headerFill
    row.getCell(1).border = headerBorder
    row.getCell(2).value = ""
    row.getCell(2).border = headerBorder
    sheet.getCell(s"B${rowIndex}").value = feature.name
    sheet
  }
}
