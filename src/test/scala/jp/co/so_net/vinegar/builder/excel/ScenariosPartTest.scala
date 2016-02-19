package jp.co.so_net.vinegar.builder.excel

import jp.co.so_net.vinegar.FixtureLoader
import jp.co.so_net.vinegar.parser.GherkinParser
import org.scalatest.{FlatSpec, Matchers}

class ScenariosPartTest extends FlatSpec with Matchers with FixtureLoader {
  val feature =
    """
      |Feature: サンプルフィーチャ
      |  ここは説明です。
      |
      |  # ここはコメントです。
      |
      |  Background:
      |   Given 前提その１
      |   Given 前提その２
      |
      |  Scenario: サンプルシナリオ１
      |    Given クッキーを削除する
      |      # コメントサンプルです。
      |    And   ログインページを開く
      |    When  forms_test01@nmt ユーザ用のランディング URL でランディングする
      |     # ID: test
      |     # PW: test1234
      |    When  ログインボタンをクリックする
      |    Then  トップページに遷移すること
      |    And   以下のメッセージが表示されていること:
      |      '''
      |      ログインに成功しました。
      |      '''
      |    Given マイページを開く
      |    When  プロフィールを開く
      |    Then  ユーザ名が表示されていること
      |    But   パスワードは表示されていないこと
      |    When  ログアウトボタンをクリックする
      |    Then  ログインページに遷移すること
      |
      |  Scenario: 空のシナリオ
      |
      |  Scenario: サンプルシナリオ２
      |    Given ステップ１
      |    When ステップ２
      |    Then ステップ３
    """.stripMargin.replaceAll("'''", "\"\"\"")

  val suite = GherkinParser.parse(feature).right.get
  val sheet = ExcelGenerator().scenarios().build(suite)

  it should "add a column header row" in {
    val row = sheet.rows.head
    val cells = row.cells.toSeq

    cells(0).value shouldBe "テストID"
    cells(1).value shouldBe "Given"
    cells(2).value shouldBe "When"
    cells(3).value shouldBe "Then"
    cells(4).value shouldBe "結果"
    cells(5).value shouldBe "確認者"
    cells(6).value shouldBe "確認日"
    cells(7).value shouldBe "備考"
  }

  it should "add scenario header rows" in {
    sheet.rows(1).cells.toSeq(0).value shouldBe "シナリオ #1"
    sheet.rows(1).cells.toSeq(1).value shouldBe "サンプルシナリオ１"
    sheet.rows(7).cells.toSeq(0).value shouldBe "シナリオ #2"
    sheet.rows(7).cells.toSeq(1).value shouldBe "空のシナリオ"
    sheet.rows(8).cells.toSeq(0).value shouldBe "シナリオ #3"
    sheet.rows(8).cells.toSeq(1).value shouldBe "サンプルシナリオ２"
  }

  it should "add step rows" in {
    sheet.rows(2).cells.toSeq(0).value shouldBe "01-001"
    sheet.rows(2).cells.toSeq(1).value shouldBe "クッキーを削除する\n# コメントサンプルです。\n\nログインページを開く"
    sheet.rows(2).cells.toSeq(2).value shouldBe "forms_test01@nmt ユーザ用のランディング URL でランディングする\n# ID: test\n# PW: test1234\n\nログインボタンをクリックする"
    sheet.rows(2).cells.toSeq(3).value shouldBe "トップページに遷移すること"

    sheet.rows(3).cells.toSeq(0).value shouldBe "01-002"
    sheet.rows(3).cells.toSeq(1).value shouldBe ""
    sheet.rows(3).cells.toSeq(2).value shouldBe ""
    sheet.rows(3).cells.toSeq(3).value shouldBe "以下のメッセージが表示されていること:\nログインに成功しました。"

    sheet.rows(4).cells.toSeq(0).value shouldBe "01-003"
    sheet.rows(4).cells.toSeq(1).value shouldBe "マイページを開く"
    sheet.rows(4).cells.toSeq(2).value shouldBe "プロフィールを開く"
    sheet.rows(4).cells.toSeq(3).value shouldBe "ユーザ名が表示されていること"

    sheet.rows(5).cells.toSeq(0).value shouldBe "01-004"
    sheet.rows(5).cells.toSeq(1).value shouldBe ""
    sheet.rows(5).cells.toSeq(2).value shouldBe ""
    sheet.rows(5).cells.toSeq(3).value shouldBe "パスワードは表示されていないこと"

    sheet.rows(6).cells.toSeq(0).value shouldBe "01-005"
    sheet.rows(6).cells.toSeq(1).value shouldBe ""
    sheet.rows(6).cells.toSeq(2).value shouldBe "ログアウトボタンをクリックする"
    sheet.rows(6).cells.toSeq(3).value shouldBe "ログインページに遷移すること"
  }

}

