package jp.co.so_net.vinegar.parser

import org.scalatest._

class StringTrimmerTest extends FlatSpec with Matchers {
  "#trimComment" should "trim # from head of the line" in {
    val comment =
      """
        |   # インデント付きのコメントです。
        |   # コメント内の # は削除されません。
        |   #   シャープマーク以降のスペースは１つだけ削除されます。
        |   # # は最初の１個だけ削除されます。
        |   #シャープの後はスペース無くても大丈夫です。
      """.stripMargin
    StringTrimmer.trimComment(comment) shouldBe
      """
        |インデント付きのコメントです。
        |コメント内の # は削除されません。
        |  シャープマーク以降のスペースは１つだけ削除されます。
        |# は最初の１個だけ削除されます。
        |シャープの後はスペース無くても大丈夫です。
      """.stripMargin
  }


  "#trimIndent" should "trim minimum common indent" in {
    val text =
      """   共通のインデント部分を削除します。
        |     最も短いインデントに合わせてるため、
        |  この行が基準となります。""".stripMargin
    StringTrimmer.trimIndent(text) shouldBe
      """ 共通のインデント部分を削除します。
        |   最も短いインデントに合わせてるため、
        |この行が基準となります。""".stripMargin
  }
}
