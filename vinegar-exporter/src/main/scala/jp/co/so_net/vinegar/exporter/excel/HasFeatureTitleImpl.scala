package jp.co.so_net.vinegar.exporter.excel

import com.norbitltd.spoiwo.model.{Cell, CellRange, Row, Sheet}
import jp.co.so_net.vinegar.exporter.HasFeatureTitle
import jp.co.so_net.vinegar.models.Feature

private[excel]
trait HasFeatureTitleImpl extends HasFeatureTitle[Sheet] {

  import DefinedStyles._

  def featureTitle = (sheet: Sheet, feature: Feature) => {
    val row = Row(
      Cell(value = "対応ストーリー", index = 0, style = headerCellStyle),
      Cell(value = feature.name, index = 1, style = valueCellStyle)
    ).addCells(
      metaRange.map(i => Cell(value = "", index = i, style = valueCellStyle))
    )
    val rowIndex = sheet.rows.length
    sheet.addRow(row).addMergedRegion(CellRange(rowIndex -> rowIndex, 1 -> metaRange.last))
  }
}
