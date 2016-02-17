package jp.co.so_net.vinegar.generator

import com.norbitltd.spoiwo.model.Sheet
import jp.co.so_net.vinegar.model.Suite

trait Generator[T] {
  def generate(suite: Suite): T
}

class ExcelGenerator2 extends Generator[Sheet] {

  val generators = Seq(
    EmptyRowGenerator
  )

  def generate(suite: Suite): Sheet = generators.foldLeft(Sheet(name = "テスト仕様書")) { (sheet, generator) =>
    generator.generate(sheet)
  }
}
