package jp.co.so_net.vinegar.builder.excel

import jp.co.so_net.vinegar.model.Suite
import org.scalatest.{FlatSpec, Matchers}

class StoryPartTest extends FlatSpec with Matchers {
  it should "add a story row" in {
    val suite = Suite(name = "サンプルテスト")
    val sheet = ExcelGenerator().story().build(suite)
    sheet.rows.head.cells.head.value shouldBe "対応ストーリー"
    sheet.rows.head.cells.toSeq(1).value shouldBe "サンプルテスト"
  }
}
