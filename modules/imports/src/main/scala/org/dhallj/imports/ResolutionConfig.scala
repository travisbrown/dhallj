package org.dhallj.imports

import org.dhallj.imports.ResolutionConfig.LocalMode

case class ResolutionConfig(
  localMode: LocalMode
)

object ResolutionConfig {

  sealed trait LocalMode

  case object FromFileSystem extends LocalMode

  case object FromResources extends LocalMode

}
