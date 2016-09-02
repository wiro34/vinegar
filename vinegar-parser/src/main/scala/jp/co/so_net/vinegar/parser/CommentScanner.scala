package jp.co.so_net.vinegar.parser

import gherkin.ast

private[parser]
class CommentScanner(document: ast.GherkinDocument) {

  import collection.JavaConversions._

  val feature = document.getFeature

  def scan(node: ast.Node): Option[String] = {
    node match {
      case feature: ast.Feature =>
        takeComment(feature.getLocation.getLine, featureCommentScanStop)
      case step: ast.Step =>
        takeComment(step.getLocation.getLine, stepCommentScanStop(step))
      case _ =>
        throw new IllegalArgumentException(s"${node.getClass.getSimpleName} is not usable")
    }
  }

  private def takeComment(start: Int, scanStop: Int): Option[String] =
    StringTrimmer.trimComment(takeComments(start, scanStop).map(c => c.getText).mkString("\n")) match {
      case s if s.nonEmpty => Some(s)
      case _ => None
    }

  private def takeComments(start: Int, scanStop: Int): Seq[ast.Comment] = takeComments(Range(start, scanStop))

  private def takeComments(range: Range): Seq[ast.Comment] =
    document.getComments.filter(c => range.contains(c.getLocation.getLine))

  private def featureCommentScanStop = nodeCommentScanStop(feature.getChildren.headOption)

  private def stepCommentScanStop(step: ast.Step) = nodeCommentScanStop(nextNode(step))

  private def nextNode(step: ast.Step): Option[ast.Node] = {
    def findNextStep(steps: Seq[ast.Step]): Option[ast.Step] = steps match {
      case head :: rest if head.equals(step) =>
        rest.headOption
      case head :: rest =>
        findNextStep(rest)
      case Nil =>
        None
    }

    def findNextNode(children: Seq[ast.ScenarioDefinition]): Option[ast.Node] = children match {
      case head :: rest if head.getSteps.contains(step) =>
        findNextStep(head.getSteps.toList).orElse(rest.headOption)
      case head :: rest =>
        findNextNode(rest)
      case Nil =>
        throw new NoSuchElementException()
    }

    findNextNode(feature.getChildren.toList)
  }

  private def nodeCommentScanStop(node: Option[ast.Node]) = node.map(_.getLocation.getLine).getOrElse(Int.MaxValue)
}
