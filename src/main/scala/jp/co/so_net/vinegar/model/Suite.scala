package jp.co.so_net.vinegar.model

case class Suite(name: String,
                 description: Option[String] = None,
                 background: Option[String] = None,
                 scenarios: Seq[Scenario] = Seq.empty[Scenario])

case class Scenario(id: Int,
                    name: String,
                    cases: Seq[Case] = Seq.empty[Case])

abstract class Case(text: Option[String] = None, note: Option[String] = None)

case class Given(text: Option[String] = None, note: Option[String] = None, children: Seq[When] = Seq.empty[When]) extends Case(text, note)

case class When(text: Option[String] = None, note: Option[String] = None, children: Seq[Then] = Seq.empty[Then]) extends Case(text, note)

case class Then(id: String, text: Option[String] = None, note: Option[String] = None) extends Case(text, note)
case object Another extends Case(None,None)

//case class Case(id: String,
//                given: Option[String] = None, givenNote: Option[String] = None,
//                when: Option[String] = None, whenNote: Option[String] = None,
//                then: Option[String] = None, thenNote: Option[String] = None)
