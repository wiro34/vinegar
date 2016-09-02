package jp.co.so_net.vinegar.parser

import jp.co.so_net.vinegar.utils.Environment

private[parser]
object StringTrimmer {

  private val COMMENT_PATTERN = """(?m)^[ \t\x0B\f\r]*# ?""".r

  private val INDENT_CHARS = """ \t\x0B\f\r"""

  def trimComment(comment: String): String = COMMENT_PATTERN.replaceAllIn(comment, "")

  def trimIndent(multilineText: String): String = {
    val lines = multilineText.split("(?m)[\r\n]+")
    val minimumCommonIndent = lines.head.zipWithIndex.takeWhile {
      case (c, i) if INDENT_CHARS.contains(c) =>
        lines.forall(_.charAt(i) == c)
      case _ =>
        false
    }.map(_._1).mkString

    lines.map(_.replaceFirst(minimumCommonIndent, "")).mkString(Environment.NEW_LINE)
  }

}
