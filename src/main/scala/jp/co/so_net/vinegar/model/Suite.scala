package jp.co.so_net.vinegar.model

case class Suite(name: String,
                 description: Option[String] = None,
                 background: Option[Background] = None,
                 remark: Option[String] = None,
                 scenarios: Seq[Scenario] = Seq.empty[Scenario])

case class Background(group: Seq[GivenGroup])

case class Scenario(id: Int,
                    name: String,
                    group: Seq[GivenGroup] = Nil)


