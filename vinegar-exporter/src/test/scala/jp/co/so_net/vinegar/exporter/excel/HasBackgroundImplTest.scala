package jp.co.so_net.vinegar.exporter.excel

import com.norbitltd.spoiwo.model.Sheet
import jp.co.so_net.vinegar.models._
import org.scalatest.OptionValues._
import org.scalatest.{FlatSpec, Matchers}

class HasBackgroundImplTest extends FlatSpec with Matchers {

  val backgroundPart = new HasBackgroundImpl {}

  it should "add a background row when feature has a background" in {
    val feature = Feature(
      name = "test",
      description = None,
      comment = None,
      background = Some(Background(steps = Seq(Given(text = "ログイン画面を開く"),
        Given(text = "ユーザIDとパスワードを入力する"),
        Given(
          text = "ログインボタンをクリックする:",
          argument = Some(DataTable(
            header = TableRow(Seq(TableCell("hoge"), TableCell("foo"))),
            rows = Seq(TableRow(Seq(TableCell("a"), TableCell("b"))), TableRow(Seq(TableCell("ほげ"), TableCell("ふーさん"))))
          ))
        )))),
      scenarioDefinitions = Nil
    )

    val sheet = backgroundPart.background(Sheet(), feature)
    val row = sheet.rows.head
    val cells = row.cells.toSeq

    cells(0).value shouldBe "事前準備"
    cells(1).value shouldBe
      """ログイン画面を開く
        |ユーザIDとパスワードを入力する
        |ログインボタンをクリックする:
        |    | hoge | foo      |
        |    | a    | b        |
        |    | ほげ | ふーさん |""".stripMargin
    row.height.value.toPoints shouldBe 96
  }

  it should "add an empty background row when feature has no background" in {
    val feature = Feature(name = "", description = None, comment = None, background = None, scenarioDefinitions = Nil)
    val sheet = backgroundPart.background(Sheet(), feature)
    val row = sheet.rows.head
    val cells = row.cells.toSeq

    cells(0).value shouldBe "事前準備"
    cells(1).value shouldBe ""
    row.height.value.toPoints shouldBe 16
  }
}
