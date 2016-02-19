package jp.co.so_net.vinegar.builder.excel

import java.nio.charset.Charset

// 行の高さを概算
private[excel] trait HeightCalculator {
  private val maxLineLength = 84

  implicit class BooleanOps(val b: Boolean) {
    final def option[A](a: => A): Option[A] = if (b) Some(a) else None
  }

  def calcHeight(text: String): Int = text.lines.map(line => Seq(calcLineHeight(line), 1).max).sum

  def calcLineHeight(text: String): Int = wordWrap(text).lines.length

  def wordWrap(text: String): String = {
    val wrapPattern = """[\s-]""".r
    def inner(rest: String, wrapped: Seq[String]): Seq[String] = {
      if (rest.length > maxLineLength) {
        val matcher = wrapPattern.pattern.matcher(rest)
        val lastIndex = Iterator.continually(matcher.find().option(matcher.start))
          .takeWhile(_.nonEmpty).toList.flatten
          .filter(_ <= maxLineLength)
          .lastOption

        lastIndex match {
          case Some(index) =>
            inner(rest.substring(index + 1, rest.length), wrapped :+ rest.substring(0, index + 1))
          case None =>
            inner(rest.substring(maxLineLength, rest.length), wrapped :+ rest.substring(0, maxLineLength))
        }
      } else {
        wrapped :+ rest
      }
    }

    inner(text, Nil).mkString("\n")
  }
}
