package jp.co.so_net.vinegar.builder.excel

import jp.co.so_net.vinegar.Wip
import org.scalatest.{FlatSpec, Matchers}

class HeightCalculatorTest extends FlatSpec with Matchers {

  "#calcHeight" should "return line count" in new HeightCalculator {
    val text =
      """１行目
        |２行目
        |
        |４行目""".stripMargin
    calcHeight(text) shouldBe 4
  }

  it should "calculate the long line as 2 lines" taggedAs Wip in new HeightCalculator {
    val text =
      """# コメントです。
        |# 日本語は考慮しておらず純粋に文字数しか見ていないので、長くてもこの行は１行にカウントされます。""".stripMargin
    calcHeight(text) shouldBe 2
  }

  "#calcHeight" should "return lines which considered ward wrap" in new HeightCalculator {
    val text =
      """URL のような英数字やハイフンを含む行は、
        |ハイフンや空白文字で自動折り返しが入ることを考慮します。
        |なので以下の URL は３行としてカウントされます。
        |http://station-name.example.com/so/much/long/station/name/llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogoch""".stripMargin
    calcHeight(text) shouldBe 6
  }

  "#calcLineHeight" should "return number of lines which considered ward wrap" in new HeightCalculator {
    val text = "http://station-name.example.com/so/much/long/station/name/llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogoch"
    calcLineHeight(text) shouldBe 3
  }

  "#wardWrap" should "return folded back string by space or hyphen" in new HeightCalculator {
    val text1 = "http://station-name.example.com/so/much/long/station/name/-llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogoch"
    wordWrap(text1) shouldBe
      """http://station-name.example.com/so/much/long/station/name/-
        |llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogoch""".stripMargin

    val text2 = "http://station-name.example.com/so/much/long/station/name/llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogoch"
    wordWrap(text2) shouldBe
      """http://station-
        |name.example.com/so/much/long/station/name/llanfairpwllgwyngyllgogerychwyrndrobwllll
        |antysiliogogogoch""".stripMargin
  }
}
