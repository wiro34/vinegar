package jp.co.so_net.vinegar.parser

import gherkin.{ast, _}
import jp.co.so_net.vinegar.models.Feature

import scala.util.Try

object GherkinParser {
  def parse(gherkinText: String): Try[Feature] = new GherkinParser(gherkinText).parse()
}

class GherkinParser private(gherkinText: String) {
  private def document = new gherkin.Parser[ast.GherkinDocument](new AstBuilder()).parse(gherkinText)

  def parse(): Try[Feature] = Try(new FeatureParser(document).parse())
}
