//package jp.co.so_net.vinegar
//
//import org.scalatest._
//
//import scala.io.Source
//
//class VinegarParserTest extends FlatSpec with Matchers {
//  def validFeature = Source.fromURL(getClass.getResource("/fixture.feature")).mkString
//
//  def invalidFeature = "Invalid Feature"
//
//  def parse(s: String) = VinegarParser.parse(s)
//
//  def suite = parse(validFeature).right.get
//
//  it should "return Suite when parse is succeeded" in {
//    parse(validFeature) shouldBe an[Right[_, _]]
//  }
//
//  it should "return None when parse is failed" in {
//    parse(invalidFeature) shouldBe an[Left[_, _]]
//  }
//
//  "Suite" should "have a name" in {
//    suite.name shouldBe "フィーチャファイルからテスト実施結果報告書を生成する"
//  }
//
//  "Suite" should "have a description" in {
//    suite.description shouldBe defined
//  }
//
//  "Suite" should "have backgrounds" in {
//    suite.background shouldBe defined
//  }
//
//  "Suite" should "have five scenarios" in {
//    suite.scenarios shouldNot be(empty)
//  }
//}
