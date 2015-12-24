package jp.co.so_net.vinegar.dto

import jp.co.so_net.vinegar.model.{Case, Scenario, Suite}
import gherkin.ast._

abstract class AbstractStep(val keyword: String)

case object GivenStep extends AbstractStep("Given ")

case object WhenStep extends AbstractStep("When ")

case object ThenStep extends AbstractStep("Then ")

case object AnotherStep extends AbstractStep("")

object StepCompanion {
  val givenPattern = """(Given )""".r
  val whenPattern = """(When )""".r
  val thenPattern = """(Then )""".r
  val otherPattern = """(And |But )""".r

  def apply(step: Step, lastKeyword: String): AbstractStep = apply(step.getKeyword, lastKeyword)

  def apply(keyword: String, lastKeyword: String): AbstractStep = keyword match {
    case givenPattern(_) => GivenStep
    case whenPattern(_) => WhenStep
    case thenPattern(_) => ThenStep
    case otherPattern(_) => apply(lastKeyword, null)
    case _ => AnotherStep
  }
}

class ScenarioDto(feature: Feature) extends SuiteDto {

  import collection.JavaConversions._

  def parseSuite(suite: Suite): Suite = {
    val scenarios = for (scenarioDefinitions <- Option(feature.getScenarioDefinitions)) yield {
      scenarioDefinitions
        .filter(scenario => scenario.getSteps.nonEmpty)
        .zipWithIndex
        .map { case (scenario, index) =>
          val id = index + 1
          Scenario(id = id, name = scenario.getName, cases = new StepDto(feature, scenario, id).parseSteps)
        }
    }
    suite.copy(scenarios = scenarios.getOrElse(Seq.empty[Scenario]))
  }
}

class StepDto(feature: Feature, scenario: ScenarioDefinition, scenarioId: Int) {

  import collection.JavaConversions._

  def parseSteps: Seq[Case] = {
    def inner(steps: Seq[gherkin.ast.Step], c: Case, last: String = GivenStep.keyword): Seq[Case] = {
      steps match {
        case step :: tail =>
          StepCompanion(step, last) match {
            case GivenStep =>
              inner(tail, joinCase(c, given = getStepText(step), note = getStepComment(step)), GivenStep.keyword)
            case WhenStep =>
              inner(tail, joinCase(c, when = getStepText(step), note = getStepComment(step)), WhenStep.keyword)
            case ThenStep =>
              joinCase(c, then = getStepText(step), note = getStepComment(step)) +:
                inner(tail, Case(id = incrementCaseId(c)), ThenStep.keyword)
            case _ =>
              inner(tail, c, last)
          }
        case Nil =>
          Nil
      }
    }
    val steps = scenario.getSteps.toList
    inner(steps, Case(id = makeInitialCaseId))
  }

  val caseIdFormat = """%03d%04d"""

  private def makeCaseId(n: Int): String = caseIdFormat.format(scenarioId, n)

  private def makeInitialCaseId: String = makeCaseId(1)

  private def incrementCaseId(c: Case): String = makeCaseId(c.id.substring(3).toInt + 1)

  private def joinCase(c1: Case,
                       given: Option[String] = None,
                       when: Option[String] = None,
                       then: Option[String] = None,
                       note: Option[String] = None): Case = {
    c1.copy(
      given = join(c1.given, given),
      when = join(c1.when, when),
      then = join(c1.then, then),
      note = join(c1.note, note)
    )
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
