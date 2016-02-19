package jp.co.so_net.vinegar.builder.excel

import jp.co.so_net.vinegar.model.Suite
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.OptionValues._

class DescriptionPartTest extends FlatSpec with Matchers {
  it should "add a description row" in {
    val description =
      """* ○○として、
        |* △△がほしい。
        |* なぜならば、××だからだ。""".stripMargin
    val suite = Suite(name = "", description = Some(description))
    val sheet = ExcelGenerator().description().build(suite)
    val row = sheet.rows.head
    val cells = row.cells.toSeq

    cells(0).value shouldBe "目的／観点"
    cells(1).value shouldBe description
    row.height.value.toPoints shouldBe 48
  }

  it should "add an empty description row when suite has no description" in {
    val suite = Suite(name = "", description = None)
    val sheet = ExcelGenerator().description().build(suite)
    val row = sheet.rows.head
    val cells = row.cells.toSeq

    cells(0).value shouldBe "目的／観点"
    cells(1).value shouldBe ""
    row.height.value.toPoints shouldBe 16
  }
}
