package jp.co.so_net.vinegar.dto

import gherkin._
import gherkin.ast._

import scala.io.Source

trait FeatureLoader {
  val featureFile = "/example.feature"
  lazy val featureText = Source.fromURL(getClass.getResource(featureFile)).mkString
  lazy val feature = new Parser[Feature](new AstBuilder()).parse(featureText)
}
