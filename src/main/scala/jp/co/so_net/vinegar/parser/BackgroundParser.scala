package jp.co.so_net.vinegar.parser

import gherkin.ast.{Background => GherkinBackground}
import jp.co.so_net.vinegar.model.Background

trait BackgroundParser extends GherkinParser {

  import collection.JavaConversions._

  def background: Option[Background] = {
    for {
      background <- Option[GherkinBackground](feature.getBackground)
      steps <- Option(background.getSteps)
    } yield {
      Background(groups = new StepTreeBuilder(steps, feature.getComments, commentScanStop).build)
    }
  }

  protected def commentScanStop: Int = {
    feature.getScenarioDefinitions.headOption.map(_.getLocation.getLine).getOrElse(Int.MaxValue)
  }
}
