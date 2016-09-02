package jp.co.so_net.vinegar.exporter.excel

import com.norbitltd.spoiwo.model.Sheet
import jp.co.so_net.vinegar.exporter._
import jp.co.so_net.vinegar.models.Feature

trait ExcelExporter extends Exporter[Sheet] {
  def export(feature: Feature): Sheet = {
    val sheet = Sheet(name = "テスト仕様書")
    Seq(
      emptyRow,
      this.featureTitle,
      description,
      background,
      featureComment,
      emptyRow,
      scenarios
    ).foldLeft(sheet) { (sheet, pf) => pf(sheet, feature) }
  }
}

trait UsesExcelExporter {
  val ExcelExporter: Exporter[Sheet]
}

trait MixinExcelExporter extends UsesExcelExporter {
  val ExcelExporter = new ExcelExporter
    with HasFeatureTitleImpl
    with HasBackgroundImpl
    with HasDescriptionImpl
    with HasFeatureCommentImpl
    with HasScenariosImpl
    with HasEmptyRowImpl
}

