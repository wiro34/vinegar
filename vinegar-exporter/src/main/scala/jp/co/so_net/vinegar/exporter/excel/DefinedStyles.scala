package jp.co.so_net.vinegar.exporter.excel

import com.norbitltd.spoiwo.model._
import com.norbitltd.spoiwo.model.enums.{CellBorderStyle, CellFill, CellHorizontalAlignment, CellVerticalAlignment}

private[excel]
object DefinedStyles {
  val metaRange = Range(2, 4)

  val lineHeight = 16

  val font = Font(fontName = "メイリオ", height = 9)

  val solidBlackBorder = CellBorders(
    leftStyle = CellBorderStyle.Thin, leftColor = Color.Black,
    topStyle = CellBorderStyle.Thin, topColor = Color.Black,
    rightStyle = CellBorderStyle.Thin, rightColor = Color.Black,
    bottomStyle = CellBorderStyle.Thin, bottomColor = Color.Black
  )

  val caseCellBorder = CellBorders(
    leftStyle = CellBorderStyle.None,
    topStyle = CellBorderStyle.None,
    rightStyle = CellBorderStyle.None,
    bottomStyle = CellBorderStyle.None
  )

  val centerStyle = CellStyle(verticalAlignment = CellVerticalAlignment.Center)

  val wrappedStyle = CellStyle(wrapText = true)

  val headerCellStyle = CellStyle(
    borders = solidBlackBorder,
    fillPattern = CellFill.Solid,
    fillForegroundColor = Color(0xd0, 0xe1, 0xf3),
    horizontalAlignment = CellHorizontalAlignment.Center,
    verticalAlignment = CellVerticalAlignment.Center,
    font = font
  )

  val valueCellStyle = CellStyle(
    borders = solidBlackBorder,
    verticalAlignment = CellVerticalAlignment.Center,
    font = font,
    wrapText = true
  )
  val centeredValueCellStyle = valueCellStyle.withHorizontalAlignment(CellHorizontalAlignment.Center)

  val scenarioHeaderStyle = headerCellStyle.withFillForegroundColor(Color(0xda, 0xee, 0xf3))

  val scenarioNameCellStyle = CellStyle(
    verticalAlignment = CellVerticalAlignment.Center,
    font = font,
    wrapText = false,
    fillForegroundColor = Color(0xda, 0xee, 0xf3),
    fillPattern = CellFill.Solid
  )
  val scenarioNameLastCellStyle = scenarioNameCellStyle.withBorders(CellBorders(
    leftStyle = CellBorderStyle.None,
    topStyle = CellBorderStyle.None,
    rightStyle = CellBorderStyle.Thin,
    bottomStyle = CellBorderStyle.None
  ))

  val caseIdCellStyle = headerCellStyle

  val caseCellStyle = valueCellStyle.withVerticalAlignment(CellVerticalAlignment.Top)

  val dateValueCellStyle = centeredValueCellStyle.withDataFormat(CellDataFormat("mm/dd"))

  val noteCellStyle = caseCellStyle.withoutWrapText

  import scala.language.implicitConversions

  implicit def toWidth(points: Int): Width = new Width(points, WidthUnit.Character)

  implicit def toHeight(points: Int): Height = new Height(points, HeightUnit.Point)

  implicit def convertIntToLineHeight(lines: Int): LineHeight = new LineHeight(lines)

  class LineHeight(n: Int) {
    def lines = n * lineHeight
  }

}
