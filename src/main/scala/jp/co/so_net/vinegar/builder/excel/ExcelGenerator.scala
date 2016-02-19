package jp.co.so_net.vinegar.builder.excel

import com.norbitltd.spoiwo.model.Sheet
import jp.co.so_net.vinegar.builder.Builder
import jp.co.so_net.vinegar.model.Suite

private[builder] object ExcelGenerator {
  def apply() = new ExcelGenerator(Nil)
}

private[builder] class ExcelGenerator(protected[this] val parts: Seq[(Sheet, Suite) => Sheet])
  extends Builder[Sheet] with EmptyRowPart with StoryPart with DescriptionPart
  with BackgroundPart with FeatureCommentPart with EnvironmentPart with ScenariosPart {

  type This = ExcelGenerator

  def build(suite: Suite): Sheet = {
    val sheet = Sheet(name = "テスト仕様書")
    parts.foldLeft(sheet) { (sheet, pf) => pf(sheet, suite) }
  }
}
