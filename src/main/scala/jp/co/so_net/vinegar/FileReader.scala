package jp.co.so_net.vinegar

import java.io.File

trait FileReader {
  def read(file: File): String
}

class TextFileReader extends FileReader {
  def read(file: File): String = io.Source.fromFile(file).mkString
}
