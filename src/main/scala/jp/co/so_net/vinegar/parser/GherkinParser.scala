package jp.co.so_net.vinegar.parser

import gherkin._
import gherkin.ast._
import jp.co.so_net.vinegar.model.Suite

object GherkinParser {
  def parse(gherkin: String): Either[Throwable, Suite] = {
    try {
      val parser = new Parser[Feature](new AstBuilder())
      val feature = parser.parse(gherkin)
      val suite = Suite(name = feature.getName)
      val mappers = Seq(
        new DescriptionParser(feature),
        //        new BackgroundParser(feature),
        new RemarkParser(feature),
        new ScenarioParser(feature)
      )
      Right(mappers.foldLeft(suite)((suite, mapper) => mapper.parse(suite)))
    } catch {
      case e: Throwable => Left(e)
    }
  }
}

class GherkinParser2(gherkin: String) extends Background2 {
  def parse(gherkin: String): Either[Throwable, Suite] = {
    try {
      val parser = new Parser[Feature](new AstBuilder())
      implicit val feature = parser.parse(gherkin)
      Right(Suite(
        name = feature.getName,
        background = background
      ))
    } catch {
      case e: Throwable => Left(e)
    }
  }

}