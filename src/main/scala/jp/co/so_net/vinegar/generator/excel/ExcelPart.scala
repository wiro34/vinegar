package jp.co.so_net.vinegar.generator.excel

import jp.co.so_net.vinegar.facade.exceljs.Worksheet
import jp.co.so_net.vinegar.facade.gherkin3.Feature

trait ExcelPart {
  def fill(sheet: Worksheet, feature: Feature): Worksheet
}
