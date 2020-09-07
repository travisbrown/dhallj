package org.dhallj.imports

import org.dhallj.core.Expr
import scala.language.implicitConversions

package object syntax {
  implicit def toResolveImportOps(expr: Expr): Resolver.Ops = new Resolver.Ops(expr)
}
