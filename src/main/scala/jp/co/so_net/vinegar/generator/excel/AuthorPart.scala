package jp.co.so_net.vinegar.generator.excel

import jp.co.so_net.vinegar.facade.exceljs.{Alignment, Worksheet}
import jp.co.so_net.vinegar.facade.gherkin3.Feature

object AuthorPart extends ExcelPart {

  import StyleDefinition._

  def fill(sheet: Worksheet, feature: Feature): Worksheet = {
    val hrow = sheet.getRow(2)
    hrow.getCell(7).value = "作成者"
    hrow.getCell(7).fill = headerFill
    hrow.getCell(7).border = headerBorder
    hrow.getCell(7).alignment = Alignment(horizontal = "center", vertical = "middle")

    val drow = sheet.getRow(3)
    drow.getCell(7).value = ""
    drow.getCell(7).border = headerBorder
    drow.getCell(7).alignment = Alignment(horizontal = "center", vertical = "middle")
    sheet
  }
}
