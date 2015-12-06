package jp.co.so_net.vinegar.generator.excel

import jp.co.so_net.vinegar.facade.Require
import jp.co.so_net.vinegar.facade.exceljs.{Alignment, Worksheet}
import jp.co.so_net.vinegar.facade.gherkin3.Feature
import jp.co.so_net.vinegar.facade.moment.Moment

object CreateDatePart extends ExcelPart {

  import StyleDefinition._

  val moment = Require[Moment]("moment")

  def fill(sheet: Worksheet, feature: Feature): Worksheet = {
    val hrow = sheet.getRow(2)
    hrow.getCell(6).value = "作成日"
    hrow.getCell(6).fill = headerFill
    hrow.getCell(6).border = headerBorder
    hrow.getCell(6).alignment = Alignment(horizontal = "center", vertical = "middle")

    val drow = sheet.getRow(3)
    drow.getCell(6).value = moment().format("YYYY/MM/DD")
    drow.getCell(6).border = headerBorder
    drow.getCell(6).alignment = Alignment(horizontal = "center", vertical = "middle")

    sheet.getCell("B2").value = feature.name
    sheet
  }
}
