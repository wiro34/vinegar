package jp.co.so_net.vinegar.parser

import jp.co.so_net.vinegar.GherkinLoader
import org.scalatest.OptionValues._
import org.scalatest._

class CommentScannerTest extends FlatSpec with Matchers with GherkinLoader {
    val featureText =
      """
        |Feature: コメントのテスト
        |  フィーチャの説明文
        |  です。
        |
        |  # フィーチャのコメント
        |  #
        |  # テスト
        |
        |  # 空行もOK
        |
        |  Background:
        |    Given 背景のコメントはどうなる？
        |      # Background のコメントだよ
        |      #       # 以降の空白は1文字だけ無視するよ
        |
        |  Scenario: シナリオ #1
        |    Given シナリオの Given のコメントは？
        |      # Given のコメント
        |    When シナリオの When のコメントは？
        |      # When のコメント
        |
        |  Scenario Outline: シナリオ #2
        |    Given コメントの間にパラメータを挟むパターン
        |      # ここは Given のコメントです。
        |      ```
        |      DocString を挟みます
        |      ```
        |      # ここも Given のコメントです。
        |    Then Example があるとどうかな？
        |      # Then のコメント
        |      # Example <foo> 付き
        |    Then テーブルとの組み合わせ:
        |      | username | password |
        |      | hoge     | secret   |
        |
        |      # テーブルのコメントだよ。
        |
        |    Examples:
        |      | foo   |
        |      | test1 |
        |      | test2 |
        |
        |  Scenario: シナリオ #3
        |    Then 最後のステップ
        | # インデントがずれてる
        |      # ファイル末尾のコメントだよ。
      """.stripMargin

  val document = parseDocument(featureText)
  val scanner = new CommentScanner(document)
  val feature = document.getFeature

  it should "scan feature comment" in {
    scanner.scan(feature).value shouldBe "フィーチャのコメント\n\nテスト\n空行もOK"
  }

  it should "scan background comment" in {
    val backgroundSteps = feature.getChildren.get(0).getSteps
    scanner.scan(backgroundSteps.get(0)).value shouldBe "Background のコメントだよ\n      # 以降の空白は1文字だけ無視するよ"
  }

  it should "scan scenario comment" in {
    val firstScenarioSteps = feature.getChildren.get(1).getSteps
    scanner.scan(firstScenarioSteps.get(0)).value shouldBe "Given のコメント"
    scanner.scan(firstScenarioSteps.get(1)).value shouldBe "When のコメント"

    val secondScenarioSteps = feature.getChildren.get(2).getSteps
    scanner.scan(secondScenarioSteps.get(0)).value shouldBe "ここは Given のコメントです。\nここも Given のコメントです。"
    scanner.scan(secondScenarioSteps.get(1)).value shouldBe "Then のコメント\nExample <foo> 付き"
    scanner.scan(secondScenarioSteps.get(2)).value shouldBe "テーブルのコメントだよ。"

    val thirdScenarioSteps = feature.getChildren.get(3).getSteps
    scanner.scan(thirdScenarioSteps.get(0)).value shouldBe "インデントがずれてる\nファイル末尾のコメントだよ。"
  }
}
