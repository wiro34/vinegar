package jp.co.so_net.vinegar.models

/*
 * https://github.com/cucumber/gherkin
 */

case class Feature(name: String,
                   description: Option[String],
                   comment: Option[String],
                   background: Option[Background],
                   scenarioDefinitions: Seq[ScenarioDefinition])

sealed abstract class ScenarioDefinition(val name: String,
                                         val description: String,
                                         val steps: Seq[Step])

case class Background(override val steps: Seq[Step]) extends ScenarioDefinition("", "", steps)

case class Scenario(override val name: String,
                    override val steps: Seq[Step]) extends ScenarioDefinition(name, "", steps)

case class ScenarioOutline(override val name: String,
                           override val steps: Seq[Step],
                           examples: Seq[Example]) extends ScenarioDefinition(name, "", steps)

sealed trait Step {
  val text: String
  val argument: Option[StepArgument]
  val comment: Option[String]

  def copyStep(text: String = text,
               argument: Option[StepArgument] = argument,
               comment: Option[String] = comment): Step = this match {
    case g @ Given(_, _, _) => g.copy(text, argument, comment)
    case w @ When(_, _, _) => w.copy(text, argument, comment)
    case t @ Then(_, _, _) => t.copy(text, argument, comment)
  }

  def withText(text: String): Step = copyStep(text = text)

  def withArgument(argument: Option[StepArgument]): Step = copyStep(argument = argument)

  def withComment(comment: Option[String]): Step = copyStep(comment = comment)
}

case class Given(text: String = "",
                 argument: Option[StepArgument] = None,
                 comment: Option[String] = None) extends Step

case class When(text: String = "",
                argument: Option[StepArgument] = None,
                comment: Option[String] = None) extends Step

case class Then(text: String = "",
                argument: Option[StepArgument] = None,
                comment: Option[String] = None) extends Step

sealed trait StepArgument

case class DocString(text: String, contentType: Option[String] = None) extends StepArgument

case class DataTable(header: TableRow, rows: Seq[TableRow]) extends StepArgument

case class TableRow(cells: Seq[TableCell])

case class TableCell(value: String)

case class Example(header: TableRow, rows: Seq[TableRow]) extends StepArgument
