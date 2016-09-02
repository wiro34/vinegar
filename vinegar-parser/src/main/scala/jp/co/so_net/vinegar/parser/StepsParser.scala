package jp.co.so_net.vinegar.parser

import gherkin.ast
import jp.co.so_net.vinegar.models._
import jp.co.so_net.vinegar.parser.Exceptions.UnsupportedKeywordException

import scala.annotation.tailrec

private[parser]
class StepsParser(document: ast.GherkinDocument) {

  import scala.collection.JavaConversions._

  private val GIVEN_PATTERN = """(Given|前提)""".r

  private val WHEN_PATTERN = """(When|もし)""".r

  private val THEN_PATTERN = """(Then|ならば)""".r

  private val OTHER_PATTERN = """(And|But|かつ|ただし)""".r

  private val commentScanner = new CommentScanner(document)

  def parse(steps: Seq[ast.Step]): Seq[Step] = {
    @tailrec
    def inner(steps: Seq[ast.Step], prevStep: Option[Step], results: Seq[Step]): Seq[Step] = steps match {
      case head :: rest =>
        val step = resolveStep(head, prevStep)
          .withText(head.getText)
          .withArgument(getArgument(head))
          .withComment(commentScanner.scan(head))
        inner(rest, Some(step), results :+ step)
      case Nil =>
        results
    }
    inner(steps, None, Nil)
  }

  private def resolveStep(original: ast.Step, prevStep: Option[Step]): Step = {
    (original.getKeyword.trim, prevStep) match {
      case (GIVEN_PATTERN(_), _) | (OTHER_PATTERN(_), Some(Given(_, _, _))) =>
        Given()
      case (WHEN_PATTERN(_), _) | (OTHER_PATTERN(_), Some(When(_, _, _))) =>
        When()
      case (THEN_PATTERN(_), _) | (OTHER_PATTERN(_), Some(Then(_, _, _))) =>
        Then()
      case (OTHER_PATTERN(keyword), None) =>
        throw new UnsupportedKeywordException(s"The location of `$keyword` is invalid.")
      case (keyword, _) =>
        throw new UnsupportedKeywordException(s"Keyword `$keyword` is not supported.")
    }
  }

  private def getArgument(step: ast.Step): Option[StepArgument] = step.getArgument match {
    case ds: ast.DocString =>
      Some(DocString(ds.getContent))
    case dt: ast.DataTable =>
      dt.getRows.toList match {
        case head :: rest =>
          Some(DataTable(
            header = convertTableRow(head),
            rows = rest.map(convertTableRow)
          ))
        case Nil =>
          None
      }
    case _ =>
      None
  }

  private def convertTableRow(row: ast.TableRow): TableRow =
    TableRow(cells = row.getCells.toList.map(cell => TableCell(cell.getValue)))
}
