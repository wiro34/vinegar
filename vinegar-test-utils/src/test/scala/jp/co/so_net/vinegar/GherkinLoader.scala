package jp.co.so_net.vinegar

import gherkin.ast.GherkinDocument
import gherkin.{AstBuilder, Parser}

import scala.io.Source

trait GherkinLoader {
  def parseDocument(gherkinText: String) = new Parser[GherkinDocument](new AstBuilder()).parse(gherkinText)

  def loadDocument(filepath: String) = parseDocument(loadFile(filepath))

  private def loadFile(filepath: String) = Source.fromURL(getClass.getResource(filepath)).mkString
}
