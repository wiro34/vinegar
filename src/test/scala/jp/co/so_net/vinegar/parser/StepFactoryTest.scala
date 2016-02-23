package jp.co.so_net.vinegar.parser

import gherkin.ast.{Location, Step}
import jp.co.so_net.vinegar.model.{Given, Then, When}
import org.scalatest._

class StepFactoryTest extends FlatSpec with Matchers {

  it should "discern Given step" in {
    val step = new Step(new Location(1, 1), "Given", "", null)
    StepFactory.create(step, None) shouldBe Given()
  }

  it should "discern When step" in {
    val step = new Step(new Location(1, 1), "When", "", null)
    StepFactory.create(step, None) shouldBe When()
  }

  it should "discern Then step" in {
    val step = new Step(new Location(1, 1), "Then", "", null)
    StepFactory.create(step, None) shouldBe Then()
  }

  it should "discern And step by last" in {
    val step = new Step(new Location(1, 1), "And", "", null)
    StepFactory.create(step, Some(When())) shouldBe When()
  }

  it should "discern But step by last" in {
    val step = new Step(new Location(1, 1), "But", "", null)
    StepFactory.create(step, Some(Then())) shouldBe Then()
  }

  it should "throw UnsupportedKeywordException when And step didn't have prev step" in {
    val step = new Step(new Location(1, 1), "And", "", null)
    the[UnsupportedKeywordException] thrownBy {
      StepFactory.create(step, None) shouldBe None
    } should have message "The location of `And` is invalid."
  }

  it should "throw UnsupportedKeywordException when But step didn't have prev step" in {
    val step = new Step(new Location(1, 1), "But", "", null)
    the[UnsupportedKeywordException] thrownBy {
      StepFactory.create(step, None) shouldBe None
    } should have message "The location of `But` is invalid."
  }

  it should "throw UnsupportedKeywordException when unknown step is used" in {
    val step = new Step(new Location(1, 1), "Therefore", "", null)
    the[UnsupportedKeywordException] thrownBy {
      StepFactory.create(step, Some(Then())) shouldBe Then()
    } should have message "Keyword `Therefore` is not supported."
  }

}

