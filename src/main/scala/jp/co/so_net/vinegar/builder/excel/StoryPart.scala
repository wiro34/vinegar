package jp.co.so_net.vinegar.builder.excel

import com.norbitltd.spoiwo.model.{Cell, CellRange, Row, Sheet}
import jp.co.so_net.vinegar.model.Suite

trait StoryPart {
  self: ExcelGenerator =>

  import DefinedStyles._

  def story(): This = new This(parts :+ storyPart _)

  private def storyPart(sheet: Sheet, suite: Suite): Sheet = {
    val row = Row(
      Cell(value = "対応ストーリー", index = 0, style = headerCellStyle),
      Cell(value = suite.name, index = 1, style = valueCellStyle)
    ).addCells(
      metaRange.map(i => Cell(value = "", index = i, style = valueCellStyle))
    )
    val rowIndex = sheet.rows.length
    sheet.addRow(row).addMergedRegion(CellRange(rowIndex -> rowIndex, 1 -> metaRange.last))
  }
}
