package jp.co.so_net.vinegar.builder.excel

import jp.co.so_net.vinegar.model.{Background, Given, GivenGroup, Suite}
import org.scalatest.OptionValues._
import org.scalatest.{FlatSpec, Matchers}

class EnvironmentPartTest extends FlatSpec with Matchers {

  it should "add environment rows" in {
    val sheet = ExcelGenerator().emptyRow().story().description().background().featureComment().environment().build(Suite(name = ""))
    val storyRow = sheet.rows(1)
    val descriptionRow = sheet.rows(2)
    val backgroundRow = sheet.rows(3)

    storyRow.cells.toSeq(4).value shouldBe "環境"
    descriptionRow.cells.toSeq(4).value shouldBe "OS／端末"
    backgroundRow.cells.toSeq(4).value shouldBe "ブラウザ"
  }

}
