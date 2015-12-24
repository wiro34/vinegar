package jp.co.so_net.vinegar.dto

import gherkin._
import gherkin.ast._

import scala.io.Source

trait FeatureLoader {
  lazy val featureText = Source.fromURL(getClass.getResource("/example.feature")).mkString
  lazy val feature = new Parser[Feature](new AstBuilder()).parse(featureText)
}
