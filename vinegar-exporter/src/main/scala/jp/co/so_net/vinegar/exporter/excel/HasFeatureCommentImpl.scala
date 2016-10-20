package jp.co.so_net.vinegar.exporter.excel

import com.norbitltd.spoiwo.model.{Cell, CellRange, Row, Sheet}
import jp.co.so_net.vinegar.exporter.HasFeatureComment
import jp.co.so_net.vinegar.models.Feature
import jp.co.so_net.vinegar.utils.Environment

private[excel]
trait HasFeatureCommentImpl extends HasFeatureComment[Sheet] {

  import DefinedStyles._

  def featureComment = (sheet: Sheet, feature: Feature) => {
    val remark = feature.comment.getOrElse("")
    val row = Row(
      Cell(value = "備考", index = 0, style = headerCellStyle),
      Cell(value = remark, index = 1, style = valueCellStyle)
    ).addCells(
      metaRange.map(i => Cell(value = "", index = i, style = valueCellStyle))
    ).withHeight(remark.split(Environment.NEW_LINE).length.lines)
    val rowIndex = sheet.rows.length
    sheet.addRow(row).addMergedRegion(CellRange(rowIndex -> rowIndex, 1 -> metaRange.last))
  }

}
