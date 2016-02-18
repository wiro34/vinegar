package jp.co.so_net.vinegar.builder.excel

import com.norbitltd.spoiwo.model.{Cell, CellRange, Row, Sheet}
import jp.co.so_net.vinegar.builder.DefinedStyles
import jp.co.so_net.vinegar.model.Suite

trait BackgroundPart {
  self: ExcelGenerator =>

  import DefinedStyles._
  import jp.co.so_net.vinegar.newLine

  def background(): This = new This(parts :+ backgroundPart _)

  private def backgroundPart(sheet: Sheet, suite: Suite): Sheet = {
    val backgroundText = suite.background match {
      case Some(background) =>
        background.groups.map(_.steps.map(_.text).mkString(newLine)).mkString
      case None =>
        ""
    }
    val row = Row(
      Cell(value = "事前準備", index = 0, style = headerCellStyle),
      Cell(value = backgroundText, index = 1, style = valueCellStyle)
    ).addCells(
      metaRange.map(i => Cell(value = "", index = i, style = valueCellStyle))
    ).withHeight(backgroundText.split("\n").length.lines)
    val rowIndex = sheet.rows.length
    sheet.addRow(row).addMergedRegion(CellRange(rowIndex -> rowIndex, 1 -> metaRange.last))
  }

}
