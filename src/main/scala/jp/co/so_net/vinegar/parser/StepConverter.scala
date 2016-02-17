package jp.co.so_net.vinegar.parser

import gherkin.ast.{Step => GherkinStep, Comment, DocString, Node}
import jp.co.so_net.vinegar.model._

/**
 * Convert List[gherkin.ast.*] into List[Step]
 */
trait StepConverter extends PartialStepTreeBuilder {

  import StringTrimmer.trimComment

  def convertedSteps: Seq[Step] = {
    def inner(steps: Seq[GherkinStep], result: Seq[Step]): Seq[Step] = {
      steps match {
        case step :: tail =>
          val text = step.getText
          val argument = getArgument(Option(step.getArgument))
          val comment = getComment(step, tail.headOption, comments, commentScanStop)
          StepFactory.create(step, result.lastOption) match {
            case g: Given =>
              inner(tail, result :+ g.copy(text = text, argument = argument, comment = comment))
            case w: Then =>
              inner(tail, result :+ w.copy(text = text, argument = argument, comment = comment))
            case t: When =>
              inner(tail, result :+ t.copy(text = text, argument = argument, comment = comment))
          }
        case Nil =>
          result
      }
    }
    inner(steps, Nil)
  }

  private def getArgument(nodeOption: Option[Node]): Option[Argument] = nodeOption match {
    case Some(ds: DocString) =>
      Some(DataText(ds.getContent))
    case _ =>
      None
  }

  private def getComment(step: GherkinStep, nextStep: Option[GherkinStep], comments: Seq[Comment], commentScanStop: Int): Option[String] = {
    val range = Range(
      step.getLocation.getLine,
      nextStep.map(_.getLocation.getLine).getOrElse(commentScanStop)
    )
    val stepComments = comments
      .filter(comment => range.contains(comment.getLocation.getLine))
      .sortBy(comment => comment.getLocation.getLine)
      .map(comment => comment.getText)
      .mkString("\n")
    trimComment(stepComments) match {
      case s if s.nonEmpty => Some(s)
      case _ => None
    }
  }

}
