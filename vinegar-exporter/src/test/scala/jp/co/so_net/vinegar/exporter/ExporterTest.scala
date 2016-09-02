package jp.co.so_net.vinegar.exporter

import java.nio.file.{Files, Paths}

import jp.co.so_net.vinegar.models._
import org.scalatest.{EitherValues, _}

class ExporterTest extends FlatSpec with Matchers with EitherValues {

  import Exporters.ExcelExporter

  val feature = Feature(
    name = "テストフィーチャ",
    description = Some("フィーチャの説明文は「目的／観点」に設定されます。\nもちろん複数行も可能です。\nインデントは最短マッチで削除されます。"),
    background = Some(Background(Seq(
      Given(text = "Background には事前準備を記述します。"),
      Given(text = "ユーザを作成する、初期データを投入するなどテスト実施前に必要なステップを記述してください。")
    ))),
    comment = Some("フィーチャ直下にコメントを書くと備考になります。\n必要に応じて使い分けてください。"),
    scenarioDefinitions = Seq(
      Scenario(name = "シナリオ", steps = Seq(
        Given(text = "これは Given ステップです。",
          comment = Some("Given ステップのコメントはステップ内に改行して記入されます。")),
        When(text = "これは When ステップです。"),
        When(text = "When ステップが複数あると、Given が行結合されて横に並びます。",
          comment = Some("When のコメントも各ステップ内に改行して記入されます。")),
        Then(text = "これは Then ステップです。"),
        Then(text = "もちろん、複数行テキストも使えます:",
          argument = Some(DocString("複数行テキストやテーブルを使う場合はステップの末尾を : にします。\n長い文字列や、複数行に渡る入力値を表現するのに便利です。")),
          comment = Some("パラメータ付きのステップにコメントを付けることも可能です\nThen ステップのコメントは「備考」欄に記入されます")),
        Then(text = "複数行テキストは \"\"\" もしくは ``` で囲みます:",
          argument = Some(DocString("こんな感じです。"))),
        Given(text = "これは２つ目の Given ステップです。"),
        Then(text = "When をすっ飛ばすことも可能です。",
          comment = Some("例えば、ページを開いたら〜が表示されていること、なんてテストは When を使わないかもしれません。")),
        When(text = "テーブルも利用可能です"),
        When(text = "ユーザを作成する:",
          argument = Some(DataTable(
            header = TableRow(Seq(TableCell("username"), TableCell("password"))),
            rows = Seq(
              TableRow(Seq(TableCell("test01"), TableCell("pass01"))),
              TableRow(Seq(TableCell("test02"), TableCell("pass02"))),
              TableRow(Seq(TableCell("hoge"), TableCell("foobar")))
            ))),
          comment = Some("メールアドレスは適当で可")),
        When(text = "\"test01\" ユーザでログインする"),
        Then(text = "ログインできること"),
        Then(text = "ただし〜できないこと、ただし〜でないことなどは But を使います。")
      )),

      ScenarioOutline(name = "シナリオアウトラインのサンプル", steps = Seq(
        Given(text = "ログイン画面を表示する"),
        When(text = "ユーザ名に <username> と入力する"),
        When(text = "パスワードに <password> と入力する"),
        When(text = "\"ログイン\" ボタンをクリックする"),
        Then(text = "<message> と表示されていること")
      ),
        examples = Seq(Example(
          header = TableRow(Seq(TableCell("username"), TableCell("password"), TableCell("message"))),
          rows = Seq(
            TableRow(Seq(TableCell("invalid"), TableCell("pass01"), TableCell("ユーザ名、またはパスワードが違います。"))),
            TableRow(Seq(TableCell("test01"), TableCell("invalid"), TableCell("ユーザ名、またはパスワードが違います。"))),
            TableRow(Seq(TableCell("expired"), TableCell("pass01"), TableCell("アカウントの有効期限が切れています。"))),
            TableRow(Seq(TableCell("freezed"), TableCell("pass01"), TableCell("あなたのアカウントは現在凍結されています。")))
          )
        ))
      )
    )
  )

  val tmpDir = Paths.get(System.getProperty("java.io.tmpdir"))
  val outfile = tmpDir.resolve("example.xlsx").toString

  it should "write file" in {
    val sheet = ExcelExporter.export(feature)
    FileWriters.ExcelFileWriter.write(outfile, sheet)
    Files.exists(Paths.get(outfile)) shouldBe true
  }
}
