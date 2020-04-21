package org.dhallj.imports

import org.dhallj.core.Expr

package object syntax {
  implicit def toResolveImportOps(expr: Expr): Resolver.Ops = new Resolver.Ops(expr)
}
