package jp.co.so_net.vinegar.parser

import org.scalamock.scalatest.MockFactory
import org.scalatest._

class GherkinParserTest extends FlatSpec with Matchers with MockFactory with BeforeAndAfter {
  val validFeature =
    """
      |Feature: 正しいフィーチャ
      |  ここは説明です
      |
      |  Scenario: シナリオ
      |    Given ログインページを開く
      |    When ログインボタンをクリックする
      |    Then マイページに遷移すること
    """.stripMargin

  val invalidFeature =
    """
      |Feature: 不正なフィーチャ
      |  ここは説明です
      |
      |  Scenario: シナリオ
      |    Given ログインページを開く
      |    Whan ログインボタンをクリックする
      |    Then マイページに遷移すること
    """.stripMargin

  it should "return result when parsing is succeeded" in {
    val result = GherkinParser.parse(validFeature)
    result shouldBe a[Right[_, _]]
  }

  it should "return exception when feature is invalid" in {
    val result = GherkinParser.parse(invalidFeature)
    result shouldBe a[Left[_, _]]
  }

}
