package jp.co.so_net.vinegar.parser

import jp.co.so_net.vinegar.FixtureLoader
import jp.co.so_net.vinegar.model._
import org.scalatest.{FlatSpec, Matchers}

class StepTreeBuilderTest extends FlatSpec with Matchers with FixtureLoader {

  import collection.JavaConversions._

  def builder(scenarioIndex: Int) = {
    val feature = loadFeature("/fixture.feature")
    val steps = feature.getScenarioDefinitions.get(scenarioIndex).getSteps.toList
    val comments = feature.getComments.toList
    val commentScanStop = feature.getScenarioDefinitions.get(scenarioIndex + 1).getLocation.getLine
    new StepTreeBuilder(steps, comments, commentScanStop)
  }

  it should "convert steps" in {
    val givenGroups = builder(1).build
    givenGroups shouldBe Seq(
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
              argument = Some(DataText("/tmp/vinegar/path/to/deep/dir: No such file or directory")),
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
    )
  }

  it should "convert steps without given step" in {
    val givenGroups = builder(3).build
    givenGroups shouldBe Seq(
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
    )
  }

}

