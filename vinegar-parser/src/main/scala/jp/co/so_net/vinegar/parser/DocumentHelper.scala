package jp.co.so_net.vinegar.parser

import gherkin.ast

private[parser]
trait DocumentHelper {
  val document: ast.GherkinDocument

  def feature: ast.Feature = document.getFeature
}
