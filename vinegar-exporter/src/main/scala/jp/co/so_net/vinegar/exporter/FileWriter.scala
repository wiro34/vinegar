package jp.co.so_net.vinegar.exporter

import jp.co.so_net.vinegar.exporter.excel.MixinExcelFileWriter

import scala.util.Try

trait FileWriter[T] {
  def write(filename: String, t: T): Try[Unit]
}

object FileWriters extends MixinExcelFileWriter {}
