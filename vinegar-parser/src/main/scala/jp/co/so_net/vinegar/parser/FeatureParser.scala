package jp.co.so_net.vinegar.parser

import gherkin.ast
import jp.co.so_net.vinegar.models.Feature

private[parser]
class FeatureParser(val document: ast.GherkinDocument) extends DocumentHelper {

  val descriptionParser = new DescriptionParser(document)
  val commentParser = new FeatureCommentParser(document)
  val backgroundParser = new BackgroundParser(document)
  val scenarioDefinitionsParser = new ScenarioDefinitionsParser(document)

  def parse(): Feature = Feature(
    name = feature.getName,
    description = descriptionParser.parse(),
    comment = commentParser.parse(),
    background = backgroundParser.parse(),
    scenarioDefinitions = scenarioDefinitionsParser.parse()
  )
}
