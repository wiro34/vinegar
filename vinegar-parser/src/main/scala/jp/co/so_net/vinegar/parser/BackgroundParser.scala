package jp.co.so_net.vinegar.parser

import gherkin.ast
import jp.co.so_net.vinegar.models.Background

private[parser]
class BackgroundParser(val document: ast.GherkinDocument) extends DocumentHelper {

  import collection.JavaConversions._

  def parse(): Option[Background] = {
    for {
      background <- feature.getChildren.headOption
      steps <- Option(background.getSteps.toList)
    } yield {
      Background(steps = new StepsParser(document).parse(steps))
    }
  }
}
