package jp.co.so_net.vinegar

import java.io.File

case class VinegarOption(file: File = new File("."),
                         out: Option[File] = None,
                         force: Boolean = false)

object VinegarOptionParser extends scopt.OptionParser[VinegarOption](Vinegar.name) {
  opt[File]('o', "out") valueName ("<outdir>") action { (x, option) =>
    option.copy(out = Some(x))
  } text ("output directory of generated excel file")

  opt[Unit]('f', "force") action { (_, option) =>
    option.copy(force = true)
  } text ("create output directory (recursively) if directory is not exist")

  arg[File]("file") unbounded() required() action { (x, option) =>
    option.copy(file = x)
  } text ("feature file")

  help("help") text ("prints this usage text")
}
