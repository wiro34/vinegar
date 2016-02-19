package jp.co.so_net.vinegar.builder.excel

import jp.co.so_net.vinegar.model.Suite
import org.scalatest.OptionValues._
import org.scalatest.{FlatSpec, Matchers}

class FeatureCommentPartTest extends FlatSpec with Matchers {
  it should "add a feature comment row" in {
    val featureComment =
      """ここはフィーチャ全体のコメントです。
        |備考欄にコピーされます。""".stripMargin
    val suite = Suite(name = "", comment = Some(featureComment))
    val sheet = ExcelGenerator().featureComment().build(suite)
    val row = sheet.rows.head
    val cells = row.cells.toSeq

    cells(0).value shouldBe "備考"
    cells(1).value shouldBe featureComment
    row.height.value.toPoints shouldBe 32
  }

  it should "add an empty featureComment row when suite has no featureComment" in {
    val suite = Suite(name = "", comment = None)
    val sheet = ExcelGenerator().featureComment().build(suite)
    val row = sheet.rows.head
    val cells = row.cells.toSeq

    cells(0).value shouldBe "備考"
    cells(1).value shouldBe ""
    row.height.value.toPoints shouldBe 16
  }
}
