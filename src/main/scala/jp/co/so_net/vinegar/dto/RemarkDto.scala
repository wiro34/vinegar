package jp.co.so_net.vinegar.dto

import gherkin.ast._
import jp.co.so_net.vinegar.model._

class RemarkDto(feature: Feature) extends SuiteDto with CommentTrimmer {

  import collection.JavaConversions._

  def parseSuite(suite: Suite): Suite = {
    suite.copy(remark = getRemarkComments)
  }

  private def getRemarkComments = {
    val comments = feature.getComments.filter(findRemarkComment(feature.getScenarioDefinitions.head))
      .map(comment => comment.getText)
      .mkString("\n")
    trimComment(comments) match {
      case s if s.nonEmpty => Some(s)
      case _ => None
    }
  }

  private def findRemarkComment(firstScenario: ScenarioDefinition): (Comment) => Boolean = {
    val firstScenarioLine = feature.getScenarioDefinitions.head.getLocation.getLine
    (c: Comment) => c.getLocation.getLine < firstScenarioLine
  }
}
