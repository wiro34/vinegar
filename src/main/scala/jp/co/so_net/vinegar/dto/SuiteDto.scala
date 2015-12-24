package jp.co.so_net.vinegar.dto

import jp.co.so_net.vinegar.model.Suite
import gherkin.ast._

trait SuiteDto {
  def parseSuite(suite: Suite): Suite
}
