package jp.co.so_net.vinegar.parser

import jp.co.so_net.vinegar.GherkinLoader
import jp.co.so_net.vinegar.models.{Background, Feature}
import org.scalatest.OptionValues._
import org.scalatest._

class FeatureParserTest extends FlatSpec with Matchers with Inside with GherkinLoader {
  val parser = new FeatureParser(loadDocument("/fixture.feature"))

  it should "return a feature" in {
    inside(parser.parse()) {
      case Feature(name, description, comment, background, scenarioDefinitions) =>
        name shouldBe "テスト用フィーチャファイル"
        description.value shouldBe
          """ * フィーチャの説明文は「目的／観点」に設定されます。
            |  * もちろん複数行も可能です。
            |* インデントは最短マッチで削除されます。""".stripMargin
        comment.value shouldBe
          """フィーチャ直下にコメントを書くと備考になります。
            |必要に応じて使い分けてください。""".stripMargin
        background.value shouldBe a [Background]
        scenarioDefinitions shouldBe a [Seq[_]]
    }
  }
}
