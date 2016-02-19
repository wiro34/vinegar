package jp.co.so_net.vinegar.parser

import gherkin.ast._

trait FeatureCommentParser extends GherkinParser {

  import StringTrimmer.trimComment

  import collection.JavaConversions._

  def featureComment: Option[String] = {
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

  private def firstScenario = feature.getScenarioDefinitions.headOption

  private def firstScenarioLine = firstScenario.map(_.getLocation.getLine).getOrElse(Int.MaxValue)

}
