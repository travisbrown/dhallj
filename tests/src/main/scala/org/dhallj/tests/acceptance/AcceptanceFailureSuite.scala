package org.dhallj.tests.acceptance

import org.dhallj.core.Expr
import org.dhallj.core.binary.Decode.decode
import org.dhallj.parser.DhallParser
import scala.reflect.ClassTag

abstract class AcceptanceFailureSuite[A, E <: Throwable: ClassTag] extends AcceptanceSuite {
  def loadInput(input: Array[Byte]): A

  testInputs
    .map {
      case (name, path) => (name, readBytes(path))
    }
    .foreach {
      case (name, input) =>
        test(name) {
          intercept[E](loadInput(input))
        }
    }
}

class ParsingFailureSuite(val base: String) extends AcceptanceFailureSuite[Expr, Exception] {
  def loadInput(input: Array[Byte]): Expr = DhallParser.parse(new String(input))
}

class TypeCheckingFailureSuite(val base: String) extends AcceptanceFailureSuite[Expr, RuntimeException] {
  def loadInput(input: Array[Byte]): Expr = DhallParser.parse(new String(input)).typeCheck
}

class BinaryDecodingFailureSuite(val base: String) extends AcceptanceFailureSuite[Expr, RuntimeException] {
  override def isInputFileName(fileName: String): Boolean = fileName.endsWith(".dhallb")

  def loadInput(input: Array[Byte]): Expr = decode(input)
}
