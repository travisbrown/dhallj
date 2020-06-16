import ReleaseTransformations._

organization in ThisBuild := "org.dhallj"

val previousVersion = "0.3.0"
val catsVersion = "2.1.1"
val circeVersion = "0.13.0"
val jawnVersion = "1.0.0"
val munitVersion = "0.7.9"
val scalaCheckVersion = "1.14.3"
val snakeYamlVersion = "1.26"
val http4sVersion = "0.21.4"

val testDependencies = Seq(
  "co.nstant.in" % "cbor" % "0.9",
  "org.scalacheck" %% "scalacheck" % scalaCheckVersion,
  "org.scalameta" %% "munit" % munitVersion,
  "org.scalameta" %% "munit-scalacheck" % munitVersion
)

val http4sDependencies = Seq(
  "org.typelevel" %% "cats-core" % catsVersion,
  "org.typelevel" %% "cats-effect" % "2.1.3",
  "org.http4s" %% "http4s-client" % http4sVersion
)

val http4sBlazeClient =
  "org.http4s" %% "http4s-blaze-client" % http4sVersion

val compilerOptions = Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-unchecked",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Xfuture",
  "-Ywarn-unused-import"
)

def priorTo2_13(scalaVersion: String): Boolean =
  CrossVersion.partialVersion(scalaVersion) match {
    case Some((2, minor)) if minor < 13 => true
    case _                              => false
  }

val baseSettings = Seq(
  libraryDependencies ++= testDependencies.map(_ % Test),
  testFrameworks += new TestFramework("munit.Framework")
)

val javaSettings = Seq(
  autoScalaLibrary := false,
  crossPaths := false,
  javacOptions in Compile ++= Seq("-source", "1.7"),
  javacOptions in (Compile, compile) ++= Seq("-target", "1.7"),
  mimaPreviousArtifacts := Set("org.dhallj" % moduleName.value % previousVersion)
)

val scalaSettings = Seq(
  mimaPreviousArtifacts := Set("org.dhallj" %% moduleName.value % previousVersion),
  scalacOptions ++= {
    if (priorTo2_13(scalaVersion.value)) compilerOptions
    else
      compilerOptions.flatMap {
        case "-Ywarn-unused-import" => Seq("-Ywarn-unused:imports")
        case "-Xfuture"             => Nil
        case other                  => Seq(other)
      }
  },
  scalacOptions in (Compile, console) ~= {
    _.filterNot(Set("-Ywarn-unused-import", "-Ywarn-unused:imports"))
  }
)

val root = project
  .in(file("."))
  .enablePlugins(ScalaUnidocPlugin)
  .settings(baseSettings ++ publishSettings)
  .settings(
    skip in publish := true,
    mimaPreviousArtifacts := Set.empty,
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
    ast,
    scala,
    codec,
    circe,
    jawn,
    yaml,
    cats,
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
    description := "DhallJ core"
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
    mimaPreviousArtifacts := Set.empty,
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
      "org.typelevel" %% "jawn-spray" % jawnVersion % Test,
      "org.typelevel" %% "jawn-parser" % jawnVersion
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
    libraryDependencies += "org.yaml" % "snakeyaml" % snakeYamlVersion
  )
  .dependsOn(core, scala % Test)

lazy val ast = project
  .in(file("modules/ast"))
  .settings(baseSettings ++ scalaSettings ++ publishSettings)
  .settings(moduleName := "dhall-ast", name := "dhall-ast", description := "DhallJ Scala AST")
  .dependsOn(importsMini)

lazy val scala = project
  .in(file("modules/scala"))
  .settings(baseSettings ++ scalaSettings ++ publishSettings)
  .settings(moduleName := "dhall-scala", name := "dhall-scala", description := "DhallJ Scala wrapper")
  .dependsOn(ast, parser, importsMini)

lazy val codec = project
  .in(file("modules/scala-codec"))
  .settings(baseSettings ++ scalaSettings ++ publishSettings)
  .settings(
    moduleName := "dhall-scala-codec",
    name := "dhall-scala-codec",
    description := "DhallJ Scala encoding and decoding",
    libraryDependencies += "org.typelevel" %% "cats-core" % catsVersion
  )
  .dependsOn(scala)

lazy val testing = project
  .in(file("modules/testing"))
  .settings(baseSettings ++ scalaSettings ++ publishSettings)
  .settings(
    moduleName := "dhall-testing",
    name := "dhall-testing",
    description := "DhallJ ScalaCheck instances",
    libraryDependencies += "org.scalacheck" %% "scalacheck" % scalaCheckVersion
  )
  .dependsOn(ast)

lazy val javagen = project
  .in(file("modules/javagen"))
  .settings(baseSettings ++ scalaSettings ++ publishSettings)
  .settings(
    moduleName := "dhall-javagen",
    name := "dhall-javagen",
    description := "DhallJ Java code generation"
  )
  .dependsOn(core)

lazy val cats = project
  .in(file("modules/cats"))
  .settings(baseSettings ++ scalaSettings ++ publishSettings)
  .settings(moduleName := "dhall-cats", name := "dhall-cats", description := "DhallJ Cats integration")
  .settings(
    libraryDependencies += "org.typelevel" %% "cats-core" % catsVersion
  )
  .dependsOn(core, testing % Test)

lazy val imports = project
  .in(file("modules/imports"))
  .settings(baseSettings ++ scalaSettings ++ publishSettings)
  .settings(moduleName := "dhall-imports", name := "dhall-imports", description := "DhallJ import resolution")
  .settings(
    libraryDependencies ++= http4sDependencies :+ (http4sBlazeClient % Test)
  )
  .dependsOn(parser, cats)

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
    libraryDependencies ++= http4sDependencies :+ http4sBlazeClient,
    skip in publish := true,
    mimaPreviousArtifacts := Set.empty,
    fork in Test := true,
    baseDirectory in Test := (ThisBuild / baseDirectory).value,
    testOptions.in(Test) += Tests.Argument("--exclude-tags=Slow"),
    unmanagedResourceDirectories.in(Test) += (ThisBuild / baseDirectory).value / "dhall-lang",
    inConfig(Slow)(Defaults.testTasks),
    testOptions.in(Slow) -= Tests.Argument("--exclude-tags=Slow"),
    testOptions.in(Slow) += Tests.Argument("--include-tags=Slow")
  )
  .dependsOn(scala, imports, importsMini, testing)

lazy val benchmarks = project
  .in(file("benchmarks"))
  .settings(baseSettings ++ scalaSettings)
  .settings(
    skip in publish := true,
    mimaPreviousArtifacts := Set.empty
  )
  .enablePlugins(JmhPlugin)
  .dependsOn(core, parser, prelude)

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
