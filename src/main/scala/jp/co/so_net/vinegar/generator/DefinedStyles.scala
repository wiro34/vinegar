package jp.co.so_net.vinegar.generator

import com.norbitltd.spoiwo.model.enums.{CellHorizontalAlignment, CellFill, CellVerticalAlignment, CellBorderStyle}
import com.norbitltd.spoiwo.model._

object DefinedStyles {
  val metaRange = Range(2, 9)

  val lineHeight = 20

  val font = Font(fontName = "メイリオ", height = 10)

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

  val scenarioHeaderStyle = headerCellStyle.withFillForegroundColor(Color(0xd0, 0xf3, 0xe1))

  val scenarioNameCellStyle = valueCellStyle.withFillForegroundColor(Color(0xd0, 0xf3, 0xe1)).withFillPattern(CellFill.Solid)

  val caseCellStyle = valueCellStyle.withVerticalAlignment(CellVerticalAlignment.Top)

  val caseIdCellStyle = headerCellStyle

  val noteCellStyle = caseCellStyle.withoutWrapText

  implicit def toWidth(points: Int): Width = new Width(points, WidthUnit.Character)

  implicit def toHeight(points: Int): Height = new Height(points, HeightUnit.Point)
}
