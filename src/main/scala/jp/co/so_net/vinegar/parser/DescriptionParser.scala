package jp.co.so_net.vinegar.parser

import gherkin.ast._
import jp.co.so_net.vinegar.model.Suite

class DescriptionParser(feature: Feature) extends PartialSuiteParser(feature) {

  import StringTrimmer.trimIndent

  def parse(suite: Suite): Suite =
    suite.copy(description = Option(feature.getDescription).map(trimIndent))
}
