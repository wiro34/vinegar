package jp.co.so_net.vinegar

import java.io.File
import java.nio.file.{Path, Paths}

import com.norbitltd.spoiwo.model.Sheet
import jp.co.so_net.vinegar.builder.Builder
import jp.co.so_net.vinegar.model.{Suite => ModelSuite}
import org.scalamock.scalatest.MockFactory
import org.scalatest._

import scala.io.Source

class VinegarTest extends FlatSpec with Matchers with MockFactory {
  val dummyPath = "/tmp/path/to/dummy.feature"

  object FixtureFileReader extends FileReader {
    def read(file: File): String = Source.fromURL(getClass.getResource("/fixture.feature")).mkString
  }

  object StubBuilder extends Builder[Sheet] {
    def build(suite: ModelSuite): Sheet = Sheet()
  }

  object StubFileWriter extends FileWriter[Sheet] {
    def write(filename: String, sheet: Sheet): Unit = {}
  }

  trait MockConsole extends Console {
    var messages = Seq.empty[String]

    override def println(message: String) = messages = messages :+ message
  }

  trait MockSystemTerminator extends SystemTerminator {
    override def exit(status: Int): Unit = {}
  }

  trait MockDirectoryUtil extends DirectoryUtil {
    val mockMakeDirRecursive = mockFunction[Path, Unit]

    override def makeDirRecursive(path: Path): Unit = mockMakeDirRecursive(path)
  }

  def newVinegar(args: String*)(builder: Builder[Sheet] = StubBuilder,
                                reader: FileReader = FixtureFileReader,
                                writer: FileWriter[Sheet] = StubFileWriter) = {
    val config = VinegarOptionParser.parse(args, VinegarOption()).get
    new Vinegar(config, builder, reader, writer) with MockConsole with MockSystemTerminator with MockDirectoryUtil
  }

  it should "generate excel file in the same directory of feature file" in {
    val mockFileWriter = mock[ExcelFileWriter]
    mockFileWriter.write _ expects("/tmp/path/to/dummy.xlsx", Sheet())
    newVinegar(dummyPath)(reader = FixtureFileReader, writer = mockFileWriter).run()
  }

  "Invalid file path" should "occurs an error" in {
    val vinegar = newVinegar("/path/to/invalid/file")(reader = new TextFileReader)
    vinegar.run()
    vinegar.messages.head should include regex "Error: /path/to/invalid/file \\(No such file or directory\\)"
  }

  "The directory which is not exist is given to output option without force option" should "occur an error" in {
    val vinegar = newVinegar("-o", "/tmp/path/to/invalid", dummyPath)(writer = new ExcelFileWriter)
    vinegar.run()
    vinegar.messages.head should include regex "Error: /tmp/path/to/invalid/dummy.xlsx \\(No such file or directory\\)"
  }

  "The directory which is not exist is given to output option with force option" should "create directory recursively and write file" in {
    val mockFileWriter = mock[ExcelFileWriter]
    val vinegar = newVinegar("-o", "/tmp/path/to/output", "--force", dummyPath)(writer = mockFileWriter)
    mockFileWriter.write _ expects("/tmp/path/to/output/dummy.xlsx", Sheet())
    vinegar.mockMakeDirRecursive expects Paths.get("/tmp/path/to/output/")
    vinegar.run()
  }

}
