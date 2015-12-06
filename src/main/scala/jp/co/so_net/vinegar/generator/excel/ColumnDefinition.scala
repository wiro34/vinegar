package jp.co.so_net.vinegar.generator.excel

import jp.co.so_net.vinegar.facade.exceljs._

import scala.scalajs.js

@js.native
trait ColumnDefinition extends js.Object {
  val id: String
  val given: String
  val when: String
  val expected: String
  val result: String
  val who: String
  val date: String
  val device: String
  val browser: String
  val note: String
}

object ColumnDefinition {
  def apply(id: String = "",
            given: String = "",
            when: String = "",
            expected: String = "",
            result: String = "",
            who: String = "",
            date: String = "",
            device: String = "",
            browser: String = "",
            note: String = ""): ColumnDefinition =
    js.Dynamic.literal(
      "id" -> id,
      "given" -> given,
      "when" -> when,
      "expected" -> expected,
      "result" -> result,
      "who" -> who,
      "date" -> date,
      "device" -> device,
      "browser" -> browser,
      "note" -> note
    ).asInstanceOf[ColumnDefinition]

  def defineColumns(sheet: Worksheet): Unit = {
    val font = Font(name = "メイリオ", size = 10)
    val centerStyle = ColumnStyle(
      alignment = Alignment(horizontal = "center", vertical = "middle"),
      font = font
    )
    val wrappedStyle = ColumnStyle(
      alignment = Alignment(wrapText = true),
      font = font
    )

    sheet.columns = js.Array(
      Column(key = "id", width = 13, style = centerStyle),
      Column(key = "given", width = 40, style = wrappedStyle),
      Column(key = "when", width = 40, style = wrappedStyle),
      Column(key = "expected", width = 50, style = wrappedStyle),
      Column(key = "result", width = 10, style = centerStyle),
      Column(key = "who", width = 10, style = centerStyle),
      Column(key = "date", width = 10, style = centerStyle),
      Column(key = "device", width = 10, style = centerStyle),
      Column(key = "browser", width = 10, style = centerStyle),
      Column(key = "note", width = 40, style = wrappedStyle)
    )
  }
}
