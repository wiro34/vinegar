//package jp.co.so_net.vinegar.parser
//
//import jp.co.so_net.vinegar.model.Suite
//import org.scalatest._
//
//class StepExtractorTest extends FlatSpec with Matchers with FeatureLoader {
//
//  import collection.JavaConversions._
//
//  val featureText =
//    """
//      |Feature: フィーチャ
//      |
//      |  Scenario: シナリオ１
//      |    Given ダミーGiven
//      |    And テーブル付きステップ:
//      |      | ID   | PW       |
//      |      | test | test1234 |
//      |    When 文字列付きステップ:
//      |      '''
//      |      http://example.com/login
//      |      '''
//      |    And コメント付きステップ
//      |      # ここはコメントです。
//      |      # 複数行も大丈夫です。
//      |      # 途中に # があっても大丈夫です。
//      |    Then ダミーThen
//      |    And ダミーAnd
//      |    But ダミーBut
//    """.stripMargin.replaceAll("'''", "\"\"\"")
//
//  it should "parse steps" in {
//    val steps = feature.getScenarioDefinitions.get(0).getSteps
//    val comments = feature.getComments
//    val extractor = new StepExtractor(steps, comments)
//
//    extractor.parse shouldBe Seq()
//    //    println(feature.getBackground.getSteps.get(1).getArgument.asInstanceOf[DataTable].getRows.get(0))
//  }
//}
