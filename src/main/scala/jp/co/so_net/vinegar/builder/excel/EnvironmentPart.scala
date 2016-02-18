package jp.co.so_net.vinegar.builder.excel

import com.norbitltd.spoiwo.model.{Cell, CellRange, Row, Sheet}
import jp.co.so_net.vinegar.builder.DefinedStyles
import jp.co.so_net.vinegar.model.Suite

trait EnvironmentPart {
  self: ExcelGenerator =>

  import DefinedStyles._

  val headerRowIndex = 1
  val systemRowIndex = 2
  val browserRowIndex = 3
  val environmentCellRange = Range(4, 7)

  def environment(): This = new This(parts :+ environmentPart _)

  private def environmentPart(sheet: Sheet, suite: Suite): Sheet = {
    val environmentHeaderRow = sheet.rows(headerRowIndex)
      .addCell(Cell(value = "環境", index = environmentCellRange.head, style = headerCellStyle))
      .addCells(environmentCellRange.tail.map(i => Cell(value = "", index = i, style = valueCellStyle)))
    val systemRow = sheet.rows(systemRowIndex)
      .addCell(Cell(value = "OS／端末", index = environmentCellRange.head, style = headerCellStyle))
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
