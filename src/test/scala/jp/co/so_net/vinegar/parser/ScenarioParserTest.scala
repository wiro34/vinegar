package jp.co.so_net.vinegar.parser

import jp.co.so_net.vinegar.FixtureLoader
import jp.co.so_net.vinegar.model._
import org.scalatest._

class ScenarioParserTest extends FlatSpec with Matchers with FixtureLoader {
  lazy val fullFeature =
    """
      |Feature: フィーチャファイルからテスト実施結果報告書を生成する
      |  * テスト仕様書はレビューや差分確認が容易になるため、テキストファイルで作成したい
      |  * 実施と結果の報告はテキストファイルでは難しいのでひとまずエクセルでまとめたい
      |  * テスト仕様書から自動で結果報告ファイルを生成したい
      |
      |  # ここにコメントを書くと備考になります。
      |  # もちろん複数のコメントを書くことも
      |  # 可能です。
      |
      |  Scenario: オプションで指定したディレクトリにファイルを出力する
      |    Given ホームディレクトリに移動する
      |    When "vinegar -o ~/Desktop /tmp/vinegar/example.feature" コマンドを実行する
      |    Then ~/Desktop に example.xlsx ファイルが存在すること
      |    And  /tmp/vinegar に example.xlsx ファイルが存在しないこと
      |    And  カレントディレクトリに example.xlsx ファイルが存在しないこと
      |
      |    When "vinegar /tmp/vinegar/example.feature --out /tmp/vinegar/path/to/deep/dir" コマンドを実行する
      |      # /tmp/vinegar/path/to/deep/dir は存在しないこと
      |      # 必要であれば rm -rf /tmp/vinegar/path/ で消しておく
      |    Then 以下のエラーが表示されること:
      |      '''
      |      /tmp/vinegar/path/to/deep/dir: No such file or directory
      |      '''
      |      # 日本語環境の場合はエラーメッセージが日本語になる場合もあります
      |    And  /tmp/vinegar/path/to/deep/dir に example.xlsx ファイルが存在しないこと
      |
      |    When "vinegar /tmp/vinegar/example.feature --out /tmp/vinegar/path/to/deep/dir -f" コマンドを実行する
      |    Then エラーが表示されず終了すること
      |    And  /tmp/vinegar/path/to/deep/dir に example.xlsx ファイルが存在すること
      |
      |  Scenario: ステップの存在しないシナリオも読み込まれること
      |
      |  @pending
      |  Scenario: @pending タグの付いたシナリオは読み込まれないこと
      |
      |  Scenario: 存在しないファイルを指定するとエラーを表示する
      |    When "vinegar /path/to/dummy.feature" コマンドを実行する
      |    Then 以下のエラーが表示されること:
      |    '''
      |    /path/to/dummy.feature: No such file or directory
      |    '''
    """.stripMargin.replaceAll("'''", "\"\"\"")

  lazy val emptyFeature =
    """
      |Feature: シナリオが存在しないフィーチャ
      |
      |  Background:
      |    Given fixture.feature を /tmp/vinegar にコピーする
      |    Given /tmp/vinegar/fixture.feature を UTF-8 にする
    """.stripMargin

  it should "parse scenarios" in {
    val parser = new ScenarioParser {
      val feature = parseFeature(fullFeature)
    }

    parser.scenarios.length shouldBe 3
    parser.scenarios(0).name shouldBe "オプションで指定したディレクトリにファイルを出力する"
    parser.scenarios(1).name shouldBe "ステップの存在しないシナリオも読み込まれること"
    parser.scenarios(2).name shouldBe "存在しないファイルを指定するとエラーを表示する"
  }

  "A scenario" should "have step groups" in {
    val parser = new ScenarioParser {
      val feature = parseFeature(fullFeature)
    }
    parser.scenarios(0).groups shouldBe Seq(
      GivenGroup(
        steps = Seq(
          Given("ホームディレクトリに移動する")
        ),
        children = Seq(WhenGroup(
          steps = Seq(When("\"vinegar -o ~/Desktop /tmp/vinegar/example.feature\" コマンドを実行する")),
          children = Seq(
            Then(text = "~/Desktop に example.xlsx ファイルが存在すること"),
            Then(text = "/tmp/vinegar に example.xlsx ファイルが存在しないこと"),
            Then(text = "カレントディレクトリに example.xlsx ファイルが存在しないこと")
          )
        ), WhenGroup(
          steps = Seq(
            When(
              text = "\"vinegar /tmp/vinegar/example.feature --out /tmp/vinegar/path/to/deep/dir\" コマンドを実行する",
              comment = Some("/tmp/vinegar/path/to/deep/dir は存在しないこと\n必要であれば rm -rf /tmp/vinegar/path/ で消しておく")
            )
          ),
          children = Seq(
            Then(
              text = "以下のエラーが表示されること:",
              argument = Some(DataText("/tmp/vinegar/path/to/deep/dir: No such file or directory")),
              comment = Some("日本語環境の場合はエラーメッセージが日本語になる場合もあります")
            ),
            Then(text = "/tmp/vinegar/path/to/deep/dir に example.xlsx ファイルが存在しないこと")
          )
        ), WhenGroup(
          steps = Seq(
            When(text = "\"vinegar /tmp/vinegar/example.feature --out /tmp/vinegar/path/to/deep/dir -f\" コマンドを実行する")
          ),
          children = Seq(
            Then(text = "エラーが表示されず終了すること"),
            Then(text = "/tmp/vinegar/path/to/deep/dir に example.xlsx ファイルが存在すること")
          )
        ))
      )
    )
  }

  it should "parse empty feature" in {
    val parser = new ScenarioParser {
      val feature = parseFeature(emptyFeature)
    }
    parser.scenarios.length shouldBe 0
  }
}
