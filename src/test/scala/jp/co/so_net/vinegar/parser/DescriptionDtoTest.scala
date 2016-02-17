//package jp.co.so_net.vinegar.parser
//
//import jp.co.so_net.vinegar.model.Suite
//import org.scalatest._
//
//class DescriptionDtoTest extends FlatSpec with Matchers with FeatureLoader {
//  def suite = new DescriptionParser(feature).parse(Suite(name = "test"))
//
//  "Suite" should "have a description" in {
//    val description =
//      """* テスト仕様書はレビューや差分確認が容易になるため、テキストファイルで作成したい
//        |* 実施と結果の報告はテキストファイルでは難しいのでひとまずエクセルでまとめたい
//        |* テスト仕様書から自動で結果報告ファイルを生成したい""".stripMargin
//    suite.description should be(Some(description))
//  }
//}
