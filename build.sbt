import org.scalajs.sbtplugin.ScalaJSPlugin

enablePlugins(ScalaJSPlugin)

name := """vinegar"""

version := "0.1"

scalaVersion := "2.11.7"

scalaJSStage in Global := FastOptStage

persistLauncher in Compile := true

persistLauncher in Test := false

postLinkJSEnv := NodeJSEnv(executable = "node").value

val buildBinFullOpt = Def.taskKey[File]("Generate the file ..")

artifactPath in Compile in buildBinFullOpt := baseDirectory.value / "bin" / "vinegar"

// @see https://gist.github.com/chandu0101/a92a9d7fd60ac741ca41
buildBinFullOpt in Compile := {
  val outFile = (artifactPath in Compile in buildBinFullOpt).value

  // open
  val loaderFile = (resourceDirectory in Compile).value / "loader.js"
  IO.copyFile(loaderFile, outFile)

  // add fullopt output
  val fullOutputCode = IO.read((fullOptJS in Compile).value.data)
  IO.append(outFile, fullOutputCode)

  // add launcher code too
  val launcher = (scalaJSLauncher in Compile).value.data.content
  IO.append(outFile, launcher)

  outFile
}

