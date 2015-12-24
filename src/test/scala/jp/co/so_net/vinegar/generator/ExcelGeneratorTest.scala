package jp.co.so_net.vinegar.generator

import java.nio.file.{Files, Paths}

import jp.co.so_net.vinegar.VinegarDto
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

import scala.io.Source

class ExcelGeneratorTest extends FlatSpec with Matchers with BeforeAndAfter {
  val tmpDir = Paths.get(System.getProperty("java.io.tmpdir"))
  val outfile = tmpDir.resolve("example.xlsx").toString

  def featureText = Source.fromURL(getClass.getResource("/example.feature")).mkString

  def suite = VinegarDto.parse(featureText).right.get

  def generator = new ExcelGenerator(suite)

  after {
    if (Files.exists(Paths.get(outfile)))
      Files.delete(Paths.get(outfile))
  }

  it should "write file" in {
    generator.writeFile(outfile)
    Files.exists(Paths.get(outfile)) shouldBe true
  }
}
