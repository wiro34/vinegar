package jp.co.so_net.vinegar.dto

import jp.co.so_net.vinegar.model.Suite
import gherkin.ast._

class BackgroundDto(feature: Feature) extends SuiteDto {

  import collection.JavaConversions._

  def parseSuite(suite: Suite): Suite = {
    val background = for {
      background <- Option[Background](feature.getBackground)
      steps <- Option(background.getSteps)
    } yield steps.map(_.getText).reduce(_ + "\n" + _)
    suite.copy(background = background)
  }
}
