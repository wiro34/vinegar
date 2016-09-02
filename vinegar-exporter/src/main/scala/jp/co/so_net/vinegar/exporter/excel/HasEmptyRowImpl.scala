package jp.co.so_net.vinegar.exporter.excel

import com.norbitltd.spoiwo.model.{Row, Sheet}
import jp.co.so_net.vinegar.exporter.HasEmptyRow
import jp.co.so_net.vinegar.models.Feature

private[excel]
trait HasEmptyRowImpl extends HasEmptyRow[Sheet] {
  def emptyRow = (sheet: Sheet, feature: Feature) => sheet.addRow(Row())
}
