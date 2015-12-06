package jp.co.so_net.vinegar.generator.excel

import jp.co.so_net.vinegar.facade.exceljs._
import jp.co.so_net.vinegar.facade.gherkin3.Feature

object DescriptionPart extends ExcelPart {

  import StyleDefinition._

  val INDENT_PATTERN = """(?m)^\s{2}""".r
  val NEWLINE_PATTERN = """(?m)^""".r

  val rowIndex = 3

  def fill(sheet: Worksheet, feature: Feature): Worksheet = {
    sheet.mergeCells(s"B${rowIndex}:D${rowIndex}")
    val row = sheet.getRow(rowIndex)
    row.getCell(1).value = "目的／観点"
    row.getCell(1).fill = headerFill
    row.getCell(1).border = headerBorder
    row.getCell(1).alignment = Alignment(vertical = "middle")
    row.getCell(2).border = headerBorder
    row.getCell(2).value = INDENT_PATTERN.replaceAllIn(feature.description, "")
    row.getCell(2).alignment = Alignment(vertical = "middle", wrapText = true)
    row.height = NEWLINE_PATTERN.findAllIn(feature.description).length * lineHeight
    sheet
  }
}
