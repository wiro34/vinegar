package jp.co.so_net.vinegar.exporter

import java.nio.charset.Charset

import jp.co.so_net.vinegar.utils.Environment

object StringUtils {
  def appearanceLength(s: String): Int = s.getBytes(Charset.forName("Shift-JIS")).length

  def paddingRight(s: String, c: Char, length: Int): String =
    if (appearanceLength(s) < length) paddingRight(s + c, c, length)
    else s

  def indent(s: String, indent: Int): String = {
    val space = (0 until indent).foldLeft(new StringBuilder)((sb, _) => sb.append(' ')).mkString
    s.lines.map(space + _).mkString(Environment.NEW_LINE)
  }
}
