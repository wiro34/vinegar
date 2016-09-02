package jp.co.so_net.vinegar.exporter.excel

import com.norbitltd.spoiwo.model.Sheet
import jp.co.so_net.vinegar.models.Feature
import org.scalatest.OptionValues._
import org.scalatest.{FlatSpec, Matchers}

class HasFeatureCommentImplTest extends FlatSpec with Matchers {
  val part = new HasFeatureCommentImpl {}

  it should "add a feature comment row" in {
    val featureComment =
      """ここはフィーチャ全体のコメントです。
        |備考欄にコピーされます。""".stripMargin
    val feature = Feature("", None, Some(featureComment), None, Nil)
    val sheet = part.featureComment(Sheet(), feature)
    val row = sheet.rows.head
    val cells = row.cells.toSeq

    cells(0).value shouldBe "備考"
    cells(1).value shouldBe featureComment
    row.height.value.toPoints shouldBe 32
  }

  it should "add an empty featureComment row when feature has no featureComment" in {
    val feature = Feature("", None, None, None, Nil)
    val sheet = part.featureComment(Sheet(), feature)
    val row = sheet.rows.head
    val cells = row.cells.toSeq

    cells(0).value shouldBe "備考"
    cells(1).value shouldBe ""
    row.height.value.toPoints shouldBe 16
  }
}
