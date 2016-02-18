package jp.co.so_net.vinegar.model

case class Suite(name: String,
                 description: Option[String] = None,
                 comment: Option[String] = None,
                 background: Option[Background] = None,
                 scenarios: Seq[Scenario] = Seq.empty[Scenario])

case class Background(groups: Seq[GivenGroup])

case class Scenario(name: String, groups: Seq[GivenGroup] = Nil)


