package jp.co.so_net.vinegar.exporter

import jp.co.so_net.vinegar.models.Feature

sealed trait ExporterPart[T] {
  type PartFunction = (T, Feature) => T
}

trait HasFeatureTitle[T] extends ExporterPart[T] {
  def featureTitle: PartFunction
}

trait HasDescription[T] extends ExporterPart[T] {
  def description: PartFunction
}

trait HasBackground[T] extends ExporterPart[T] {
  def background: PartFunction
}

trait HasFeatureComment[T] extends ExporterPart[T] {
  def featureComment: PartFunction
}

trait HasScenarios[T] extends ExporterPart[T] {
  def scenarios: PartFunction
}

trait HasEmptyRow[T] extends ExporterPart[T] {
  def emptyRow: PartFunction
}
