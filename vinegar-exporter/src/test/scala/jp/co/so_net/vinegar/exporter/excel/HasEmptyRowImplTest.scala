package jp.co.so_net.vinegar.exporter.excel

import com.norbitltd.spoiwo.model.{Row, Sheet}
import jp.co.so_net.vinegar.models.Feature
import org.scalatest.{FlatSpec, Matchers}

class HasEmptyRowImplTest extends FlatSpec with Matchers {
  val part = new HasEmptyRowImpl {}

  it should "add an empty row" in {
    val sheet = part.emptyRow(Sheet(), Feature("", None, None, None, Nil))
    sheet shouldBe Sheet(rows = List(Row()))
  }
}
