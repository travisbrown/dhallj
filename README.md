# Dhall for Java

[![Build status](https://img.shields.io/github/workflow/status/travisbrown/dhallj/Continuous%20Integration.svg)](https://github.com/travisbrown/dhallj/actions)
[![Gitter](https://img.shields.io/badge/gitter-join%20chat-green.svg)](https://gitter.im/dhallj/)
[![Maven Central](https://img.shields.io/maven-central/v/org.dhallj/dhall-core.svg)](https://maven-badges.herokuapp.com/maven-central/org.dhallj/dhall-core)

This project is an implementation of the [Dhall][dhall-lang] configuration language for the Java
Virtual Machine.

Our goal for this project is to make it as easy as possible to integrate Dhall
into JVM build systems (see the [dhall-kubernetes] demonstration
[below](#converting-to-other-formats) for a concrete example of why you might want to do this).

The core modules have no external dependencies, are Java 7-compatible, and are fairly minimal:

```bash
$ du -h modules/core/target/dhall-core-0.10.0-M1.jar
168K    modules/core/target/dhall-core-0.10.0-M1.jar

$ du -h modules/parser/target/dhall-parser-0.10.0-M1.jar
108K    modules/parser/target/dhall-parser-0.10.0-M1.jar
```

There are also several [Scala][scala] modules that are published for Scala 2.12,
2.13, and 3.0. While most of the examples in this README are focused on Scala, you
shouldn't need to know or care about Scala to use the core DhallJ modules.

The initial development of this project was supported in part by [Permutive][permutive].

## Table of contents

* [Status](#status)
* [Getting started](#getting-started)
* [Converting to other formats](#converting-to-other-formats)
* [Import resolution](#import-resolution)
* [Command-line interface](#command-line-interface)
* [Other stuff](#other-stuff)
* [Developing](#developing)
* [Community](#community)
* [Copyright and license](#copyright-and-license)

## Status

The current release of this project supports [Dhall 21.0.0][dhall-21-0-0].
We're running the [Dhall acceptance test suites][dhall-tests] for parsing, normalization,
[CBOR][cbor] encoding and decoding, hashing, and type inference, and
currently all tests are passing (with three exception; see the [0.10.0-M1 release notes for details](https://github.com/travisbrown/dhallj/releases/tag/v0.10.0-M1)).

There are several known issues:

* The parser [cannot parse deeply nested structures](https://github.com/travisbrown/dhallj/issues/2) (records, etc., although note that indefinitely long lists are fine).
* The type checker is [also not stack-safe](https://github.com/travisbrown/dhallj/issues/3) (this should be fixed soon).
* Import resolution is not provided in the core modules, and is a work in progress.

While we think the project is reasonably well-tested, it's very new, is sure to be full of bugs, and
nothing about the API should be considered stable at the moment. Please use responsibly.

## Getting started

The easiest way to try things out is to add the Scala wrapper module to your build.
If you're using [sbt][sbt] that would look like this:

```scala
libraryDependencies += "org.dhallj" %% "dhall-scala" % "0.10.0-M1"
```

This dependency includes two packages: `org.dhallj.syntax` and `org.dhallj.ast`.

The `syntax` package provides some extension methods, including a `parseExpr`
method for strings (note that this method returns an
`Either[ParsingFailure, Expr]`, which we unwrap here with `Right`):

```scala
scala> import org.dhallj.syntax._
import org.dhallj.syntax._

scala> val Right(expr) = "\\(n: Natural) -> [n + 0, n + 1, 1 + 1]".parseExpr
expr: org.dhallj.core.Expr = λ(n : Natural) → [n + 0, n + 1, 1 + 1]
```

Now that we have a Dhall expression, we can type-check it:

```scala
scala> val Right(exprType) = expr.typeCheck
exprType: org.dhallj.core.Expr = ∀(n : Natural) → List Natural
```

We can "reduce" (or _β-normalize_) it:

```scala
scala> val normalized = expr.normalize
normalized: org.dhallj.core.Expr = λ(n : Natural) → [n, n + 1, 2]
```

We can also _α-normalize_ it, which replaces all named variables with
indexed underscores:

```scala
scala> val alphaNormalized = normalized.alphaNormalize
alphaNormalized: org.dhallj.core.Expr = λ(_ : Natural) → [_, _ + 1, 2]
```

We can encode it as a CBOR byte array:

```scala
scala> alphaNormalized.getEncodedBytes
res0: Array[Byte] = Array(-125, 1, 103, 78, 97, 116, 117, 114, 97, 108, -123, 4, -10, 0, -124, 3, 4, 0, -126, 15, 1, -126, 15, 2)
```

And we can compute its semantic hash:

```scala
scala> alphaNormalized.hash
res1: String = c57cdcdae92638503f954e63c0b3ae8de00a59bc5e05b4dd24e49f42aca90054
```

If we have the official `dhall` CLI installed, we can confirm that this hash is
correct:

```bash
$ dhall hash <<< '\(n: Natural) -> [n + 0, n + 1, 1 + 1]'
sha256:c57cdcdae92638503f954e63c0b3ae8de00a59bc5e05b4dd24e49f42aca90054
```

We can also compare expressions:

```scala
scala> val Right(other) = "\\(n: Natural) -> [n, n + 1, 3]".parseExpr
other: org.dhallj.core.Expr = λ(n : Natural) → [n, n + 1, 3]

scala> normalized == other
res2: Boolean = false

scala> val Some(diff) = normalized.diff(other)
diff: (Option[org.dhallj.core.Expr], Option[org.dhallj.core.Expr]) = (Some(2),Some(3))
```

And apply them to other expressions:

```scala
scala> val Right(arg) = "10".parseExpr
arg: org.dhallj.core.Expr = 10

scala> expr(arg)
res3: org.dhallj.core.Expr = (λ(n : Natural) → [n + 0, n + 1, 1 + 1]) 10

scala> expr(arg).normalize
res4: org.dhallj.core.Expr = [10, 11, 2]
```

We can also resolve expressions containing imports (although at the moment
dhall-scala doesn't support remote imports or caching; please see the
[section on import resolution](#import-resolution) below for details about
how to set up remote import resolution if you need it):

```scala
val Right(enumerate) =
     |   "./dhall-lang/Prelude/Natural/enumerate".parseExpr.flatMap(_.resolve)
enumerate: org.dhallj.core.Expr = let enumerate : Natural → List Natural = ...

scala> enumerate(arg).normalize
res5: org.dhallj.core.Expr = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
```

Note that we're working with values of type `Expr`, which comes from dhall-core,
which is a Java module. The `Expr` class includes static methods for creating
`Expr` values:

```scala
scala> import org.dhallj.core.Expr
import org.dhallj.core.Expr

scala> Expr.makeTextLiteral("foo")
res6: org.dhallj.core.Expr = "foo"

scala> Expr.makeEmptyListLiteral(Expr.Constants.BOOL)
res7: org.dhallj.core.Expr = [] : Bool
```

If you're working from Scala, though, you're generally better off using the
constructors included in the `org.dhallj.ast` package, which provide more
type-safety:

```scala
scala> TextLiteral("foo")
res8: org.dhallj.core.Expr = "foo"

scala> NonEmptyListLiteral(BoolLiteral(true), Vector())
res9: org.dhallj.core.Expr = [True]
```

The `ast` package also includes extractors that let you pattern match on
`Expr` values:

```scala
scala> expr match {
     |   case Lambda(name, _, NonEmptyListLiteral(first +: _)) => (name, first)
     | }
res10: (String, org.dhallj.core.Expr) = (n,n + 0)
```

Note that we don't have exhaustivity checking for these extractors, although we
might be able to add that in an eventual Dotty version.

In addition to dhall-scala, there's a (more experimental) dhall-scala-codec
module, which supports encoding and decoding Scala types to and from Dhall expressions.
If you add it to your build, you can write the following:

```scala
scala> import org.dhallj.codec.syntax._
import org.dhallj.codec.syntax._

scala> List(List(1, 2), Nil, List(3, -4)).asExpr
res0: org.dhallj.core.Expr = [[+1, +2], [] : List Integer, [+3, -4]]
```

You can even decode Dhall functions into Scala functions (assuming you have the
appropriate codecs for the input and output types):

```scala
val Right(f) = """

  let enumerate = ./dhall-lang/Prelude/Natural/enumerate

  let map = ./dhall-lang/Prelude/List/map

  in \(n: Natural) ->
    map Natural Integer Natural/toInteger (enumerate n)

""".parseExpr.flatMap(_.resolve)
```
And then:

```scala
scala> val Right(scalaEnumerate) = f.as[BigInt => List[BigInt]]
scalaEnumerate: BigInt => List[BigInt] = org.dhallj.codec.Decoder$$anon$11$$Lambda$15614/0000000050B06E20@94b036

scala> scalaEnumerate(BigInt(3))
res1: List[BigInt] = List(0, 1, 2)
```

Eventually we'll probably support generic derivation for encoding Dhall
expressions to and from algebraic data types in Scala, but we haven't
implemented this yet.

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
  "org.dhallj"    %% "dhall-jawn" % "0.4.0",
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

You can use the YAML exporter with [dhall-kubernetes], for example. Instead of
maintaining a lot of verbose and repetitive and error-prone YAML files, you can
keep your configuration in well-typed Dhall files (like
[this example](https://github.com/dhall-lang/dhall-kubernetes/blob/506d633e382872346927b8cb9884d8b7382e6cab/1.17/examples/deploymentSimple.dhall))
and have your build system export them to YAML:

```scala
import org.dhallj.syntax._, org.dhallj.yaml.YamlConverter

val kubernetesExamplePath = "../dhall-kubernetes/1.17/examples/deploymentSimple.dhall"
val Right(kubernetesExample) = kubernetesExamplePath.parseExpr.flatMap(_.resolve)
```

And then:

```scala
scala> println(YamlConverter.toYamlString(kubernetesExample.normalize))
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nginx
spec:
  replicas: 2
  selector:
    matchLabels:
      name: nginx
  template:
    metadata:
      name: nginx
    spec:
      containers:
      - image: nginx:1.15.3
        name: nginx
        ports:
        - containerPort: 80
```

It's not currently possible to convert to YAML without the SnakeYAML dependency, although we may support a simplified
version of this in the future (something similar to what we have for JSON in the core module).

## Import resolution

There are currently two modules that implement import resolution (to different degrees).

### dhall-imports

The first is dhall-imports, which is a Scala library built on [cats-effect] that uses [http4s] for
its HTTP client. This module is intended to be a complete implementation of the
[import resolution and caching specification][dhall-imports].

It requires a bit of ceremony to set up:

```scala
import cats.effect.{IO, Resource}
import org.dhallj.core.Expr
import org.dhallj.imports.syntax._
import org.dhallj.parser.DhallParser
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.client.Client
import scala.concurrent.ExecutionContext

val client: Resource[IO, Client[IO]] = BlazeClientBuilder[IO](ExecutionContext.global).resource
```

And then if we have some definitions like this:

```scala
val concatSepImport = DhallParser.parse("https://prelude.dhall-lang.org/Text/concatSep")

val parts = DhallParser.parse("""["foo", "bar", "baz"]""")
val delimiter = Expr.makeTextLiteral("-")
```

We can use them with a function from the Dhall Prelude like this:

```scala
scala> val resolved = client.use { implicit c =>
     |   concatSepImport.resolveImports[IO]
     | }
resolved: cats.effect.IO[org.dhallj.core.Expr] = IO(...)

scala> import cats.effect.unsafe.implicits.global
import cats.effect.unsafe.implicits.global

scala> val result = resolved.map { concatSep =>
     |   Expr.makeApplication(concatSep, Array(delimiter, parts)).normalize
     | }
result: cats.effect.IO[org.dhallj.core.Expr] = IO(...)

scala> result.unsafeRunSync()
res0: org.dhallj.core.Expr = "foo-bar-baz"
```

(Note that we could use dhall-scala to avoid the use of `Array` above.)

#### Classpath imports

We support an extension of the spec which allows you to also import expressions
from the classpath using the syntax `let e = classpath:/absolute/path/to/file in e`.
The semantics are subject to change as we get more experience with it but
currently it should generally have the same behaviour as an absolute
path import of a local file (but files on the classpath can import each other
using relative paths). This includes it being protected by the referential
sanity check so that remote imports cannot exfiltrate information
from the classpath.

Also note that classpath imports as location are currently not supported as the spec
requires that an import as Location must return an expression of type
`<Local Text | Remote Text | Environment Text | Missing>`.

### dhall-imports-mini

The other implementation is dhall-imports-mini, which is a Java library that
depends only on the core and parser modules, but that doesn't support
remote imports or caching.

The previous example could be rewritten as follows using dhall-imports-mini
and a local copy of the Prelude:

```scala
import org.dhallj.core.Expr
import org.dhallj.imports.mini.Resolver
import org.dhallj.parser.DhallParser

val concatSep = Resolver.resolve(DhallParser.parse("./dhall-lang/Prelude/Text/concatSep"), false)

val parts = DhallParser.parse("""["foo", "bar", "baz"]""")
val delimiter = Expr.makeTextLiteral("-")
```

And then:

```scala
scala> Expr.makeApplication(concatSep, Array(delimiter, parts)).normalize
res0: org.dhallj.core.Expr = "foo-bar-baz"
```

It's likely that eventually we'll provide a complete pure-Java implementation of import resolution,
but this isn't currently a high priority for us.

## Command-line interface

We include a command-line interface that supports some common operations. It's currently similar to
the official `dhall` and `dhall-to-json` binaries, but with many fewer options.

If [GraalVM Native Image][graal-native-image] is available on your system, you can build the CLI as
a native binary (thanks to [sbt-native-packager]).

```bash
$ sbt cli/graalvm-native-image:packageBin

$ cd cli/target/graalvm-native-image/

$ du -h dhall-cli
8.2M    dhall-cli

$ time ./dhall-cli hash --normalize --alpha <<< "λ(n: Natural) → [n, n + 1]"
sha256:a8d9326812aaabeed29412e7b780dc733b1e633c5556c9ea588e8212d9dc48f3

real    0m0.009s
user    0m0.000s
sys     0m0.009s

$ time ./dhall-cli type <<< "{foo = [1, 2, 3]}"
{foo : List Natural}

real    0m0.003s
user    0m0.000s
sys     0m0.003s

$ time ./dhall-cli json <<< "{foo = [1, 2, 3]}"
{"foo":[1,2,3]}

real    0m0.005s
user    0m0.004s
sys     0m0.001s
```

Even on the JVM it's close to usable, although you can definitely feel the slow startup:

```bash
$ cd ..

$ time java -jar ./cli-assembly-0.4.0-SNAPSHOT.jar hash --normalize --alpha <<< "λ(n: Natural) → [n, n + 1]"
sha256:a8d9326812aaabeed29412e7b780dc733b1e633c5556c9ea588e8212d9dc48f3

real    0m0.104s
user    0m0.106s
sys     0m0.018s
```

There's probably not really any reason you'd want to use `dhall-cli` right now, but I think it's a
pretty neat demonstration of how Graal can make Java (or Scala) a viable language for building
native CLI applications.

## Other stuff

### dhall-testing

The dhall-testing module provides support for property-based testing with [ScalaCheck][scalacheck]
in the form of `Arbitrary` (and `Shrink`) instances:

```scala
scala> import org.dhallj.core.Expr
import org.dhallj.core.Expr

scala> import org.dhallj.testing.instances._
import org.dhallj.testing.instances._

scala> import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary

scala> Arbitrary.arbitrary[Expr].sample
res0: Option[org.dhallj.core.Expr] = Some(Optional (Optional (List Double)))

scala> Arbitrary.arbitrary[Expr].sample
res1: Option[org.dhallj.core.Expr] = Some(Optional (List <neftfEahtuSq : Double | kg...
```

It includes (fairly basic) support for producing both well-typed and probably-not-well-typed
expressions, and for generating arbitrary values of specified Dhall types:

```scala
scala> import org.dhallj.testing.WellTypedExpr
import org.dhallj.testing.WellTypedExpr

scala> Arbitrary.arbitrary[WellTypedExpr].sample
res2: Option[org.dhallj.testing.WellTypedExpr] = Some(WellTypedExpr(8436008296256993755))

scala> genForType(Expr.Constants.BOOL).flatMap(_.sample)
res3: Option[org.dhallj.core.Expr] = Some(True)

scala> genForType(Expr.Constants.BOOL).flatMap(_.sample)
res4: Option[org.dhallj.core.Expr] = Some(False)

scala> genForType(Expr.makeApplication(Expr.Constants.LIST, Expr.Constants.INTEGER)).flatMap(_.sample)
res5: Option[org.dhallj.core.Expr] = Some([+1522471910085416508, -9223372036854775809, ...
```

This module is currently fairly minimal, and is likely to change substantially in future releases.

### dhall-javagen and dhall-prelude

The dhall-javagen module lets you take a DhallJ representation of a Dhall expression and use it to
generate Java code that will build the DhallJ representation of that expression.

This is mostly a toy, but it allows us for example to distribute a "pre-compiled" jar containing the
Dhall Prelude:

```scala
scala> import java.math.BigInteger
import java.math.BigInteger

scala> import org.dhallj.core.Expr
import org.dhallj.core.Expr

scala> val ten = Expr.makeNaturalLiteral(new BigInteger("10"))
ten: org.dhallj.core.Expr = 10

scala> val Prelude = org.dhallj.prelude.Prelude.instance
Prelude: org.dhallj.core.Expr = ...

scala> val Natural = Expr.makeFieldAccess(Prelude, "Natural")
Natural: org.dhallj.core.Expr = ...

scala> val enumerate = Expr.makeFieldAccess(Natural, "enumerate")
enumerate: org.dhallj.core.Expr = ...

scala> Expr.makeApplication(enumerate, ten).normalize
res0: org.dhallj.core.Expr = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
```

Note that the resulting jar (which is available from Maven Central as dhall-prelude) is many times
smaller than either the Prelude source or the Prelude serialized as CBOR.

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

## Community

This project supports the [Scala code of conduct][code-of-conduct] and wants all of its channels
(Gitter, GitHub, etc.) to be inclusive environments.

## Copyright and license

All code in this repository is available under the [3-Clause BSD License][bsd-license].

Copyright [Travis Brown][travisbrown] and [Tim Spence][timspence], 2020.

[bsd-license]: https://opensource.org/licenses/BSD-3-Clause
[cats-effect]: https://github.com/typelevel/cats-effect
[cbor]: https://cbor.io/
[circe]: https://github.com/circe/circe
[code-of-conduct]: https://www.scala-lang.org/conduct/
[dhall-21-0-0]: https://github.com/dhall-lang/dhall-lang/pull/1194
[dhall-haskell]: https://github.com/dhall-lang/dhall-haskell
[dhall-imports]: https://github.com/dhall-lang/dhall-lang/blob/master/standard/imports.md
[dhall-json]: https://docs.dhall-lang.org/tutorials/Getting-started_Generate-JSON-or-YAML.html
[dhall-kubernetes]: https://github.com/dhall-lang/dhall-kubernetes
[dhall-tests]: https://github.com/dhall-lang/dhall-lang/tree/master/tests
[dhall-lang]: https://dhall-lang.org/
[discipline]: https://github.com/typelevel/discipline
[graal-native-image]: https://www.graalvm.org/docs/reference-manual/native-image/
[http4s]: https://http4s.org
[javacc]: https://javacc.github.io/javacc/
[jawn]: https://github.com/typelevel/jawn
[permutive]: https://permutive.com
[permutive-medium]: https://medium.com/permutive
[sbt]: https://www.scala-sbt.org
[sbt-installation]: https://www.scala-sbt.org/1.x/docs/Setup.html
[sbt-javacc]: https://github.com/travisbrown/sbt-javacc
[sbt-native-packager]: https://github.com/sbt/sbt-native-packager
[scala]: https://www.scala-lang.org
[scalacheck]: https://www.scalacheck.org/
[snake-yaml]: https://bitbucket.org/asomov/snakeyaml/
[spray-json]: https://github.com/spray/spray-json
[timspence]: https://github.com/TimWSpence
[travisbrown]: https://twitter.com/travisbrown
