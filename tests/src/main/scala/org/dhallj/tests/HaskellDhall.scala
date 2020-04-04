package org.dhallj.tests

import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets.UTF_8
import scala.sys.process._

class HaskellDhall {}
object HaskellDhall {
  def hash(input: String): String = {
    val stream = new ByteArrayInputStream(input.getBytes(UTF_8))
    Process("dhall hash").#<(stream).lazyLines.head.substring(7)
  }
}
