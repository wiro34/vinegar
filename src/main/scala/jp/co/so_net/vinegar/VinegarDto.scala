package jp.co.so_net.vinegar

import jp.co.so_net.vinegar.dto.{ScenarioDto, DescriptionDto, BackgroundDto}
import jp.co.so_net.vinegar.model.{Scenario, Suite, Case}
import gherkin._
import gherkin.ast._

object VinegarDto {
  def parse(gherkin: String): Either[Throwable, Suite] = {
    try {
      val parser = new Parser[Feature](new AstBuilder())
      val feature = parser.parse(gherkin)
      val suite = Suite(name = feature.getName)
      val parsers = Seq(
        new DescriptionDto(feature),
        new BackgroundDto(feature),
        new ScenarioDto(feature)
      )
      Right(parsers.foldLeft(suite)((suite, parser) => parser.parseSuite(suite)))
    } catch {
      case e: Throwable => Left(e)
    }
  }
}








