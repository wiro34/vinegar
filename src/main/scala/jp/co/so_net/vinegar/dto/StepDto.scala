package jp.co.so_net.vinegar.dto

import gherkin.ast._
import jp.co.so_net.vinegar.model._

import scala.annotation.tailrec

trait StepMatchPatternBase {
  val givenPattern = """(Given|前提)""".r
  val whenPattern = """(When|もし)""".r
  val thenPattern = """(Then|ならば)""".r
  val otherPattern = """(And|But|かつ|ただし)""".r

  trait Extractor {
    type -->[A, B] = PartialFunction[A, B]
    type Result = Option[(String)]

    def keywordExtractor: String --> Result

    def caseExtractor: Option[Case] --> Result

    def unapply(t: (Step, Option[Case])): Result = {
      val step = t._1
      val last = t._2

      val other: String --> Option[Case] = {
        case otherPattern(k) => last
      }

      val none: Any --> Result = {
        case _ => None
      }

      keywordExtractor orElse (other andThen caseExtractor.orElse(none)) orElse none apply step.getKeyword.trim
    }
  }

  object GivenPattern extends Extractor {
    def keywordExtractor = {
      case givenPattern(k) => Some(k)
    }

    def caseExtractor = {
      case Some(_: Given) => Some("Given")
    }
  }

  object WhenPattern extends Extractor {
    def keywordExtractor = {
      case whenPattern(k) => Some(k)
    }

    def caseExtractor = {
      case Some(_: When) => Some("Given")
    }
  }

  object ThenPattern extends Extractor {
    def keywordExtractor = {
      case thenPattern(k) => Some(k)
    }

    def caseExtractor = {
      case Some(_: Then) => Some("Given")
    }
  }

}

object StepMatchPattern extends StepMatchPatternBase

class StepDto(feature: Feature, scenario: ScenarioDefinition, scenarioId: Int) {

  import StepMatchPattern._

  import collection.JavaConversions._

  val caseIdFormat = """%03d%03d"""

  case class Root(children: Seq[Given]) extends HasChildren[Given]

  def parseSteps: Seq[Given] = {
    val steps = scenario.getSteps.toList
    inner(1, steps, None, Root(Seq.empty[Given])).children
  }

  @tailrec
  private def inner(id: Int, steps: Seq[gherkin.ast.Step], last: Option[Case], root: Root): Root = {
    take(id, steps.headOption, last) match {
      case step@Some(g: Given) =>
        inner(id, steps.tail, step, root.copy(children = root.children :+ g))
      case step@Some(w: When) =>
        val g = root.children.lastOption.getOrElse(Given())
        inner(id, steps.tail, step, root.copy(children = initOfChildren(root) :+ g.copy(children = g.children :+ w)))
      case step@Some(t: Then) =>
        val g = root.children.lastOption.getOrElse(Given())
        val w = g.children.lastOption.getOrElse(When())
        inner(id + 1, steps.tail, step, root.copy(children = initOfChildren(root) :+ g.copy(children = initOfChildren(g) :+ w.copy(children = w.children :+ t))))
      case None =>
        root
    }
  }

  private def take(id: Int, stepOption: Option[Step], last: Option[Case]): Option[Case] = {
    stepOption.map { step =>
      val text = getStepText(step)
      val note = getStepComment(step)
      (step, last) match {
        case GivenPattern(_) => Given(text = text, note = note)
        case WhenPattern(_) => When(text = text, note = note)
        case ThenPattern(_) => Then(id = makeCaseId(id), text = text, note = note)
      }
    }
  }

  private def makeCaseId(n: Int): String = caseIdFormat.format(scenarioId, n)

  private def initOfChildren[T](hc: HasChildren[T]): Seq[T] = hc.children match {
    case init :+ last => init
    case _ => Nil
  }

  private def getStepText(step: Step): Option[String] = {
    val argument = step.getArgument match {
      case s: DocString => Option(s.getContent)
      case _ => None
    }
    join(Option(step.getText), argument)
  }

  private def join(s1: Option[String], s2: Option[String]): Option[String] = {
    (s1, s2) match {
      case (Some(x), Some(y)) => Some(x + "\n" + y)
      case (Some(x), None) => Some(x)
      case (None, Some(y)) => Some(y)
      case (None, None) => None
    }
  }

  val commentStripPattern = """[ \t\x0B\f\r]*# ?""".r

  private def getStepComment(step: Step): Option[String] = {
    val range = Range(step.getLocation.getLine, scanStopLine(step))
    val comments = feature.getComments
      .filter(comment => range.contains(comment.getLocation.getLine))
      .sortBy(comment => comment.getLocation.getLine)
      .map(comment => comment.getText)
      .mkString("\n")
    commentStripPattern.replaceAllIn(comments, "") match {
      case s if s.nonEmpty => Some(s)
      case _ => None
    }
  }

  private def scanStopLine(step: Step): Int = {
    def findNextStep(steps: Seq[Step]): Option[Location] = steps match {
      case s :: tail if s equals step => tail.headOption.map(_.getLocation)
      case s :: tail => findNextStep(tail)
      case Nil => None
    }

    def findNextScenario(scenarios: Seq[ScenarioDefinition]): Option[Location] = scenarios match {
      case s :: tail if s equals scenario => tail.headOption.map(_.getLocation)
      case s :: tail => findNextScenario(tail)
      case Nil => None
    }

    findNextStep(scenario.getSteps.toList) match {
      case Some(location) =>
        location.getLine
      case None =>
        findNextScenario(feature.getScenarioDefinitions.toList).map(_.getLine).getOrElse(Int.MaxValue)
    }
  }
}
