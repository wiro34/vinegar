package jp.co.so_net.vinegar.dto

trait CommentTrimmer {
  private val commentStripPattern = """(?m)^[ \t\x0B\f\r]*# ?""".r

  def trimComment(comment: String): String = commentStripPattern.replaceAllIn(comment, "")
}
