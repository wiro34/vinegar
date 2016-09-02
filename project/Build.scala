import sbt.{Tests, _}
import sbt.Keys._
import sbtassembly.AssemblyPlugin._
import sbtassembly.AssemblyPlugin.autoImport._

object BuildSettings {
  val buildOrganization = "jp.co.so_net"
  val buildVersion = "0.3.0-SNAPSHOT"
  val buildScalaVersion = "2.11.8"

  def project(_name: String, dependencies: Seq[ModuleID], customSettings: Seq[Setting[_]] = Nil): Project =
    Project(
      id = _name.replace("vinegar-", ""),
      base = file("./" + _name),
      settings = Defaults.coreDefaultSettings ++ Seq(
        name := _name,
        organization := buildOrganization,
        version := buildVersion,
        scalaVersion := buildScalaVersion,
        scalacOptions ++= Seq("-deprecation"),
        testOptions in Test += Tests.Argument("-oT")
      ) ++ Seq(
        resolvers ++= Resolvers.all,
        libraryDependencies ++= dependencies
      ) ++ customSettings ++ assemblySettings
    )
}

object Resolvers {
  val public = Resolver.sonatypeRepo("public")
  val all = Seq(public)
}

object Dependencies {
  val gherkin = "io.cucumber" % "gherkin" % "4.0.0"
  val spoiwo = "com.norbitltd" % "spoiwo" % "1.0.6"
  val scopt = "com.github.scopt" %% "scopt" % "3.3.0"
  val scalactic = "org.scalactic" %% "scalactic" % "2.2.6"
  val scalatest = "org.scalatest" %% "scalatest" % "2.2.6" % "test"
  val scalamockScalatestSupport = "org.scalamock" %% "scalamock-scalatest-support" % "3.2" % "test"
  val pprint = "com.lihaoyi" %% "pprint" % "0.3.6"

  val common = Seq()

  val test = Seq(scalatest, scalamockScalatestSupport, pprint)

  val utils = Seq() ++ test
  val parser = Seq(gherkin) ++ test
  val exporter = Seq(spoiwo) ++ test
  val cui = Seq(scopt) ++ test
  val testUtils = Seq(gherkin % "test") ++ test
}

object VinegarBuild extends Build {

  import BuildSettings._

  lazy val buildRelease = Def.taskKey[File]("Build release jar ...")

  lazy val root = Project(id = "vinegar", base = file(".")) aggregate(parser, exporter)
  lazy val utils = project("vinegar-utils", Dependencies.test)
  lazy val models = project("vinegar-models", Dependencies.test)
  lazy val parser = project("vinegar-parser", Dependencies.parser) dependsOn(utils, models, testUtils % "test->test")
  lazy val exporter = project("vinegar-exporter", Dependencies.exporter) dependsOn(utils, models, testUtils % "test->test") aggregate models
  lazy val cui = project("vinegar-cui", Dependencies.cui) settings(
    artifactPath in Compile in buildRelease := baseDirectory.value / "release" / ("vinegar-" + version.value + ".jar"),
    buildRelease in Compile := {
      val outFile = (artifactPath in Compile in buildRelease).value
      val assemblyFile = (assembly in Compile).value
      IO.copyFile(assemblyFile, outFile)
      outFile
    }
    ) dependsOn(parser, exporter)
  lazy val testUtils = project("vinegar-test-utils", Dependencies.testUtils)
}
