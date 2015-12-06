package jp.co.so_net.vinegar.generator.excel

import jp.co.so_net.vinegar.facade.exceljs.{BorderStyle, CellBorder, Color, FillStyle}

object StyleDefinition {
   val stepBorder = CellBorder(
     left = BorderStyle(style = "thin", color = Color("FF000000")),
     bottom = BorderStyle(style = "dotted", color = Color("FF666666")),
     right = BorderStyle(style = "thin", color = Color("FF000000"))
   )

   val headerFill = FillStyle(
     `type` = "pattern",
     pattern = "solid",
     fgColor = Color("FFD0E1F3")
   )
   val headerBorder = CellBorder(
     top = BorderStyle(style = "thin"),
     left = BorderStyle(style = "thin"),
     bottom = BorderStyle(style = "thin"),
     right = BorderStyle(style = "thin")
   )

   val lineHeight: Double = 20
 }
