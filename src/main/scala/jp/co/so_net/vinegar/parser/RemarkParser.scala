package jp.co.so_net.vinegar.parser

import gherkin.ast._
import jp.co.so_net.vinegar.model._

class RemarkParser(feature: Feature) extends PartialSuiteParser(feature) {

  import StringTrimmer.trimComment

  import collection.JavaConversions._

  val firstScenario = feature.getScenarioDefinitions.headOption
  val firstScenarioLine = firstScenario.map(_.getLocation.getLine).getOrElse(Int.MaxValue)

  def parse(suite: Suite): Suite = suite.copy(remark = getRemarkComments)

  private def getRemarkComments = {
    val comments = feature.getComments
      .filter(isRemarkComment)
      .map(comment => comment.getText)
      .mkString("\n")
    trimComment(comments) match {
      case s if s.nonEmpty => Some(s)
      case _ => None
    }
  }

  private def isRemarkComment(c: Comment): Boolean = c.getLocation.getLine < firstScenarioLine
}
