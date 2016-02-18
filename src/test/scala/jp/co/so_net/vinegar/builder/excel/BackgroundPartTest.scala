package jp.co.so_net.vinegar.builder.excel

import jp.co.so_net.vinegar.model.{Given, GivenGroup, Background, Suite}
import org.scalatest.OptionValues._
import org.scalatest.{FlatSpec, Matchers}

class BackgroundPartTest extends FlatSpec with Matchers {
  it should "add a background row when suite has a background" in {
    val background = Background(groups = Seq(GivenGroup(steps = Seq(
      Given(text = "ログイン画面を開く"),
      Given(text = "ユーザIDとパスワードを入力する"),
      Given(text = "ログインボタンをクリックする")
    ))))
    val suite = Suite(name = "", background = Some(background))
    val sheet = ExcelGenerator().background().build(suite)
    val row = sheet.rows.head
    val cells = row.cells.toSeq

    cells(0).value shouldBe "事前準備"
    cells(1).value shouldBe
      """ログイン画面を開く
        |ユーザIDとパスワードを入力する
        |ログインボタンをクリックする""".stripMargin
    row.height.value.toPoints shouldBe 48
  }

  it should "add an empty background row when suite has no background" in {
    val suite = Suite(name = "", background = None)
    val sheet = ExcelGenerator().background().build(suite)
    val row = sheet.rows.head
    val cells = row.cells.toSeq

    cells(0).value shouldBe "事前準備"
    cells(1).value shouldBe ""
    row.height.value.toPoints shouldBe 16
  }
}
