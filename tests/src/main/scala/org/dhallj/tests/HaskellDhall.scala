package org.dhallj.tests

import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets.UTF_8
import scala.sys.process._
import scala.util.Try

class HaskellDhall {}
object HaskellDhall {
  def isAvailable(): Boolean =
    Try(Process("dhall --version").lineStream.head.split("\\.")).toOption.exists(_.length == 3)

  def hash(input: String): String = {
    val stream = new ByteArrayInputStream(input.getBytes(UTF_8))
    Process("dhall hash").#<(stream).lineStream.head.substring(7)
  }
}
