package jp.co.so_net.vinegar.exporter

import jp.co.so_net.vinegar.exporter.excel.MixinExcelExporter
import jp.co.so_net.vinegar.models.Feature

trait Exporter[T] extends HasFeatureTitle[T]
  with HasBackground[T]
  with HasDescription[T]
  with HasFeatureComment[T]
  with HasScenarios[T]
  with HasEmptyRow[T] {

  def export(feature: Feature): T
}

object Exporters extends MixinExcelExporter {}
