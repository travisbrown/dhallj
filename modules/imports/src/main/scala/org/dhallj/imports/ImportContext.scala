package org.dhallj.imports

import java.net.URI
import java.nio.file.Path
import org.dhallj.core.Expr

sealed abstract class ImportContext extends Product with Serializable

object ImportContext {
  case object Missing extends ImportContext
  case class Env(value: String) extends ImportContext
  case class Local(absolutePath: Path) extends ImportContext
  case class Classpath(absolutePath: Path) extends ImportContext
  case class Remote(url: URI, using: Expr) extends ImportContext
}
