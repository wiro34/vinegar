//package jp.co.so_net.vinegar.transformer
//
//import gherkin.ast.Feature
//import jp.co.so_net.vinegar.FixtureLoader
//import jp.co.so_net.vinegar.model._
//import jp.co.so_net.vinegar.parser.HaveComments
//import org.scalatest._
//
//class StepMapperTest extends FlatSpec with Matchers with FixtureLoader {
//
//  import collection.JavaConversions._
//
//  val feature = loadFeature("/fixture.feature")
//  val steps = feature.getScenarioDefinitions.get(1).getSteps.toList
//  val comments = feature.getComments.toList
//  val commentScanStop = feature.getScenarioDefinitions.get(2).getLocation.getLine
//
//  it should "convert steps" in {
//    val mappedSteps = StepMapper.mapSteps(steps, comments, commentScanStop)
//    mappedSteps shouldBe Seq(
//      Given(text = "ホームディレクトリに移動する"),
//      When(text = "\"vinegar -o ~/Desktop /tmp/vinegar/example.feature\" コマンドを実行する"),
//      Then(text = "~/Desktop に example.xlsx ファイルが存在すること"),
//      Then(text = "/tmp/vinegar に example.xlsx ファイルが存在しないこと"),
//      Then(text = "カレントディレクトリに example.xlsx ファイルが存在しないこと"),
//      Given(text = "ホームディレクトリに移動する"),
//      When(
//        text = "\"vinegar /tmp/vinegar/example.feature --out /tmp/vinegar/path/to/deep/dir\" コマンドを実行する",
//        comment = Some(
//          """/tmp/vinegar/path/to/deep/dir は存在しないこと
//            |必要であれば rm -rf /tmp/vinegar/path/ で消しておく""".stripMargin
//        )
//      ),
//      Then(
//        text = "以下のエラーが表示されること:",
//        argument = Some(DataText("/tmp/vinegar/path/to/deep/dir: No such file or directory")),
//        comment = Some("エラーメッセージが日本語の可能性もあり")
//      ),
//      Then(text = "/tmp/vinegar/path/to/deep/dir に example.xlsx ファイルが存在しないこと"),
//      When(text = "\"vinegar /tmp/vinegar/example.feature --out /tmp/vinegar/path/to/deep/dir -f\" コマンドを実行する"),
//      Then(text = "エラーが表示されず終了すること"),
//      Then(text = "/tmp/vinegar/path/to/deep/dir に example.xlsx ファイルが存在すること")
//    )
//  }
//
//}
//
