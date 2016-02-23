package jp.co.so_net.vinegar.builder.excel

import com.norbitltd.spoiwo.model.{Row, Sheet}
import jp.co.so_net.vinegar.model.Suite

trait EmptyRowPart {
  self: ExcelGenerator =>

  def emptyRow(): This = new This(parts :+ emptyRowPart _)

  private def emptyRowPart(sheet: Sheet, suite: Suite): Sheet = sheet.addRow(Row())
}
