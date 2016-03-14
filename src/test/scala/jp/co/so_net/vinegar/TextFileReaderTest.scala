package jp.co.so_net.vinegar

import java.io.File

import org.scalatest.{EitherValues, _}
import org.scalatest.prop.TableDrivenPropertyChecks._

import scala.io.Source

class TextFileReaderTest extends FlatSpec with Matchers with EitherValues {

  val textFileReader = new TextFileReader
  val expected = Source.fromURL(getClass.getResource("/fixture.feature")).mkString

  val files = Table(
    ("UTF-8", "/fixture.feature"),
    ("SJIS", "/fixture_sjis.feature")
  )

  forAll(files) { case (charset, file) =>
    it should s"read ${charset} encoded file" in {
      val actual = textFileReader.read(new File(getClass.getResource(file).toURI))
      actual.right.value shouldBe expected
    }
  }

  it should "occur an error when reading unsupported encoding file" in {
    val actual = textFileReader.read(new File(getClass.getResource("/fixture_utf16.feature").toURI))
    actual.left.value shouldBe a[FileEncodingException]
  }
}
