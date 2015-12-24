package jp.co.so_net.vinegar.dto

import jp.co.so_net.vinegar.model.Suite
import org.scalatest._

class BackgroundDtoTest extends FlatSpec with Matchers with FeatureLoader {
  def suite = new BackgroundDto(feature).parseSuite(Suite(name = "test"))

  "Suite" should "have backgrounds" in {
    val background =
      """example.feature を /tmp/vinegar にコピーする
        |/tmp/vinegar/example.feature を UTF-8 にする""".stripMargin
    suite.background should be(Some(background))
  }
}
