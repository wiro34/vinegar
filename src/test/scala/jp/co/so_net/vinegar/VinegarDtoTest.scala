package jp.co.so_net.vinegar

import jp.co.so_net.vinegar.model.Suite
import org.scalatest._

import scala.io.Source

class VinegarDtoTest extends FlatSpec with Matchers {
  def feature = Source.fromURL(getClass.getResource("/example.feature")).mkString

  def parse(s: String) = VinegarDto.parse(s)

  def suite = parse(feature).right.get

  it should "return Suite when parse is succeeded" in {
    parse(feature) shouldBe an[Right[_, _]]
  }

  it should "return None when parse is failed" in {
    parse("Invalid") shouldBe an[Left[_, _]]
  }

  "Suite" should "have a name" in {
    suite.name shouldBe "フィーチャファイルからテスト実施結果報告書を生成する"
  }

  "Suite" should "have a description" in {
    suite.description shouldBe defined
  }

  "Suite" should "have backgrounds" in {
    suite.background shouldBe defined
  }

  "Suite" should "have five scenarios" in {
    suite.scenarios.length should ===(5)
  }
}
