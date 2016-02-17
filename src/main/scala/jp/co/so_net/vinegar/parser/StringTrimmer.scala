package jp.co.so_net.vinegar.parser

object StringTrimmer {

  private val COMMENT_Pattern = """[ \t\x0B\f\r]*# ?""".r

  private val INDENT_PATTERN = """(?m)^\s{2}""".r

  def trimComment(comment: String): String = COMMENT_Pattern.replaceAllIn(comment, "")

  def trimIndent(lines: String): String = INDENT_PATTERN.replaceAllIn(lines, "")

}
