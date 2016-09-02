package jp.co.so_net.vinegar.exporter.excel

import com.norbitltd.spoiwo.model.Sheet
import jp.co.so_net.vinegar.models.Feature
import org.scalatest.OptionValues._
import org.scalatest.{FlatSpec, Matchers}

class HasDescriptionImplTest extends FlatSpec with Matchers {
  var descriptionPart = new HasDescriptionImpl {}

  it should "add a description row" in {
    val description =
      """* ○○として、
        |* △△がほしい。
        |* なぜならば、××だからだ。""".stripMargin
    val feature = Feature(name = "", description = Some(description), background = None, comment = None, scenarioDefinitions = Nil)
    val sheet = descriptionPart.description(Sheet(), feature)
    val row = sheet.rows.head
    val cells = row.cells.toSeq

    cells(0).value shouldBe "目的／観点"
    cells(1).value shouldBe description
    row.height.value.toPoints shouldBe 48
  }

  it should "add an empty description row when feature has no description" in {
    val feature = Feature(name = "", description = None, background = None, comment = None, scenarioDefinitions = Nil)
    val sheet = descriptionPart.description(Sheet(), feature)
    val row = sheet.rows.head
    val cells = row.cells.toSeq

    cells(0).value shouldBe "目的／観点"
    cells(1).value shouldBe ""
    row.height.value.toPoints shouldBe 16
  }
}
