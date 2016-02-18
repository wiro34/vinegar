package jp.co.so_net.vinegar

import gherkin._
import gherkin.ast._

import scala.io.Source

trait FixtureLoader {
  def parseFeature(featureText: String) = new Parser[Feature](new AstBuilder()).parse(featureText)

  def loadFeature(filepath: String) = parseFeature(loadFile(filepath))

  def loadFile(filepath: String) = Source.fromURL(getClass.getResource(filepath)).mkString
}
