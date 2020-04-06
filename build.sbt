organization in ThisBuild := "org.dhallj"

val testDependencies = Seq(
  "org.scalacheck" %% "scalacheck" % "1.14.3",
  "org.scalameta" %% "munit" % "0.7.1"
)

val catsDependencies = Seq(
  "org.typelevel" %% "cats-core" % "2.0.0",
  "org.typelevel" %% "cats-effect" % "2.1.2",
  "org.http4s" %% "http4s-dsl" % "0.21.1",
  "org.http4s" %% "http4s-blaze-client" % "0.21.1"
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
    initialCommands in console := "import org.dhallj.parser.Dhall.parse"
  )
  .aggregate(core, parser, javagen, demo, scala, tests)
  .dependsOn(importsMini, scala, javagen)

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
    sources in (Compile, doc) := Nil
  )
  .enablePlugins(JavaCCPlugin)
  .dependsOn(core)

lazy val demo = project
  .in(file("demo"))
  .settings(baseSettings ++ javaSettings)
  .settings(
    skip in publish := true
  )
  .enablePlugins(GraalVMNativeImagePlugin)
  .dependsOn(parser)

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

lazy val tests = project
  .in(file("tests"))
  .settings(baseSettings ++ scalaSettings)
  .settings(
    libraryDependencies ++= testDependencies,
    skip in publish := true,
    Test / unmanagedResourceDirectories += (ThisBuild / baseDirectory).value / "dhall-lang"
  )
  .dependsOn(scala, importsMini)
