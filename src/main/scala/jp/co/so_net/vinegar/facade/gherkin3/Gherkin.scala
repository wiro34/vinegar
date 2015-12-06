package jp.co.so_net.vinegar.facade.gherkin3

import jp.co.so_net.vinegar.facade.GlobalExportedModule

import scala.scalajs.js

@js.native
trait Gherkin extends js.Object

object Gherkin extends GlobalExportedModule[Gherkin]("gherkin") {
  def newInstance[T <: js.Object](clazz: js.Dynamic): T =
    js.Dynamic.newInstance(clazz)().asInstanceOf[T]
}

@js.native
trait Parser extends js.Object {
  def parse(text: String): Feature = js.native
}

@js.native
trait Feature extends js.Object {
  val language: String = js.native
  val keyword: String = js.native
  val name: String = js.native
  val description: String = js.native
  val scenarioDefinitions: js.Array[ScenarioDefinition] = js.native
  val background: Background = js.native
  val comments: js.Array[Comment] = js.native
}

@js.native
trait Background extends js.Object {
  val keyword: String = js.native
  val name: String = js.native
  val description: String = js.native
  val steps: js.Array[Step] = js.native
}

@js.native
trait ScenarioDefinition extends js.Object {
  val keyword: String = js.native
  val name: String = js.native
  val description: String = js.native
  val steps: js.Array[Step] = js.native
  val location: Location = js.native
}

@js.native
trait Scenario extends ScenarioDefinition

@js.native
trait Step extends js.Object {
  val keyword: String = js.native
  val text: String = js.native
  val argument: js.UndefOr[DocString] = js.native
  val location: Location = js.native
}

@js.native
trait Location extends js.Object {
  val line: Int = js.native
  val column: Int = js.native
}

@js.native
trait DocString extends js.Object {
  val content: String = js.native
  val contentType: String = js.native
}

@js.native
trait Comment extends js.Object {
  val text: String = js.native
  val location: Location = js.native
}
