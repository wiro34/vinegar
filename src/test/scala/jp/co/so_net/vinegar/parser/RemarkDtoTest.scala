//package jp.co.so_net.vinegar.parser
//
//import jp.co.so_net.vinegar.model.Suite
//import org.scalatest._
//
//class RemarkDtoTest extends FlatSpec with Matchers with FeatureLoader {
//  def suite = new RemarkParser(feature).parse(Suite(name = "test"))
//
//  "Suite" should "have a description" in {
//    val remark =
//      """ここにコメントを書くと備考になります。
//        |もちろん複数のコメントを書くことも
//        |可能です。""".stripMargin
//    suite.remark should be(Some(remark))
//  }
//}
