package jp.co.so_net.vinegar.parser

import gherkin.ast
import jp.co.so_net.vinegar.models._
import jp.co.so_net.vinegar.parser.Exceptions.UnsupportedKeywordException

private[parser]
class ScenarioDefinitionsParser(val document: ast.GherkinDocument) extends DocumentHelper {

  import collection.JavaConversions._

  private lazy val stepParser = new StepsParser(document)

  def parse(): Seq[ScenarioDefinition] = {
    feature.getChildren.toList
      .filter(exceptBackground)
      .map {
        case scenario: ast.Scenario =>
          Scenario(
            name = scenario.getName,
            steps = stepParser.parse(scenario.getSteps.toList)
          )
        case scenarioOutline: ast.ScenarioOutline =>
          ScenarioOutline(
            name = scenarioOutline.getName,
            steps = stepParser.parse(scenarioOutline.getSteps.toList),
            examples = scenarioOutline.getExamples.toList.map(toExample)
          )
        case s =>
          throw new UnsupportedKeywordException(s.getKeyword)
      }
  }

  private def exceptBackground(scenario: ast.ScenarioDefinition): Boolean = !scenario.isInstanceOf[ast.Background]

  private def toExample(example: ast.Examples): Example = {
    def convertCells(cells: java.util.List[ast.TableCell]): Seq[TableCell] =
      cells.toList.map(cell => TableCell(cell.getValue))

    Example(
      header = TableRow(convertCells(example.getTableHeader.getCells)),
      rows = example.getTableBody.toList.map(body => TableRow(convertCells(body.getCells)))
    )
  }
}
