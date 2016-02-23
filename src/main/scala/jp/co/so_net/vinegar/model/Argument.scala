package jp.co.so_net.vinegar.model

sealed trait Argument {
  def mkString: String
}

case class DataText(text: String) extends Argument {
  def mkString: String = text
}

//case class TableCell(value: String) extends AnyVal
//
//case class TableRow(cells: Seq[TableCell])
//
//case class DataTable(header: TableRow, body: Seq[TableRow]) extends Argument {
//  override def toString: String = {
//    header.cells.mkString(" | ")
//  }
//}
