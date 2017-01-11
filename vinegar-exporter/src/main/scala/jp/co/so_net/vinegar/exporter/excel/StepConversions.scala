package jp.co.so_net.vinegar.exporter.excel

import jp.co.so_net.vinegar.models._
import jp.co.so_net.vinegar.utils.Environment

private[excel]
object StepConversions {

  implicit class StepText(step: Step) {

    import jp.co.so_net.vinegar.exporter.StringUtils._

    def mkString: String = (step match {
      case _: Then =>
        Seq(Some(step.text), step.argument.map(step => indent(step.mkString, 4)))
      case _ =>
        Seq(Some(step.text), step.argument.map(step => indent(step.mkString, 4)), step.comment.map(_.replaceAll("(?m)^", "# ")))
    }).flatten.mkString(Environment.NEW_LINE)
  }

  implicit class ArgumentText(argument: StepArgument) {

    import jp.co.so_net.vinegar.exporter.StringUtils._

    def mkString: String = argument match {
      case dt: DataTable => tableText(dt)
      case ds: DocString => ds.text
      case ex: Example => ""
    }

    private def tableText(dt: DataTable): String = {
      val columnLengthes = dt.header.cells.zipWithIndex.map {
        case (_, index) =>
          (Seq(appearanceLength(dt.header.cells(index).value)) ++
            dt.rows.map(row => appearanceLength(row.cells(index).value))).max
      }

      def rowText(row: TableRow): String = (Seq("| ") ++
        row.cells.zipWithIndex.map { case (s, i) => paddingRight(s.value, ' ', columnLengthes(i)) }.mkString(" | ") ++
        Seq(" |")).mkString

      val headerText = rowText(dt.header)
      val rowsText = dt.rows.map(row => rowText(row)).mkString(Environment.NEW_LINE)

      headerText + Environment.NEW_LINE + rowsText
    }
  }

}
