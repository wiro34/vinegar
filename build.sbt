name := """vinegar"""

version := "0.1.1"

scalaVersion := "2.11.7"

libraryDependencies += "io.cucumber" % "gherkin3" % "3.1.2"

libraryDependencies += "com.norbitltd" % "spoiwo" % "1.0.6"

libraryDependencies += "com.github.scopt" %% "scopt" % "3.3.0"

libraryDependencies += "org.scalactic" %% "scalactic" % "2.2.6"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"

libraryDependencies += "org.scalamock" %% "scalamock-scalatest-support" % "3.2" % "test"

libraryDependencies += "com.lihaoyi" %% "pprint" % "0.3.6"

scalacOptions ++= Seq("-feature", "-deprecation")

resolvers += Resolver.sonatypeRepo("public")

val buildRelease = Def.taskKey[File]("Build release jar ...")

artifactPath in Compile in buildRelease := baseDirectory.value / "release" / ("vinegar-" + version.value + ".jar")

buildRelease in Compile := {
  val outFile = (artifactPath in Compile in buildRelease).value
  val assemblyFile = (assembly in Compile).value

  IO.copyFile(assemblyFile, outFile)

  outFile
}