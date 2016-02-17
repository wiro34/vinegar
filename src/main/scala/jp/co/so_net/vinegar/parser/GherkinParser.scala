package jp.co.so_net.vinegar.parser

import gherkin._
import gherkin.ast._
import jp.co.so_net.vinegar.model.Suite

object GherkinParser {
  def parse(gherkin: String): Either[Throwable, Suite] = try {
    new ParserImpl(gherkin).parse
  } catch {
    case e: Throwable => Left(e)
  }

  class ParserImpl(gherkin: String) extends DescriptionParser with FeatureCommentParser with BackgroundParser {
    val feature: Feature = new Parser[Feature](new AstBuilder()).parse(gherkin)

    def parse: Either[Throwable, Suite] = {
      Right(Suite(
        name = feature.getName,
        background = background
      ))
    }
  }

}

trait GherkinParser {
  val feature: Feature
}
