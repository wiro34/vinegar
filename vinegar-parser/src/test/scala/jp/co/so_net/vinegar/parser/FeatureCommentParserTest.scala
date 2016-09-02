package jp.co.so_net.vinegar.parser

import jp.co.so_net.vinegar.GherkinLoader
import org.scalatest.OptionValues._
import org.scalatest._

class FeatureCommentParserTest extends FlatSpec with Matchers with GherkinLoader {
  val parser = new FeatureCommentParser(loadDocument("/description_ext.feature"))

  it should "have a feature comment" in {
    parser.parse().value shouldBe
      """ここにコメントを書くと備考になります。
        |もちろん複数行のコメントを書くことも
        |可能です。""".stripMargin
  }
}
