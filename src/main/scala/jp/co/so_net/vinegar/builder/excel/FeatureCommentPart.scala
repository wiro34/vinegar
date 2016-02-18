package jp.co.so_net.vinegar.builder.excel

import com.norbitltd.spoiwo.model.{Cell, CellRange, Row, Sheet}
import jp.co.so_net.vinegar.builder.DefinedStyles
import jp.co.so_net.vinegar.model.Suite

trait FeatureCommentPart {
  self: ExcelGenerator =>

  import DefinedStyles._

  def featureComment(): This = new This(parts :+ featureCommentPart _)

  private def featureCommentPart(sheet: Sheet, suite: Suite): Sheet = {
    val remark = suite.comment.getOrElse("")
    val row = Row(
      Cell(value = "備考", index = 0, style = headerCellStyle),
      Cell(value = remark, index = 1, style = valueCellStyle)
    ).addCells(
      metaRange.map(i => Cell(value = "", index = i, style = valueCellStyle))
    ).withHeight(remark.split("\n").length.lines)
    val rowIndex = sheet.rows.length
    sheet.addRow(row).addMergedRegion(CellRange(rowIndex -> rowIndex, 1 -> metaRange.last))
  }

}
