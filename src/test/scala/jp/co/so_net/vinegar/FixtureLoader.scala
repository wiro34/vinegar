package jp.co.so_net.vinegar

import gherkin._
import gherkin.ast._

import scala.io.Source

trait FixtureLoader {
  def parseFeature(featureText: String) = new Parser[Feature](new AstBuilder()).parse(featureText)

  def loadFeature(filepath: String) = parseFeature(Source.fromURL(getClass.getResource(filepath)).mkString)
}
