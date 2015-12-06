package jp.co.so_net.vinegar.facade.exceljs

import jp.co.so_net.vinegar.facade.GlobalExportedModule

import scala.scalajs.js
import scala.scalajs.js.{Date, `|`}

@js.native
trait ExcelJS extends js.Object

object ExcelJS extends GlobalExportedModule[ExcelJS]("exceljs") {
  def newInstance[T <: js.Object](clazz: js.Dynamic): T =
    js.Dynamic.newInstance(clazz)().asInstanceOf[T]
}

@js.native
trait Workbook extends js.Object {
  val xlsx: XLSX = js.native

  def addWorksheet(name: String): Worksheet = js.native

  def getWorksheet(indexOrName: String | Int): Worksheet = js.native
}

@js.native
trait XLSX extends js.Object {
  def writeFile(filename: String): Unit = js.native
}

@js.native
trait Worksheet extends js.Object {
  var columns: js.Array[Column] = js.native

  def mergeCells(cells: String): Unit = js.native

  def addRow(row: js.Array[String] | js.Object): Row = js.native

  def getRow(index: Int): Row = js.native

  def getCell(ref: String): Cell = js.native

}

@js.native
trait Row extends js.Object {
  var height: Double = js.native

  val _number: Int = js.native
  val _cells: js.Any = js.native

  def getCell(ref: Int | String): Cell = js.native

  def eachCell(callback: js.Function1[Cell, Any]): Unit = js.native

  def eachCell(option: js.Object, callback: js.Function1[Cell, Any]): Unit = js.native
}

@js.native
trait Cell extends js.Object with ColumnStyle {
  var value: String | Number | Date = js.native
  val _column: Column = js.native
}

@js.native
trait Alignment extends js.Object {
  val vertical: String = js.native
  val horizontal: String = js.native
  val wrapText: Boolean = js.native
}

object Alignment {
  def apply(vertical: String = "top", horizontal: String = "left", wrapText: Boolean = false): Alignment =
    js.Dynamic.literal("vertical" -> vertical, "horizontal" -> horizontal, "wrapText" -> wrapText).asInstanceOf[Alignment]
}

@js.native
trait BorderStyle extends js.Object {
  val style: String = js.native
  val color: Color = js.native
}

object BorderStyle {
  def apply(style: String, color: Color = null): BorderStyle =
    js.Dynamic.literal("style" -> style, "color" -> color).asInstanceOf[BorderStyle]
}

@js.native
trait CellBorder extends js.Object {
  val top: BorderStyle = js.native
  val left: BorderStyle = js.native
  val right: BorderStyle = js.native
  val bottom: BorderStyle = js.native
}

object CellBorder {
  def apply(top: BorderStyle = null,
            left: BorderStyle = null,
            right: BorderStyle = null,
            bottom: BorderStyle = null): CellBorder =
    js.Dynamic.literal("top" -> top, "left" -> left, "right" -> right, "bottom" -> bottom).asInstanceOf[CellBorder]
}

@js.native
trait Color extends js.Object {
  val argb: String = js.native
}

object Color {
  def apply(argb: String): Color =
    js.Dynamic.literal("argb" -> argb).asInstanceOf[Color]
}

@js.native
trait Column extends js.Object {
  val key: String = js.native
  val width: Int = js.native
}

object Column {
  def apply(key: String, width: Int, style: ColumnStyle = null): Column =
    js.Dynamic.literal("key" -> key, "width" -> width, "style" -> style).asInstanceOf[Column]

}

@js.native
trait ColumnStyle extends js.Object {
  val font: Font = js.native
  var fill: FillStyle = js.native
  var border: CellBorder = js.native
  var alignment: Alignment = js.native
}

object ColumnStyle {
  def apply(font: Font = null,
            fill: FillStyle = null,
            border: CellBorder = null,
            alignment: Alignment = null): ColumnStyle =
    js.Dynamic.literal(
      "font" -> font,
      "fill" -> fill,
      "border" -> border,
      "alignment" -> alignment
    ).asInstanceOf[ColumnStyle]
}

@js.native
trait FillStyle extends js.Object {
  val `type`: String = js.native
  val pattern: String = js.native
  val fgColor: Color = js.native
}

object FillStyle {
  def apply(`type`: String, pattern: String, fgColor: Color): FillStyle =
    js.Dynamic.literal("type" -> `type`, "pattern" -> pattern, "fgColor" -> fgColor).asInstanceOf[FillStyle]
}

@js.native
trait Font extends js.Object {
  val name: String = js.native
  val family: Int = js.native
  //  val charset: Int = js.native
  val size: Int = js.native
  val color: Color = js.native
  val underline: Boolean = js.native
  val bold: Boolean = js.native
  val italic: Boolean = js.native
}

object Font {
  def apply(name: String,
            family: Int = 1,
            size: Int = 11,
            color: Color = Color("FF000000"),
            underline: Boolean = false,
            bold: Boolean = false,
            italic: Boolean = false): Font = {
    js.Dynamic.literal(
      "name" -> name,
      //      "family" -> family,
      "size" -> size,
      "color" -> color,
      "underline" -> underline,
      "bold" -> bold,
      "italic" -> italic
    ).asInstanceOf[Font]
  }
}