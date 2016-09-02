package jp.co.so_net.vinegar.exporter.excel

import com.norbitltd.spoiwo.model.Sheet
import jp.co.so_net.vinegar.models.Feature
import org.scalatest.{FlatSpec, Matchers}

class FeaturePartTest extends FlatSpec with Matchers {
  val part = new HasFeatureTitleImpl {}

  it should "add a story row" in {
    val feature = Feature("サンプルテスト", None, None, None, Nil)
    val sheet = part.featureTitle(Sheet(), feature)
    sheet.rows.head.cells.head.value shouldBe "対応ストーリー"
    sheet.rows.head.cells.toSeq(1).value shouldBe "サンプルテスト"
  }
}
