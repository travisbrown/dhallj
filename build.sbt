organization in ThisBuild := "org.dhallj"
scapegoatVersion in ThisBuild := "1.4.3"

val circeVersion = "0.13.0"

val testDependencies = Seq(
  "org.scalacheck" %% "scalacheck" % "1.14.3",
  "org.scalameta" %% "munit" % "0.7.2",
  "org.scalameta" %% "munit-scalacheck" % "0.7.2",
  "co.nstant.in" % "cbor" % "0.9"
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
    initialCommands in console := "import org.dhallj.parser.DhallParser.parse"
  )
  .aggregate(core, parser, javagen, prelude, demo, scala, circe, jawn, yaml, imports, importsMini, tests, benchmarks)
  .dependsOn(importsMini, scala, javagen, prelude)

lazy val core = project
  .in(file("modules/core"))
  .settings(baseSettings ++ javaSettings)
  .settings(moduleName := "dhall-core",
            javacOptions in Compile ++= Seq("-source", "1.7"),
            javacOptions in (Compile, compile) ++= Seq("-target", "1.7"))

lazy val parser = project
  .in(file("modules/parser"))
  .settings(baseSettings ++ javaSettings)
  .settings(
    moduleName := "dhall-parser",
    // Temporarily necessary because JavaCC produces invalid Javadocs.
    javacOptions in Compile ++= Seq("-Xdoclint:none")
  )
  .enablePlugins(JavaCCPlugin)
  .dependsOn(core)

lazy val prelude = project
  .in(file("modules/prelude"))
  .settings(baseSettings ++ javaSettings)
  .settings(
    moduleName := "dhall-prelude"
  )
  .dependsOn(core)

lazy val demo = project
  .in(file("demo"))
  .settings(baseSettings ++ javaSettings)
  .settings(
    skip in publish := true
  )
  .enablePlugins(GraalVMNativeImagePlugin)
  .dependsOn(parser, importsMini)

lazy val circe = project
  .in(file("modules/circe"))
  .settings(baseSettings ++ scalaSettings)
  .settings(
    moduleName := "dhall-circe",
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-jawn" % circeVersion % Test,
      "io.circe" %% "circe-testing" % circeVersion % Test
    )
  )
  .dependsOn(core, scala % Test)

lazy val jawn = project
  .in(file("modules/jawn"))
  .settings(baseSettings ++ scalaSettings)
  .settings(
    moduleName := "dhall-jawn",
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-jawn" % circeVersion % Test,
      "org.typelevel" %% "jawn-spray" % "1.0.0" % Test,
      "org.typelevel" %% "jawn-parser" % "1.0.0"
    )
  )
  .dependsOn(core, scala % Test)

lazy val yaml = project
  .in(file("modules/yaml"))
  .settings(baseSettings ++ javaSettings)
  .settings(
    moduleName := "dhall-yaml",
    libraryDependencies += "org.yaml" % "snakeyaml" % "1.26"
  )
  .dependsOn(core, scala % Test)

lazy val scala = project
  .in(file("modules/scala"))
  .settings(baseSettings ++ scalaSettings)
  .settings(moduleName := "dhall-scala")
  .dependsOn(parser)

lazy val testing = project
  .in(file("modules/testing"))
  .settings(
    baseSettings ++ scalaSettings,
    libraryDependencies ++= Seq("org.scalacheck" %% "scalacheck" % "1.14.3")
  )
  .settings(moduleName := "dhall-testing")
  .dependsOn(scala)

lazy val javagen = project
  .in(file("modules/javagen"))
  .settings(baseSettings ++ scalaSettings)
  .settings(
    moduleName := "dhall-javagen"
  )
  .dependsOn(core)

lazy val imports = project
  .in(file("modules/imports"))
  .settings(baseSettings ++ scalaSettings)
  .settings(moduleName := "dhall-imports")
  .settings(
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core" % "2.2.0-M1",
      "org.typelevel" %% "cats-effect" % "2.1.2",
      "org.http4s" %% "http4s-dsl" % "0.21.3",
      "org.http4s" %% "http4s-blaze-client" % "0.21.3"
    )
  )
  .dependsOn(parser, core)

lazy val importsMini = project
  .in(file("modules/imports-mini"))
  .settings(baseSettings ++ javaSettings)
  .settings(moduleName := "dhall-imports-mini")
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
