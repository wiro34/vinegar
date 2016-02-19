package jp.co.so_net.vinegar.parser

trait DescriptionParser extends GherkinParser {

  import StringTrimmer.trimIndent

  def description: Option[String] = Option(feature.getDescription).map(trimIndent)

}
