package jp.co.so_net.vinegar.parser

import jp.co.so_net.vinegar.GherkinLoader
import jp.co.so_net.vinegar.models._
import org.scalatest.OptionValues._
import org.scalatest._

class BackgroundParserTest extends FlatSpec with Matchers with GherkinLoader {
  val parser = new BackgroundParser(loadDocument("/good/background.feature"))

  it should "have backgrounds" in {
    parser.parse().value shouldBe Background(
      steps = Seq(Given(text = "the minimalism inside a background"))
    )
  }
}
