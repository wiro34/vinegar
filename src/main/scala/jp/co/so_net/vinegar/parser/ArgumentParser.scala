package jp.co.so_net.vinegar.parser

import gherkin.ast.Node
import jp.co.so_net.vinegar.model.{TableCell, TableRow, DataTable}

trait ArgumentParser {

  import collection.JavaConversions._

  def argument(arg: Node) = {
    arg match {
      case dt: gherkin.ast.DataTable =>
        val header = dt.getRows.head
        val body = dt.getRows.tail
                DataTable(
                  header = TableRow(header.getCells.map(c => TableCell(c.getValue))),
                  body = body.map(row => TableRow(row.getCells.map(c => TableCell(c.getValue))))
                )
      case _ =>
        "hoge"
    }
  }
}
