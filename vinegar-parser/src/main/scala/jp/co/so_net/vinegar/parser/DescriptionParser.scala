package jp.co.so_net.vinegar.parser

import gherkin.ast

private[parser]
class DescriptionParser(val document: ast.GherkinDocument) extends DocumentHelper {
  def parse(): Option[String] = Option(document.getFeature.getDescription).map(StringTrimmer.trimIndent)
}
