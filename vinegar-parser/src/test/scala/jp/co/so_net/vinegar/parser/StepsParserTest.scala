package jp.co.so_net.vinegar.parser

import jp.co.so_net.vinegar.GherkinLoader
import jp.co.so_net.vinegar.models._
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.JavaConversions._

class StepsParserTest extends FlatSpec with Matchers with GherkinLoader {

  val testPatterns = Table(
    ("file", "scenarioIndex", "steps"),

    ("/fixture.feature", 1, Seq(
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

    ("/fixture.feature", 2, Seq(
      Given(text = "ログイン画面を表示する"),
      When(text = "ユーザ名に <username> と入力する"),
      When(text = "パスワードに <password> と入力する"),
      When(text = "\"ログイン\" ボタンをクリックする"),
      Then(text = "<message> と表示されていること")
    ))
  )

  it should "parse scenario steps" in {
    forAll(testPatterns) { (file: String, scenarioIndex: Int, expectedSteps: Seq[Step]) =>
      val document = loadDocument(file)
      val feature = document.getFeature
      val steps = feature.getChildren.get(scenarioIndex).getSteps.toList
      new StepsParser(document).parse(steps) shouldBe expectedSteps
    }
  }
}
