package org.dhallj.imports

import org.dhallj.imports.ResolutionConfig.LocalMode

case class ResolutionConfig(
  localMode: LocalMode,
  //This should only be false for testing prelude, where it is too slow
  shouldTypeCheck: Boolean = true
)

object ResolutionConfig {

  sealed trait LocalMode

  case object FromFileSystem extends LocalMode

  case object FromResources extends LocalMode

}
