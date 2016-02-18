package jp.co.so_net.vinegar.builder.excel

import java.nio.file.{Files, Paths}

import jp.co.so_net.vinegar.parser.GherkinParser
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

import scala.io.Source

class ExcelGeneratorTest extends FlatSpec with Matchers with BeforeAndAfter {
  val tmpDir = Paths.get(System.getProperty("java.io.tmpdir"))
  val outfile = tmpDir.resolve("example.xlsx").toString

  def featureText = Source.fromURL(getClass.getResource("/fixture.feature")).mkString

  def suite = GherkinParser.parse(featureText).right.get

  it should "write file" in {
    val sheet = ExcelGenerator().emptyRow().build(suite)
//    generator.writeFile(outfile)
//    Files.exists(Paths.get(outfile)) shouldBe true
  }
}
