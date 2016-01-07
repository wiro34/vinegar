package jp.co.so_net.vinegar.model

case class Suite(name: String,
                 description: Option[String] = None,
                 background: Option[String] = None,
                 scenarios: Seq[Scenario] = Seq.empty[Scenario])

case class Scenario(id: Int,
                    name: String,
                    cases: Seq[Given] = Seq.empty[Given])

sealed trait Case {
  val text: Option[String]
  val note: Option[String]
}

trait HasChildren[T] {
  val children: Seq[T]
}

case class Given(text: Option[String] = None,
                 note: Option[String] = None,
                 children: Seq[When] = Seq.empty[When]) extends Case with HasChildren[When]

case class When(text: Option[String] = None,
                note: Option[String] = None,
                children: Seq[Then] = Seq.empty[Then]) extends Case with HasChildren[Then]

case class Then(id: String,
                text: Option[String] = None,
                note: Option[String] = None) extends Case
