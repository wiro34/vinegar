package jp.co.so_net.vinegar.parser

import jp.co.so_net.vinegar.GherkinLoader
import org.scalatest.OptionValues._
import org.scalatest._
import org.scalatest.prop.TableDrivenPropertyChecks._

class DescriptionParserTest extends FlatSpec with Matchers with GherkinLoader {
  val testPatterns = Table(
    ("file", "description"),

    ("/good/descriptions.feature", "This is a single line description"),

    ("/description_ext.feature",
      """ テスト仕様書はレビューや差分確認が容易になるため、テキストファイルで作成したい
        | 実施と結果の報告はテキストファイルでは難しいのでひとまずエクセルでまとめたい
        |テスト仕様書から自動で結果報告ファイルを生成したい""".stripMargin)
  )

  it should "have a description" in {
    forAll(testPatterns) { (file: String, description: String) =>
      val parser = new DescriptionParser(loadDocument(file))
      parser.parse().value shouldBe description
    }
  }
}
