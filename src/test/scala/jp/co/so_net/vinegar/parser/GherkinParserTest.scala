package jp.co.so_net.vinegar.parser

import jp.co.so_net.vinegar.model
import jp.co.so_net.vinegar.model._
import org.scalatest.OptionValues._
import org.scalatest._

class GherkinParserTest extends FlatSpec with Matchers with Inside {
  val validFeature =
    """
      |Feature: 正しいフィーチャ
      |  ここは説明です。
      |
      |  # ここはコメントです。
      |
      |  Background:
      |   Given 前提その１
      |   Given 前提その２
      |
      |  Scenario: シナリオ１
      |    Given ログインページを開く
      |    When ログインボタンをクリックする
      |    Then トップページに遷移すること
      |    And  ユーザ名が表示されていること
      |    When ログアウトボタンをクリックする
      |    Then ログイン画面に遷移すること
      |
      |  Scenario: シナリオ２
      |    Given 管理ページを開く
      |    When ログインする
      |    Then ユーザ一覧が表示されていること
    """.stripMargin

  val invalidFeature =
    """
      |Feature: 不正なフィーチャ
      |  ここは説明です
      |
      |  Scenario: シナリオ
      |    Given ログインページを開く
      |    Whan ログインボタンをクリックする(typo)
      |    Then マイページに遷移すること
    """.stripMargin

  it should "return result when parsing is succeeded" in {
    val result = GherkinParser.parse(validFeature)
    val suite = result.right.get

    inside(suite) {
      case Suite(name, description, comment, background, scenarios) =>
        name shouldBe "正しいフィーチャ"
        description.value shouldBe "ここは説明です。"
        comment.value shouldBe "ここはコメントです。"

        inside(background) {
          case Some(Background(groups)) =>
            groups shouldBe Seq(GivenGroup(
              steps = Seq(Given(text = "前提その１"), Given(text = "前提その２"))
            ))
        }

        inside(scenarios) {
          case Seq(scenario1, scenario2) =>
            scenario1.name shouldBe "シナリオ１"
            scenario2.name shouldBe "シナリオ２"
        }
    }
  }

  it should "return exception when feature is invalid" in {
    val result = GherkinParser.parse(invalidFeature)
    result shouldBe a[Left[_, _]]
  }
}
