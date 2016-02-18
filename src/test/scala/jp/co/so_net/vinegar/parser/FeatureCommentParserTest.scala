package jp.co.so_net.vinegar.parser

import jp.co.so_net.vinegar.FixtureLoader
import org.scalatest.OptionValues._
import org.scalatest._

class FeatureCommentParserTest extends FlatSpec with Matchers with FixtureLoader {
  val featureText =
    """
      |Feature: フィーチャファイルからテスト実施結果報告書を生成する
      |  * テスト仕様書はレビューや差分確認が容易になるため、テキストファイルで作成したい
      |  * 実施と結果の報告はテキストファイルでは難しいのでひとまずエクセルでまとめたい
      |  * テスト仕様書から自動で結果報告ファイルを生成したい
      |
      |  # ここにコメントを書くと備考になります。
      |  # もちろん複数行のコメントを書くことも
      |  # 可能です。
    """.stripMargin

  val parser = new FeatureCommentParser {
    val feature = parseFeature(featureText)
  }

  it should "have a description" in {
    parser.featureComment.value shouldBe
      """ここにコメントを書くと備考になります。
        |もちろん複数行のコメントを書くことも
        |可能です。""".stripMargin
  }
}
