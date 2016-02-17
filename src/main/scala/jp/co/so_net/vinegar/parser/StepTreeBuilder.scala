package jp.co.so_net.vinegar.parser

import java.util.List

import gherkin.ast.{Comment, Step => GherkinStep}
import jp.co.so_net.vinegar.model._

import scala.collection.JavaConversions._
import scala.reflect.ClassTag

case class BuilderContext(steps: Seq[GherkinStep],
                          comments: Seq[Comment],
                          commentScanStop: Int)

trait PartialStepTreeBuilder {
  val context: BuilderContext

  def steps = context.steps

  def comments = context.comments

  def commentScanStop = context.commentScanStop
}

class StepTreeBuilder(steps: Seq[GherkinStep],
                      comments: Seq[Comment],
                      commentScanStop: Int) extends StepConverter {
  val context = BuilderContext(steps, comments, commentScanStop)

  def this(steps: List[GherkinStep], comments: List[Comment], commentScanStop: Int) {
    this(steps.toList, comments.toList, commentScanStop)
  }

  def build: Seq[GivenGroup] = inner(convertedSteps)

  private def inner(steps: Seq[Step]): Seq[GivenGroup] = {
    val (givenSteps, rest) = spanGivenSteps(steps)
    val givenGroup = GivenGroup(steps = givenSteps, children = inner2(rest))
    if (rest.nonEmpty)
      givenGroup +: inner(dropUntilGiven(rest))
    else if (givenGroup.nonEmpty)
      Seq(givenGroup)
    else
      Nil
  }

  private def inner2(steps: Seq[Step]): Seq[WhenGroup] = {
    val (whenSteps, rest1) = spanWhenSteps(steps)
    val (thenSteps, rest2) = spanThenSteps(rest1)
    WhenGroup(steps = whenSteps, children = thenSteps) match {
      case group if group.nonEmpty =>
        group +: inner2(rest2)
      case _ =>
        Nil
    }
  }

  private def dropUntilGiven(steps: Seq[Step]): Seq[Step] = steps.dropWhile(!_.isInstanceOf[Given])

  private def spanGivenSteps(steps: Seq[Step]): (Seq[Given], Seq[Step]) = spanSteps[Given](steps)

  private def spanWhenSteps(steps: Seq[Step]): (Seq[When], Seq[Step]) = spanSteps[When](steps)

  private def spanThenSteps(steps: Seq[Step]): (Seq[Then], Seq[Step]) = spanSteps[Then](steps)

  private def spanSteps[A <: Step : ClassTag](steps: Seq[Step]): (Seq[A], Seq[Step]) = {
    val (filteredSteps, rest) = steps.span {
      case a: A => true
      case _ => false
    }
    // asInstanceOf を使わないで変換する方法を知りたい
    (filteredSteps.asInstanceOf[Seq[A]], rest)
  }
}
