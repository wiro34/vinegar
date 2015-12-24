name := """vinegar"""

version := "0.1"

scalaVersion := "2.11.7"

libraryDependencies += "io.cucumber" % "gherkin3" % "3.1.2"

libraryDependencies += "com.norbitltd" % "spoiwo" % "1.0.6"

libraryDependencies += "com.github.scopt" %% "scopt" % "3.3.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"

resolvers += Resolver.sonatypeRepo("public")

val buildRelease = Def.taskKey[File]("Build release jar ...")

artifactPath in Compile in buildRelease := baseDirectory.value / "release" / ("vinegar-" + version.value + ".jar")

buildRelease in Compile := {
  val outFile = (artifactPath in Compile in buildRelease).value
  val assemblyFile = (assembly in Compile).value

  IO.copyFile(assemblyFile, outFile)

  outFile
}