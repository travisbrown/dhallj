organization in ThisBuild := "org.dhallj"

val testDependencies = Seq(
  "org.scalacheck" %% "scalacheck" % "1.14.3",
  "org.scalameta" %% "munit" % "0.7.2",
  "co.nstant.in" % "cbor" % "0.9"
)

val catsDependencies = Seq(
  "org.typelevel" %% "cats-core" % "2.2.0-M1",
  "org.typelevel" %% "cats-effect" % "2.1.2",
  "org.http4s" %% "http4s-dsl" % "0.21.3",
  "org.http4s" %% "http4s-blaze-client" % "0.21.3"
)

val baseSettings = Seq(
  scalaVersion := "2.13.1",
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
  .settings(
    scalaVersion := "2.13.1",
    initialCommands in console := "import org.dhallj.parser.DhallParser.parse"
  )
  .aggregate(core, parser, javagen, prelude, demo, scala, imports, importsMini, tests, benchmarks)
  .dependsOn(importsMini, scala, javagen, prelude)

lazy val core = project
  .in(file("core"))
  .settings(baseSettings ++ javaSettings)
  .settings(moduleName := "dhall-core",
            javacOptions in Compile ++= Seq("-source", "1.7"),
            javacOptions in (Compile, compile) ++= Seq("-target", "1.7"))

lazy val parser = project
  .in(file("parser"))
  .settings(baseSettings ++ javaSettings)
  .settings(
    moduleName := "dhall-parser",
    // Temporarily necessary because JavaCC produces invalid Javadocs.
    javacOptions in Compile ++= Seq("-Xdoclint:none")
  )
  .enablePlugins(JavaCCPlugin)
  .dependsOn(core)

lazy val prelude = project
  .in(file("prelude"))
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

lazy val scala = project
  .in(file("scala"))
  .settings(baseSettings ++ scalaSettings)
  .settings(moduleName := "dhall-scala")
  .dependsOn(parser)

lazy val javagen = project
  .in(file("javagen"))
  .settings(baseSettings ++ scalaSettings)
  .settings(
    moduleName := "dhall-javagen"
  )
  .dependsOn(core)

lazy val imports = project
  .in(file("imports"))
  .settings(baseSettings ++ scalaSettings)
  .settings(moduleName := "dhall-imports")
  .settings(libraryDependencies ++= catsDependencies)
  .dependsOn(parser, core)

lazy val importsMini = project
  .in(file("imports-mini"))
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
  .dependsOn(scala, importsMini)

lazy val benchmarks = project
  .in(file("benchmarks"))
  .settings(baseSettings ++ scalaSettings)
  .settings(
    skip in publish := true
  )
  .enablePlugins(JmhPlugin)
  .dependsOn(core, prelude)
