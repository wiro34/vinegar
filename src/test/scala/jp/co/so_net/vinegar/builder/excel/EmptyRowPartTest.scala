package jp.co.so_net.vinegar.builder.excel

import com.norbitltd.spoiwo.model.{Row, Sheet}
import jp.co.so_net.vinegar.model.Suite
import org.scalatest.{FlatSpec, Matchers}

class EmptyRowPartTest extends FlatSpec with Matchers {
  it should "add an empty row" in {
    val sheet = ExcelGenerator().emptyRow().build(Suite(name = ""))
    sheet shouldBe Sheet(name = "テスト仕様書", rows = List(Row()))
  }
}
