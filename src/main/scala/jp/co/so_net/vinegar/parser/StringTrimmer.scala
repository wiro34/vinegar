package jp.co.so_net.vinegar.parser

object StringTrimmer {

  private val commentPattern = """(?m)^[ \t\x0B\f\r]*# ?""".r

  private val indentChars = """ \t\x0B\f\r"""

  private val newLine: String = try {
    System.getProperty("line.separator")
  } catch {
    case _: Throwable => "\n"
  }

  def trimComment(comment: String): String = commentPattern.replaceAllIn(comment, "")

  def trimIndent(multilineText: String): String = {
    val lines = multilineText.split("(?m)[\r\n]+")
    val minimumCommonIndent = lines.head.zipWithIndex.takeWhile {
      case (c, i) if indentChars.contains(c) =>
        lines.forall(_.charAt(i) == c)
      case _ =>
        false
    }.map(_._1).mkString

    lines.map(_.replaceFirst(minimumCommonIndent, "")).mkString(newLine)
  }

}
