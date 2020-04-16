package org.dhallj.benchmarks

import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations._
import org.dhallj.core.Expr
import org.dhallj.parser.DhallParser
import org.dhallj.prelude.Prelude

/**
 * Compare the performance of various ways of folding JSON values.
 *
 * The following command will run the benchmarks with reasonable settings:
 *
 * > sbt "benchmarks/jmh:run -i 10 -wi 10 -f 2 -t 1 org.dhallj.benchmarks.ParsingBenchmark"
 */
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.SECONDS)
class ParsingBenchmark {
  val preludeAsString = Prelude.instance.toString

  @Benchmark
  def parsePrelude: Expr = DhallParser.parse(preludeAsString)
}
