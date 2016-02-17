package jp.co.so_net.vinegar.model


sealed trait Step {
  val text: String
  val comment: Option[String]
  val argument: Option[Argument]
}

case class Id[Then](id: String) extends AnyVal

case class Given(text: String = "",
                 comment: Option[String] = None,
                 argument: Option[Argument] = None) extends Step

case class When(text: String = "",
                comment: Option[String] = None,
                argument: Option[Argument] = None) extends Step

case class Then(id: Id[Then] = Id(""),
                text: String = "",
                comment: Option[String] = None,
                argument: Option[Argument] = None) extends Step

sealed trait StepGroup[T <: Step, Child] {
  val steps: Seq[T]
  val children: Seq[Child]

  def nonEmpty: Boolean = steps.nonEmpty || children.nonEmpty
}

case class GivenGroup(steps: Seq[Given] = Nil,
                      children: Seq[WhenGroup] = Nil) extends StepGroup[Given, WhenGroup]

case class WhenGroup(steps: Seq[When] = Nil,
                     children: Seq[Then] = Nil) extends StepGroup[When, Then]

sealed trait Argument

case class DataText(text: String) extends Argument

