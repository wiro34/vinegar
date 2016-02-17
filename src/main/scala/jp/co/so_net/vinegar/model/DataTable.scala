package jp.co.so_net.vinegar.model

case class TableCell(value: String) extends AnyVal

case class TableRow(cells: Seq[TableCell])

case class DataTable(header: TableRow, body: Seq[TableRow]) {
  override def toString: String= {
    header.cells.mkString(" | ")
  }
}
