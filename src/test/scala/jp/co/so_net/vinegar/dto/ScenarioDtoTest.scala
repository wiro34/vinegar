package jp.co.so_net.vinegar.dto

import jp.co.so_net.vinegar.model.Suite
import org.scalatest._

class ScenarioDtoTest extends FlatSpec with Matchers with FeatureLoader {
  def suite = new ScenarioDto(feature).parseSuite(Suite(name = "test"))

  "Suite" should "have five scenarios" in {
    suite.scenarios.length should ===(5)
  }

  it should "transfar scenario names" in {
    suite.scenarios(0).name should ===("コマンドを実行して結果報告書を生成する")
    suite.scenarios(1).name should ===("オプションで指定したディレクトリにファイルを出力する")
    suite.scenarios(2).name should ===("存在しないファイルを指定するとエラーを表示する")
    suite.scenarios(3).name should ===("And や But が正しくパースされていること")
    suite.scenarios(4).name should ===("コメントは備考にコピーする")
  }
}

class StepDtoForEnTest extends SharedExamplesForStepDto {
  override val featureFile = "/example.feature"
}

class StepDtoForJaTest extends SharedExamplesForStepDto {
  override val featureFile = "/example.ja.feature"
}

abstract class SharedExamplesForStepDto extends FlatSpec with Matchers with FeatureLoader {
  def parseCases(scenarioIndex: Int) =
    new StepDto(feature, feature.getScenarioDefinitions.get(scenarioIndex), scenarioIndex + 1).parseSteps

  it should "transfar scenario cases" in {
    parseCases(0).length should ===(5)
    parseCases(1).length should ===(7)
    parseCases(2).length should ===(1)
    parseCases(3).length should ===(3)
    parseCases(5).length should ===(3)
  }

  it should "transfar scenario cases including Given, When, Then and And keywords" in {
    val cases = parseCases(0)

    cases(0).id should ===("0010001")
    cases(0).given should ===(Some("ホームディレクトリに移動する"))
    cases(0).when should ===(Some("\"vinegar /tmp/vinegar/example.feature\" を実行する"))
    cases(0).then should ===(Some("/tmp/vinegar に example.xlsx ファイルが存在すること"))

    cases(1).id should ===("0010002")
    cases(1).given should ===(None)
    cases(1).when should ===(None)
    cases(1).then should ===(Some("対象ストーリーに「フィーチャファイルからテスト実施結果報告書を生成する」と表示されていること"))

    cases(2).id should ===("0010003")
    cases(2).given should ===(None)
    cases(2).when should ===(None)
    cases(2).then should ===(Some(
      """目的／観点に以下が表示されていること:
        |* テスト仕様書はレビューや差分確認が容易になるため、テキストファイルで作成したい
        |* 実施と結果の報告はテキストファイルでは難しいのでひとまずエクセルでまとめたい
        |* テスト仕様書から自動で結果報告ファイルを生成したい""".stripMargin))

    cases(3).id should ===("0010004")
    cases(3).given should ===(None)
    cases(3).when should ===(None)
    cases(3).then should ===(Some(
      """事前準備に以下が表示されていること:
        |example.feature を /tmp/vinegar にコピーする
        |/tmp/vinegar/example.feature を UTF-8 にする""".stripMargin))

    cases(4).id should ===("0010005")
    cases(4).given should ===(None)
    cases(4).when should ===(None)
    cases(4).then should ===(Some("各シナリオとステップが表示されていること"))
    cases(4).note should ===(Some(
      """シナリオは以下の４つ
        |1. コマンドを実行して同名の結果報告書を生成する
        |2. オプションで指定したディレクトリにファイルを出力する
        |3. 存在しないファイルを指定するとエラーを表示する
        |4. コメントは備考にコピーする
        |※「ステップが存在しないシナリオは無視する」は表示されないこと
        |各ステップについては元のファイルと比較して不足がないか確認すること""".stripMargin))
  }

  it should "transfar scenario cases including But keyword" in {
    val cases = parseCases(3)

    cases(0).id should ===("0040001")
    cases(0).given should ===(None)
    cases(0).when should ===(Some(
      """ホームディレクトリに移動する
        |"vinegar -o ~/Desktop /tmp/vinegar/example.feature" コマンドを実行する
        |このテキストは上記２ステップと合わせて「When」に記述されていること""".stripMargin))
    cases(0).then should ===(Some("1. このテキストは「Then」に記述されていること"))

    cases(1).id should ===("0040002")
    cases(1).given should ===(None)
    cases(1).when should ===(None)
    cases(1).then should ===(Some("2. このテキストは 1. の次行の「Then」に記述されていること"))

    cases(2).id should ===("0040003")
    cases(2).given should ===(None)
    cases(2).when should ===(None)
    cases(2).then should ===(Some("3. このテキストは 3. の次行の「Then」に記述されていること"))
  }

  it should "transfar cases with comment" in {
    parseCases(1)(3).note should ===(Some(
      """/tmp/vinegar/path/to/deep/dir は存在しないこと
        |必要であれば rm -rf /tmp/vinegar/path/ で消しておく
        |日本語環境の場合はエラーメッセージが日本語になる場合もあります""".stripMargin))
    parseCases(5)(2).note should ===(Some(
      """ここはファイル末尾のコメントです
        |備考に入っていますか？""".stripMargin))
  }
}