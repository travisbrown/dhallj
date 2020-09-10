package org.dhallj.benchmarks

import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations._
import org.dhallj.core.Expr
import org.dhallj.prelude.Prelude

/**
 * Compare the performance of various ways of folding JSON values.
 *
 * The following command will run the benchmarks with reasonable settings:
 *
 * > sbt "benchmarks/jmh:run -i 10 -wi 10 -f 2 -t 1 org.dhallj.benchmarks.EncodingBenchmark"
 */
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.SECONDS)
class EncodingBenchmark {
  val prelude: Expr = Prelude.instance
  val deep: Expr = (0 to 100000).foldLeft(Expr.makeDoubleLiteral(0)) { case (acc, i) =>
    Expr.makeRecordLiteral(s"a$i", acc)
  }

  @Benchmark
  def encodePreludeToBytes: Array[Byte] = prelude.getEncodedBytes

  @Benchmark
  def encodeDeepToBytes: Array[Byte] = deep.getEncodedBytes
}
