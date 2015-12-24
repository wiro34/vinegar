package jp.co.so_net.vinegar.model

case class Suite(name: String,
                 description: Option[String] = None,
                 background: Option[String] = None,
                 scenarios: Seq[Scenario] = Seq.empty[Scenario])

case class Scenario(id: Int,
                    name: String,
                    cases: Seq[Case] = Seq.empty[Case])

case class Case(id: String,
                given: Option[String] = None,
                when: Option[String] = None,
                then: Option[String] = None,
                note: Option[String] = None)
