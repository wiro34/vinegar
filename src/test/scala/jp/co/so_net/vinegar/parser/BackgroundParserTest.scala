package jp.co.so_net.vinegar.parser

import jp.co.so_net.vinegar.FixtureLoader
import jp.co.so_net.vinegar.model.{Background, Given, GivenGroup, Suite}
import org.scalatest._

class BackgroundParserTest extends FlatSpec with Matchers with FixtureLoader {
  val featureText =
    """
      |Feature: フィーチャ
      |
      |  Background:
      |    Given クッキーを削除する
      |    And 以下のユーザを作成する:
      |      | ID   | PW       |
      |      | test | test1234 |
      |    And ログインページを開く
      |      # http://example.com/login
    """.stripMargin

  val feature = parseFeature(featureText)

  it should "have backgrounds" in {
    val background = Background(
      group = Seq(GivenGroup(
        steps = Seq(
          Given(text = "クッキーを削除する"),
          Given(text = "以下のユーザを作成する:"),
          Given(text = "ログインページを開く", comment = Some("http://example.com/login"))
        )
      ))
    )
    val suite = new BackgroundParser(feature).parse(Suite(name = "test"))
    suite.background should be(Some(background))
  }
}
