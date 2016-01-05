package jp.co.so_net.vinegar.dto

import jp.co.so_net.vinegar.model._
import gherkin.ast._

class ScenarioDto(feature: Feature) extends SuiteDto {

  import collection.JavaConversions._
  import jp.co.so_net.vinegar.model.{Scenario => ScenarioModel}

  def parseSuite(suite: Suite): Suite = {
    val scenarios = for (scenarioDefinitions <- Option(feature.getScenarioDefinitions)) yield {
      scenarioDefinitions
        .filter(scenario => scenario.getSteps.nonEmpty)
        .zipWithIndex
        .map { case (scenario, index) =>
          val id = index + 1
          ScenarioModel(id = id, name = scenario.getName, cases = new StepDto(feature, scenario, id).parseSteps)
        }
    }
    suite.copy(scenarios = scenarios.getOrElse(Seq.empty[ScenarioModel]))
  }
}
