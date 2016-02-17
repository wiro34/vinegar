package jp.co.so_net.vinegar.parser

import jp.co.so_net.vinegar.model._
import gherkin.ast._

class ScenarioParser(feature: Feature) extends PartialSuiteParser(feature) {

  import collection.JavaConversions._
  import jp.co.so_net.vinegar.model.{Scenario => ScenarioModel}

  def parse(suite: Suite): Suite = suite.copy(scenarios = scenarios)

  private def scenarios = feature.getScenarioDefinitions
    .filter(scenario => scenario.getSteps.nonEmpty)
    .zipWithIndex
    .map { case (scenario, index) =>
      val id = index + 1
//      ScenarioModel(id = id,
//        name = scenario.getName,
//        cases = new StepDto(feature, scenario, id).parseSteps)
      ScenarioModel(id = id,
        name = scenario.getName)
    }
}
