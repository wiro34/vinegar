package jp.co.so_net.vinegar.builder

import jp.co.so_net.vinegar.model.Suite

trait Builder[T] {
  def build(suite: Suite): T
}

object Builder {
  val ExcelGenerator = excel.ExcelGenerator()
    .emptyRow()
    .story()
    .description()
    .background()
    .featureComment()
    .environment()
    .emptyRow()
    .scenarios()
}
