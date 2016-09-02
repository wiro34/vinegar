package jp.co.so_net.vinegar.parser

import gherkin.ast

private[parser]
class FeatureCommentParser(val document: ast.GherkinDocument) extends DocumentHelper {
  def parse(): Option[String] = new CommentScanner(document).scan(feature)
}
