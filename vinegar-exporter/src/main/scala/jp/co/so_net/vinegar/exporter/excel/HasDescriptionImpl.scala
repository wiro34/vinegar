package jp.co.so_net.vinegar.exporter.excel

import com.norbitltd.spoiwo.model.{Cell, CellRange, Row, Sheet}
import jp.co.so_net.vinegar.exporter.HasDescription
import jp.co.so_net.vinegar.models.Feature

private[excel]
trait HasDescriptionImpl extends HasDescription[Sheet] {

  import DefinedStyles._

  def description = (sheet: Sheet, feature: Feature) => {
    val description = feature.description.getOrElse("")
    val row = Row(
      Cell(value = "目的／観点", index = 0, style = headerCellStyle),
      Cell(value = description, index = 1, style = valueCellStyle)
    ).addCells(
      metaRange.map(i => Cell(value = "", index = i, style = valueCellStyle))
    ).withHeight(description.split("[\r\n]+").length.lines)
    val rowIndex = sheet.rows.length
    sheet.addRow(row).addMergedRegion(CellRange(rowIndex -> rowIndex, 1 -> metaRange.last))
  }

}
