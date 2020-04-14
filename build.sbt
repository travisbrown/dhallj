import ReleaseTransformations._

organization in ThisBuild := "org.dhallj"

val circeVersion = "0.13.0"

val testDependencies = Seq(
  "org.scalacheck" %% "scalacheck" % "1.14.3",
  "org.scalameta" %% "munit" % "0.7.2",
  "org.scalameta" %% "munit-scalacheck" % "0.7.2"
)

val baseSettings = Seq(
  libraryDependencies ++= testDependencies.map(_ % Test),
  testFrameworks += new TestFramework("munit.Framework")
)

val javaSettings = Seq(
  autoScalaLibrary := false,
  crossPaths := false
)

val scalaSettings = Seq()

val root = project
  .in(file("."))
  .enablePlugins(ScalaUnidocPlugin)
  .settings(
    skip in publish := true,
    initialCommands in console := "import org.dhallj.parser.DhallParser.parse",
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runClean,
      releaseStepCommand("javacc"),
      runTest,
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      publishArtifacts,
      setNextVersion,
      commitNextVersion
    )
  )
  .aggregate(
    core,
    parser,
    javagen,
    prelude,
    cli,
    scala,
    codec,
    circe,
    jawn,
    yaml,
    imports,
    importsMini,
    testing,
    tests,
    benchmarks
  )
  .dependsOn(importsMini, scala, javagen, prelude, yaml)

lazy val core = project
  .in(file("modules/core"))
  .settings(baseSettings ++ javaSettings ++ publishSettings)
  .settings(
    moduleName := "dhall-core",
    name := "dhall-core",
    description := "DhallJ core",
    javacOptions in Compile ++= Seq("-source", "1.7"),
    javacOptions in (Compile, compile) ++= Seq("-target", "1.7")
  )

lazy val parser = project
  .in(file("modules/parser"))
  .settings(baseSettings ++ javaSettings ++ publishSettings)
  .settings(
    moduleName := "dhall-parser",
    name := "dhall-parser",
    description := "DhallJ parser",
    // Temporarily necessary because JavaCC produces invalid Javadocs.
    javacOptions in Compile ++= Seq("-Xdoclint:none")
  )
  .enablePlugins(JavaCCPlugin)
  .dependsOn(core)

lazy val prelude = project
  .in(file("modules/prelude"))
  .settings(baseSettings ++ javaSettings ++ publishSettings)
  .settings(
    moduleName := "dhall-prelude",
    name := "dhall-prelude",
    description := "DhallJ Prelude"
  )
  .dependsOn(core)

lazy val cli = project
  .in(file("cli"))
  .settings(baseSettings ++ javaSettings)
  .settings(
    skip in publish := true,
    name in GraalVMNativeImage := "dhall-cli"
  )
  .enablePlugins(GraalVMNativeImagePlugin)
  .dependsOn(parser, importsMini)

lazy val circe = project
  .in(file("modules/circe"))
  .settings(baseSettings ++ scalaSettings ++ publishSettings)
  .settings(
    moduleName := "dhall-circe",
    name := "dhall-circe",
    description := "DhallJ Circe integration",
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-jawn" % circeVersion % Test,
      "io.circe" %% "circe-testing" % circeVersion % Test
    )
  )
  .dependsOn(core, scala % Test)

lazy val jawn = project
  .in(file("modules/jawn"))
  .settings(baseSettings ++ scalaSettings ++ publishSettings)
  .settings(
    moduleName := "dhall-jawn",
    name := "dhall-jawn",
    description := "DhallJ Jawn integration",
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-jawn" % circeVersion % Test,
      "org.typelevel" %% "jawn-spray" % "1.0.0" % Test,
      "org.typelevel" %% "jawn-parser" % "1.0.0"
    )
  )
  .dependsOn(core, scala % Test)

lazy val yaml = project
  .in(file("modules/yaml"))
  .settings(baseSettings ++ javaSettings ++ publishSettings)
  .settings(
    moduleName := "dhall-yaml",
    name := "dhall-yaml",
    description := "DhallJ YAML export",
    libraryDependencies += "org.yaml" % "snakeyaml" % "1.26"
  )
  .dependsOn(core, scala % Test)

lazy val scala = project
  .in(file("modules/scala"))
  .settings(baseSettings ++ scalaSettings ++ publishSettings)
  .settings(moduleName := "dhall-scala", name := "dhall-scala", description := "DhallJ Scala wrapper")
  .dependsOn(parser, importsMini)

lazy val codec = project
  .in(file("modules/scala-codec"))
  .settings(baseSettings ++ scalaSettings ++ publishSettings)
  .settings(
    moduleName := "dhall-scala-codec",
    name := "dhall-scala-codec",
    description := "DhallJ Scala encoding and decoding",
    libraryDependencies += "org.typelevel" %% "cats-core" % "2.1.1"
  )
  .dependsOn(scala)

lazy val testing = project
  .in(file("modules/testing"))
  .settings(baseSettings ++ scalaSettings ++ publishSettings)
  .settings(
    moduleName := "dhall-testing",
    name := "dhall-testing",
    description := "DhallJ ScalaCheck instances",
    libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.14.3"
  )
  .dependsOn(scala)

lazy val javagen = project
  .in(file("modules/javagen"))
  .settings(baseSettings ++ scalaSettings ++ publishSettings)
  .settings(
    moduleName := "dhall-javagen",
    name := "dhall-javagen",
    description := "DhallJ Java code generation"
  )
  .dependsOn(core)

lazy val imports = project
  .in(file("modules/imports"))
  .settings(baseSettings ++ scalaSettings ++ publishSettings)
  .settings(moduleName := "dhall-imports", name := "dhall-imports", description := "DhallJ import resolution")
  .settings(
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core" % "2.1.1",
      "org.typelevel" %% "cats-effect" % "2.1.2",
      "org.http4s" %% "http4s-dsl" % "0.21.3",
      "org.http4s" %% "http4s-blaze-client" % "0.21.3"
    )
  )
  .dependsOn(parser, core)

lazy val importsMini = project
  .in(file("modules/imports-mini"))
  .settings(baseSettings ++ javaSettings ++ publishSettings)
  .settings(
    moduleName := "dhall-imports-mini",
    name := "dhall-imports-mini",
    description := "DhallJ import resolution for Java"
  )
  .dependsOn(parser, core)

lazy val Slow = config("slow").extend(Test)

lazy val tests = project
  .in(file("tests"))
  .configs(Slow)
  .settings(baseSettings ++ scalaSettings)
  .settings(
    libraryDependencies ++= testDependencies,
    skip in publish := true,
    unmanagedResourceDirectories.in(Test) += (ThisBuild / baseDirectory).value / "dhall-lang",
    testOptions.in(Test) += Tests.Argument("--exclude-tags=Slow"),
    inConfig(Slow)(Defaults.testTasks),
    testOptions.in(Slow) -= Tests.Argument("--exclude-tags=Slow"),
    testOptions.in(Slow) += Tests.Argument("--include-tags=Slow")
  )
  .dependsOn(scala, importsMini, testing)

lazy val benchmarks = project
  .in(file("benchmarks"))
  .settings(baseSettings ++ scalaSettings)
  .settings(
    skip in publish := true
  )
  .enablePlugins(JmhPlugin)
  .dependsOn(core, prelude)

lazy val publishSettings = Seq(
  releaseCrossBuild := true,
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  releaseVcsSign := true,
  homepage := Some(url("https://github.com/travisbrown/djallj")),
  licenses := Seq("BSD 3-Clause" -> url("http://opensource.org/licenses/BSD-3-Clause")),
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := { _ => false },
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots".at(nexus + "content/repositories/snapshots"))
    else
      Some("releases".at(nexus + "service/local/staging/deploy/maven2"))
  },
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/travisbrown/djallj"),
      "scm:git:git@github.com:travisbrown/djallj.git"
    )
  ),
  pomExtra := (
    <developers>
      <developer>
        <id>travisbrown</id>
        <name>Travis Brown</name>
        <email>travisrobertbrown@gmail.com</email>
        <url>https://twitter.com/travisbrown</url>
      </developer>
      <developer>
        <id>TimWSpence</id>
        <name>Tim Spence</name>
        <url>https://github.com/TimWSpence</url>
      </developer>
    </developers>
  )
)
