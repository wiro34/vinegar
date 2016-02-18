package jp.co.so_net.vinegar.model

import jp.co.so_net.vinegar.newLine

sealed trait Step {
  val text: String
  val comment: Option[String]
  val argument: Option[Argument]

  def mkString: String = {
    Seq(text, comment.map(_.replaceAll("(?m)^", "# ")).getOrElse(""))
      .filter(_.nonEmpty)
      .mkString(newLine)
  }
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
                argument: Option[Argument] = None) extends Step {

  override def mkString: String = {
    Seq(Some(text), argument.map(_.mkString)).flatten.mkString(newLine)
  }
}

sealed trait StepGroup[T <: Step, Child] {
  val steps: Seq[T]
  val children: Seq[Child]

  def nonEmpty: Boolean = steps.nonEmpty || children.nonEmpty
}

case class GivenGroup(steps: Seq[Given] = Nil,
                      children: Seq[WhenGroup] = Nil) extends StepGroup[Given, WhenGroup] {
  //  def mkString: String = {
  //    steps.map(_.text).mkString(Vinegar.newLine)
  //  }
}

case class WhenGroup(steps: Seq[When] = Nil,
                     children: Seq[Then] = Nil) extends StepGroup[When, Then]

sealed trait Argument {
  def mkString: String
}

case class DataText(text: String) extends Argument {
  def mkString: String = text
}
