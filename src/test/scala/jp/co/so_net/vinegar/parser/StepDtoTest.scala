//package jp.co.so_net.vinegar.parser
//
//import jp.co.so_net.vinegar.model.{Given, Then, When}
//import org.scalatest._
//
//class StepDtoTest extends WordSpec with Matchers with FeatureLoader with Inside {
//
//  import pprint.Config.Defaults._
//  import collection.JavaConversions._
//
//  def scenario(scenarioIndex: Int) = feature.getScenarioDefinitions.get(scenarioIndex)
//
//  def stepDto(scenarioIndex: Int) = new StepDto(feature, scenario(scenarioIndex), scenarioIndex + 1)
//
//  def steps(scenarioIndex: Int) = scenario(scenarioIndex).getSteps.toList
//
//  def parseSteps(scenarioIndex: Int) = stepDto(scenarioIndex).parseSteps
//
//  "#parseSteps" when {
//    "parse second step" should {
//      val givens = parseSteps(1)
//
//      "return given list of this scenario" in {
////        pprint.pprintln(givens)
//        givens should ===(Seq(
//          Given(Some("ホームディレクトリに移動する"), None, Seq(
//            When(Some("\"vinegar -o ~/Desktop /tmp/vinegar/fixture.feature\" コマンドを実行する"), None, Seq(
//              Then("002001", Some("~/Desktop に example.xlsx ファイルが存在すること"), None),
//              Then("002002", Some("/tmp/vinegar に example.xlsx ファイルが存在しないこと"), None),
//              Then("002003", Some("カレントディレクトリに example.xlsx ファイルが存在しないこと"), None)
//            ))
//          )),
//          Given(Some("ホームディレクトリに移動する"), None, Seq(
//            When(Some("\"vinegar /tmp/vinegar/fixture.feature --out /tmp/vinegar/path/to/deep/dir\" コマンドを実行する"),
//              Some("/tmp/vinegar/path/to/deep/dir は存在しないこと\n必要であれば rm -rf /tmp/vinegar/path/ で消しておく"), Seq(
//                Then("002004", Some("以下のエラーが表示されること:\n/tmp/vinegar/path/to/deep/dir: No such file or directory"),
//                  Some("日本語環境の場合はエラーメッセージが日本語になる場合もあります")),
//                Then("002005", Some("/tmp/vinegar/path/to/deep/dir に example.xlsx ファイルが存在しないこと"), None)
//              )),
//            When(Some("\"vinegar /tmp/vinegar/fixture.feature --out /tmp/vinegar/path/to/deep/dir -f\" コマンドを実行する"), None, Seq(
//              Then("002006", Some("エラーが表示されず終了すること"), None),
//              Then("002007", Some("/tmp/vinegar/path/to/deep/dir に example.xlsx ファイルが存在すること"), None)
//            ))
//          ))
//        ))
//      }
//    }
//  }
//}
