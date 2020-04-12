# Dhall for Java

This project is an implementation of the [Dhall][dhall-lang] configuration language for the Java
Virtual Machine.

The core modules have no external dependencies and are compatible with Java 7 and later versions.
There are also several [Scala][scala] modules that are published for Scala 2.12 and 2.13.

This project has been supported in part by [Permutive][permutive]. Please see our
[monthly reports][permutive-medium] for updates on the work of the Permutive Community Engineering
team.

## Status

We're running the [Dhall acceptance test suites][dhall-tests] for parsing, normalization, binary
decoding, hashing, and type inference, and currently 1,139 of 1,143 tests are passing.

There are several known issues:

* The parser does not support at least one known corner case.
* The parser cannot parse e.g. deeply nested records (although indefinitely long lists are fine).
* The type checker is not stack-safe (this should be fixed soon).
* In some cases printing Dhall expressions may produce invalid code.
* Import resolution is not provided in the core modules, and is a work in progress.

While we think the project is reasonably well-tested, it's very new, is sure to be full of bugs, and
nothing about the API should be considered stable at the moment. Please use responsibly.

## Getting started

The easiest way to try things out is to add the Scala wrapper module to your build.
If you're using [sbt][sbt] that would look like this:

```scala
libraryDependencies ++= "org.dhallj" %% "dhall-scala" % "0.1.0"
```

## Converting to other formats

DhallJ currently includes several ways to export Dhall expressions to other formats. The core module
includes very basic support for printing Dhall expressions as JSON:

```scala
scala> import org.dhallj.core.converters.JsonConverter
import org.dhallj.core.converters.JsonConverter

scala> import org.dhallj.parser.DhallParser.parse
import org.dhallj.parser.DhallParser.parse

scala> val expr = parse("(λ(n: Natural) → [n, n + 1, n + 2]) 100")
expr: org.dhallj.core.Expr.Parsed = (λ(n : Natural) → [n, n + 1, n + 2]) 100

scala> JsonConverter.toCompactString(expr.normalize)
res0: String = [100,101,102]
```

This conversion supports the same subset of Dhall expressions as [`dhall-to-json`][dhall-json] (e.g.
it can't produce JSON representation of functions, which means the normalization in the example
above is necessary—if we hadn't normalized the conversion would fail).

There's also a module that provides integration with [Circe][circe], allowing you to convert Dhall
expressions directly to (and from) `io.circe.Json` values without intermediate serialization to
strings:

```scala
scala> import org.dhallj.circe.Converter
import org.dhallj.circe.Converter

scala> import io.circe.syntax._
import io.circe.syntax._

scala> Converter(expr.normalize)
res0: Option[io.circe.Json] =
Some([
  100,
  101,
  102
])

scala> Converter(List(true, false).asJson)
res1: org.dhallj.core.Expr = [True, False]
```

Another module supports converting to any JSON representation for which you have a [Jawn][jawn]
facade. For example, the following build configuration would allow you to export [spray-json]
values:

```scala
libraryDependencies ++= Seq(
  "org.dhallj"    %% "dhall-jawn" % "0.1.0",
  "org.typelevel" %% "jawn-spray" % "1.0.0"
)
```

And then:

```scala
scala> import org.dhallj.jawn.JawnConverter
import org.dhallj.jawn.JawnConverter

scala> import org.typelevel.jawn.support.spray.Parser
import org.typelevel.jawn.support.spray.Parser

scala> val toSpray = new JawnConverter(Parser.facade)
toSpray: org.dhallj.jawn.JawnConverter[spray.json.JsValue] = org.dhallj.jawn.JawnConverter@be3ffe1d

scala> toSpray(expr.normalize)
res0: Option[spray.json.JsValue] = Some([100,101,102])
```

Note that unlike the dhall-circe module, the integration provided by dhall-jawn is only one way
(you can convert Dhall expressions to JSON values, but not the other way around).

We also support YAML export via [SnakeYAML][snake-yaml] (which doesn't require a Scala dependency):

```scala
scala> import org.dhallj.parser.DhallParser.parse
import org.dhallj.parser.DhallParser.parse

scala> import org.dhallj.yaml.YamlConverter
import org.dhallj.yaml.YamlConverter

scala> val expr = parse("{foo = [1, 2, 3], bar = [4, 5]}")
expr: org.dhallj.core.Expr.Parsed = {foo = [1, 2, 3], bar = [4, 5]}

scala> println(YamlConverter.toYamlString(expr))
foo:
- 1
- 2
- 3
bar:
- 4
- 5
```

## Developing

The project includes the currently-supported version of the Dhall language repository as a
submodule, so if you want to run the acceptance test suites, you'll need to clone recursively:

```bash
git clone --recurse-submodules git@github.com:travisbrown/dhallj.git
```

Or if you're like me and always forget to do this, you can initialize the submodule after cloning:

```bash
git submodule update --init
```

This project is built with [sbt][sbt], and you'll need to have sbt [installed][sbt-installation]
on your machine.

We're using the [JavaCC][javacc] parser generator for the parsing module, and we have
[our own sbt plugin][sbt-javacc] for integrating JavaCC into our build. This plugin is open source
and published to Maven Central, so you don't need to do anything to get it, but you will need to run
it manually the first time you build the project (or any time you update the JavaCC grammar):

```
sbt:root> javacc
Java Compiler Compiler Version 7.0.5 (Parser Generator)
File "Provider.java" does not exist.  Will create one.
File "StringProvider.java" does not exist.  Will create one.
File "StreamProvider.java" does not exist.  Will create one.
File "TokenMgrException.java" does not exist.  Will create one.
File "ParseException.java" does not exist.  Will create one.
File "Token.java" does not exist.  Will create one.
File "SimpleCharStream.java" does not exist.  Will create one.
Parser generated with 0 errors and 1 warnings.
[success] Total time: 0 s, completed 12-Apr-2020 08:48:53
```

After this is done, you can run the tests:

```
sbt:root> test
...
[info] Passed: Total 1319, Failed 0, Errors 0, Passed 1314, Skipped 5
[success] Total time: 36 s, completed 12-Apr-2020 08:51:07
```

Note that a few tests require the [dhall-haskell] `dhall` CLI. If you don't have it installed on
your machine, these tests will be skipped.

There are also a few additional slow tests that must be run manually:

```
sbt:root> slow:test
...
[info] Passed: Total 4, Failed 0, Errors 0, Passed 4
[success] Total time: 79 s (01:19), completed 12-Apr-2020 08:52:41
```

## Copyright and license

All code in this repository is available under the [3-Clause BSD License][bsd-license].

Copyright [Travis Brown][travisbrown] and [Tim Spence][timspence], 2020.

[bsd-license]: https://opensource.org/licenses/BSD-3-Clause
[circe]: https://github.com/circe/circe
[dhall-haskell]: https://github.com/dhall-lang/dhall-haskell
[dhall-json]: https://docs.dhall-lang.org/tutorials/Getting-started_Generate-JSON-or-YAML.html
[dhall-tests]: https://github.com/dhall-lang/dhall-lang/tree/master/tests
[dhall-lang]: https://dhall-lang.org/
[javacc]: https://javacc.github.io/javacc/
[jawn]: https://github.com/typelevel/jawn
[permutive]: https://permutive.com
[permutive-medium]: https://medium.com/permutive
[sbt]: https://www.scala-sbt.org
[sbt-installation]: https://www.scala-sbt.org/1.x/docs/Setup.html
[sbt-javacc]: https://github.com/travisbrown/sbt-javacc
[scala]: https://www.scala-lang.org
[snake-yaml]: https://bitbucket.org/asomov/snakeyaml/
[spray-json]: https://github.com/spray/spray-json
[timspence]: https://github.com/TimWSpence
[travisbrown]: https://twitter.com/travisbrown
