package jp.co.so_net.vinegar.exporter.excel

import com.norbitltd.spoiwo.model.{Cell, CellRange, Row, Sheet}
import jp.co.so_net.vinegar.exporter.HasBackground
import jp.co.so_net.vinegar.models._

private[excel]
trait HasBackgroundImpl extends HasBackground[Sheet] {
  val headerText = "事前準備"

  import DefinedStyles._
  import jp.co.so_net.vinegar.utils.Environment

  def background = (sheet: Sheet, feature: Feature) => {
    val backgroundText = feature.background match {
      case Some(background) => background.steps.map(_.text).mkString(Environment.NEW_LINE)
      case None => ""
    }
    val row = Row(
      Cell(value = headerText, index = 0, style = headerCellStyle),
      Cell(value = backgroundText, index = 1, style = valueCellStyle)
    ).addCells(
      metaRange.map(i => Cell(value = "", index = i, style = valueCellStyle))
    ).withHeight(backgroundText.split(Environment.NEW_LINE).length.lines)
    val rowIndex = sheet.rows.length
    sheet.addRow(row).addMergedRegion(CellRange(rowIndex -> rowIndex, 1 -> metaRange.last))
  }

}
