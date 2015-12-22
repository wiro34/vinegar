package jp.co.so_net.vinegar.dto

import jp.co.so_net.vinegar.model.Suite
import gherkin.ast._

class DescriptionDto(feature: Feature) extends SuiteDto {

  import collection.JavaConversions._

  def parseSuite(suite: Suite): Suite = {
    val INDENT_PATTERN = """(?m)^\s{2}""".r
    suite.copy(description = Option(feature.getDescription).map { desc =>
      INDENT_PATTERN.replaceAllIn(desc, "")
    })
  }
}
