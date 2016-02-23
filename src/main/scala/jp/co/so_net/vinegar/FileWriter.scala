package jp.co.so_net.vinegar

import com.norbitltd.spoiwo.model.Sheet

trait FileWriter[T] {
  def write(filename: String, t: T): Unit
}

class ExcelFileWriter extends FileWriter[Sheet] {

  import com.norbitltd.spoiwo.natures.xlsx.Model2XlsxConversions._

  def write(filename: String, sheet: Sheet): Unit = sheet.saveAsXlsx(filename)
}
