package jp.co.so_net.vinegar.exporter.excel

import jp.co.so_net.vinegar.test.Wip
import org.scalatest.{FlatSpec, Matchers}

class HeightCalculatorTest extends FlatSpec with Matchers {

  "#calcHeight" should "return line count" in {
    val text =
      """１行目
        |２行目
        |
        |４行目""".stripMargin
    HeightCalculator.calcHeight(text) shouldBe 4
  }

  it should "calculate the long line as 2 lines" taggedAs Wip in {
    val text =
      """# コメントです。
        |# 日本語は考慮しておらず純粋に文字数しか見ていないので、長くてもこの行は１行にカウントされます。""".stripMargin
    HeightCalculator.calcHeight(text) shouldBe 2
  }

  "#calcHeight" should "return lines which considered ward wrap" in {
    val text =
      """URL のような英数字やハイフンを含む行は、
        |ハイフンや空白文字で自動折り返しが入ることを考慮します。
        |なので以下の URL は３行としてカウントされます。
        |http://station-name.example.com/so/much/long/station/name/llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogoch""".stripMargin
    HeightCalculator.calcHeight(text) shouldBe 6
  }

  "#calcLineHeight" should "return number of lines which considered ward wrap" in {
    val text = "http://station-name.example.com/so/much/long/station/name/llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogoch"
    HeightCalculator.calcLineHeight(text) shouldBe 3
  }

  "#wardWrap" should "return folded back string by space or hyphen" in {
    val text1 = "http://station-name.example.com/so/much/long/station/name/-llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogoch"
    HeightCalculator.wordWrap(text1) shouldBe
      """http://station-name.example.com/so/much/long/station/name/-
        |llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogoch""".stripMargin

    val text2 = "http://station-name.example.com/so/much/long/station/name/llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogoch"
    HeightCalculator.wordWrap(text2) shouldBe
      """http://station-
        |name.example.com/so/much/long/station/name/llanfairpwllgwyngyllgogerychwyrndrobwllll
        |antysiliogogogoch""".stripMargin
  }
}
