package jp.co.so_net.vinegar.models

import org.scalatest._
import org.scalatest.prop.TableDrivenPropertyChecks._

class GWTTreeTest extends FlatSpec with Matchers {

  val testPatterns = Table(
    ("steps", "GWTTree"),

    (Seq(
      Given(text = "ホームディレクトリに移動する"),
      When(text = "\"vinegar -o ~/Desktop /tmp/vinegar/example.feature\" コマンドを実行する"),
      Then(text = "~/Desktop に example.xlsx ファイルが存在すること"),
      Then(text = "/tmp/vinegar に example.xlsx ファイルが存在しないこと"),
      Then(text = "カレントディレクトリに example.xlsx ファイルが存在しないこと"),
      Given(text = "ホームディレクトリに移動する"),
      When(
        text = "\"vinegar /tmp/vinegar/example.feature --out /tmp/vinegar/path/to/deep/dir\" コマンドを実行する",
        comment = Some(
          """/tmp/vinegar/path/to/deep/dir は存在しないこと
            |必要であれば rm -rf /tmp/vinegar/path/ で消しておく""".stripMargin
        )
      ),
      Then(
        text = "以下のエラーが表示されること:",
        argument = Some(DocString("/tmp/vinegar/path/to/deep/dir: No such file or directory")),
        comment = Some("エラーメッセージが日本語の可能性もあり")
      ),
      Then(text = "/tmp/vinegar/path/to/deep/dir に example.xlsx ファイルが存在しないこと"),
      When(text = "\"vinegar /tmp/vinegar/example.feature --out /tmp/vinegar/path/to/deep/dir -f\" コマンドを実行する"),
      Then(text = "エラーが表示されず終了すること"),
      Then(text = "/tmp/vinegar/path/to/deep/dir に example.xlsx ファイルが存在すること")
    ), GWTRoot(Seq(
      GivenGroup(
        steps = Seq(
          Given(text = "ホームディレクトリに移動する")
        ),
        children = Seq(WhenGroup(
          steps = Seq(
            When(text = "\"vinegar -o ~/Desktop /tmp/vinegar/example.feature\" コマンドを実行する")
          ),
          children = Seq(
            Then(text = "~/Desktop に example.xlsx ファイルが存在すること"),
            Then(text = "/tmp/vinegar に example.xlsx ファイルが存在しないこと"),
            Then(text = "カレントディレクトリに example.xlsx ファイルが存在しないこと")
          )
        ))
      ),
      GivenGroup(
        steps = Seq(
          Given(text = "ホームディレクトリに移動する")
        ),
        children = Seq(WhenGroup(
          steps = Seq(
            When(
              text = "\"vinegar /tmp/vinegar/example.feature --out /tmp/vinegar/path/to/deep/dir\" コマンドを実行する",
              comment = Some(
                """/tmp/vinegar/path/to/deep/dir は存在しないこと
                  |必要であれば rm -rf /tmp/vinegar/path/ で消しておく""".stripMargin
              )
            )
          ),
          children = Seq(
            Then(
              text = "以下のエラーが表示されること:",
              argument = Some(DocString("/tmp/vinegar/path/to/deep/dir: No such file or directory")),
              comment = Some("エラーメッセージが日本語の可能性もあり")
            ),
            Then(text = "/tmp/vinegar/path/to/deep/dir に example.xlsx ファイルが存在しないこと")
          )
        ), WhenGroup(
          steps = Seq(
            When(text = "\"vinegar /tmp/vinegar/example.feature --out /tmp/vinegar/path/to/deep/dir -f\" コマンドを実行する")
          ),
          children = Seq(
            Then(text = "エラーが表示されず終了すること"),
            Then(text = "/tmp/vinegar/path/to/deep/dir に example.xlsx ファイルが存在すること")
          )
        ))
      )
    ))),

    (Seq(
      When(text = "ホームディレクトリに移動する"),
      When(text = "\"vinegar -o ~/Desktop /tmp/vinegar/example.feature\" コマンドを実行する"),
      When(text = "このテキストは上記２ステップと合わせて「When」に記述されていること"),
      Then(text = "1. このテキストは「Then」に記述されていること"),
      Then(text = "2. このテキストは 1. の次行の「Then」に記述されていること"),
      Then(text = "3. このテキストは 3. の次行の「Then」に記述されていること"),
      Given(text = "このテキストは次の行の「Given」"),
      Then(text = "1. このテキストは「Then」に記述されていること"),
      When(text = "ホームディレクトリに移動する"),
      Then(text = "1. このテキストは「Then」に記述されていること"),
      When(text = "ホームディレクトリに移動する"),
      Then(text = "1. このテキストは「Then」に記述されていること")
    ), GWTRoot(Seq(
      GivenGroup(
        steps = Seq(),
        children = Seq(WhenGroup(
          steps = Seq(
            When(text = "ホームディレクトリに移動する"),
            When(text = "\"vinegar -o ~/Desktop /tmp/vinegar/example.feature\" コマンドを実行する"),
            When(text = "このテキストは上記２ステップと合わせて「When」に記述されていること")
          ),
          children = Seq(
            Then(text = "1. このテキストは「Then」に記述されていること"),
            Then(text = "2. このテキストは 1. の次行の「Then」に記述されていること"),
            Then(text = "3. このテキストは 3. の次行の「Then」に記述されていること")
          )
        ))
      ),
      GivenGroup(
        steps = Seq(
          Given(text = "このテキストは次の行の「Given」")
        ),
        children = Seq(WhenGroup(
          steps = Seq(),
          children = Seq(
            Then(text = "1. このテキストは「Then」に記述されていること")
          )
        ), WhenGroup(
          steps = Seq(
            When(text = "ホームディレクトリに移動する")
          ),
          children = Seq(
            Then(text = "1. このテキストは「Then」に記述されていること")
          )
        ), WhenGroup(
          steps = Seq(
            When(text = "ホームディレクトリに移動する")
          ),
          children = Seq(
            Then(text = "1. このテキストは「Then」に記述されていること")
          )
        ))
      )
    )))
  )

  it should "create a tree" in {
    forAll(testPatterns) { (steps: Seq[Step], root: GWTRoot) =>
      GWTTree(steps) shouldBe root
    }
  }
}
