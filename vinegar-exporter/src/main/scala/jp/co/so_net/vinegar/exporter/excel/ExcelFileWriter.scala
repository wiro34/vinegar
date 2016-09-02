package jp.co.so_net.vinegar.exporter.excel

import com.norbitltd.spoiwo.model.Sheet
import jp.co.so_net.vinegar.exporter.FileWriter

import scala.util.Try

class ExcelFileWriter extends FileWriter[Sheet] {

  import com.norbitltd.spoiwo.natures.xlsx.Model2XlsxConversions._

  def write(filename: String, sheet: Sheet): Try[Unit] = Try(sheet.saveAsXlsx(filename))
}

trait UsesExcelFileWriter {
  val ExcelFileWriter: ExcelFileWriter
}

trait MixinExcelFileWriter extends UsesExcelFileWriter {
  val ExcelFileWriter = new ExcelFileWriter
}

