package jp.co.so_net.vinegar.parser

import gherkin.ast.{Background => GherkinBackground, Feature}
import jp.co.so_net.vinegar.model._

class BackgroundParser(feature: Feature) extends PartialSuiteParser(feature) {

  import collection.JavaConversions._

  def parse(suite: Suite): Suite = suite.copy(background = takeBackground)

  private def takeBackground: Option[Background] = {
    for {
      background <- Option[GherkinBackground](feature.getBackground)
      steps <- Option(background.getSteps)
    } yield {
      Background(group = new StepTreeBuilder(steps, feature.getComments, commentScanStop).build)
    }
  }

  protected def commentScanStop: Int = {
    feature.getScenarioDefinitions.headOption.map(_.getLocation.getLine).getOrElse(Int.MaxValue)
  }
}

trait Background2 {
  import collection.JavaConversions._
  import jp.co.so_net.vinegar.model.{Background => B}

  def background(implicit feature: Feature): Option[B] = {
    for {
      background <- Option[GherkinBackground](feature.getBackground)
      steps <- Option(background.getSteps)
    } yield {
      B(group = new StepTreeBuilder(steps, feature.getComments, commentScanStop).build)
    }
  }

  protected def commentScanStop(implicit feature: Feature): Int = {
    feature.getScenarioDefinitions.headOption.map(_.getLocation.getLine).getOrElse(Int.MaxValue)
  }
}
