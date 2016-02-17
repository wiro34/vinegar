package jp.co.so_net.vinegar.parser

import gherkin.ast.{Comment, Feature}
import jp.co.so_net.vinegar.model.Suite

abstract class PartialSuiteParser(protected val feature: Feature) extends HaveComments {

  import collection.JavaConversions._

  def parse(suite: Suite): Suite

  def comments: Seq[Comment] = feature.getComments.toList
}

trait HaveComments {
  def comments: Seq[Comment]
}
