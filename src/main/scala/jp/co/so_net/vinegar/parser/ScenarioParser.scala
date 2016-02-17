package jp.co.so_net.vinegar.parser

import jp.co.so_net.vinegar.model._

trait ScenarioParser extends GherkinParser {

  import collection.JavaConversions._

  def scenarios: Seq[Scenario] = feature.getScenarioDefinitions
    .filter(pendedScenario)
    .zipWithIndex
    .map { case (scenario, index) =>
      Scenario(
        name = scenario.getName,
        groups = new StepTreeBuilder(scenario.getSteps, feature.getComments, commentScanStop(index)).build
      )
    }

  private def commentScanStop(scenarioIndex: Int): Int =
    feature.getScenarioDefinitions.toList.lift(scenarioIndex + 1).map(_.getLocation.getLine).getOrElse(Int.MaxValue)

  private def pendedScenario(scenario: gherkin.ast.ScenarioDefinition): Boolean = {
    !scenario.getTags.toList.map(_.getName).contains("@pending")
  }

}
