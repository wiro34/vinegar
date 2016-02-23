package jp.co.so_net.vinegar.parser

import gherkin.ast.{Step => GherkinStep}
import jp.co.so_net.vinegar.model._

class UnsupportedKeywordException(message: String) extends RuntimeException(message)

object StepFactory {
  val givenPattern = """(Given|前提)""".r
  val whenPattern = """(When|もし)""".r
  val thenPattern = """(Then|ならば)""".r
  val otherPattern = """(And|But|かつ|ただし)""".r

  def create(step: GherkinStep, lastOption: Option[Step]): Step = {
    (step, lastOption) match {
      case GivenPattern(g) => g
      case WhenPattern(w) => w
      case ThenPattern(t) => t
      case (s, None) if otherPattern.findFirstIn(s.getKeyword).nonEmpty =>
        throw new UnsupportedKeywordException(s"The location of `${step.getKeyword}` is invalid.")
      case (s, _) =>
        throw new UnsupportedKeywordException(s"Keyword `${s.getKeyword}` is not supported.")
    }
  }

  abstract class StepCasePattern {
    type -->[A, B] = PartialFunction[A, B]
    type Result = Option[(Step)]

    def matchByKeyword: String --> Result

    def matchByLastStep: Option[Step] --> Result

    def unapply(t: (GherkinStep, Option[Step])): Result = {
      val step = t._1
      val lastOption = t._2

      val other: String --> Option[Step] = {
        case otherPattern(k) => lastOption
      }

      val none: Any --> Result = {
        case _ => None
      }

      matchByKeyword orElse (other andThen matchByLastStep.orElse(none)) orElse none apply step.getKeyword.trim
    }
  }

  object GivenPattern extends StepCasePattern {
    def matchByKeyword = {
      case givenPattern(_) => Some(Given())
    }

    def matchByLastStep = {
      case Some(_: Given) => Some(Given())
    }
  }

  object WhenPattern extends StepCasePattern {
    def matchByKeyword = {
      case whenPattern(k) => Some(When())
    }

    def matchByLastStep = {
      case Some(_: When) => Some(When())
    }
  }

  object ThenPattern extends StepCasePattern {
    def matchByKeyword = {
      case thenPattern(k) => Some(Then())
    }

    def matchByLastStep = {
      case Some(_: Then) => Some(Then())
    }
  }

}
