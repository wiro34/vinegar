package jp.co.so_net.vinegar.models

import scala.reflect.ClassTag

trait GWTNode[T, V] {
  val steps: Seq[T]
  val children: Seq[V]

  def nonEmpty = steps.nonEmpty || children.nonEmpty
}

case class GWTRoot(children: Seq[GivenGroup] = Nil)

case class GivenGroup(steps: Seq[Given] = Nil, children: Seq[WhenGroup] = Nil) extends GWTNode[Given, WhenGroup]

case class WhenGroup(steps: Seq[When] = Nil, children: Seq[Then] = Nil) extends GWTNode[When, Then]

object GWTTree {
  def apply(steps: Seq[Step]): GWTRoot = {
    GWTRoot(takeGivenGroups(steps))
  }

  private def takeGivenGroups(steps: Seq[Step]): Seq[GivenGroup] = {
    val (givenSteps, rest) = spanGivenSteps(steps)
    val givenGroup = GivenGroup(steps = givenSteps, children = takeWhenGroups(rest))
    if (rest.nonEmpty)
      givenGroup +: takeGivenGroups(dropUntilGiven(rest))
    else if (givenGroup.nonEmpty)
      Seq(givenGroup)
    else
      Nil
  }

  private def takeWhenGroups(steps: Seq[Step]): Seq[WhenGroup] = {
    val (whenSteps, rest1) = spanWhenSteps(steps)
    val (thenSteps, rest2) = spanThenSteps(rest1)
    WhenGroup(steps = whenSteps, children = thenSteps) match {
      case group if group.nonEmpty => group +: takeWhenGroups(rest2)
      case _ => Nil
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
    (filteredSteps.asInstanceOf[Seq[A]], rest)
  }
}
