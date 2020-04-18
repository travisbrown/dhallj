package org.dhallj

import org.dhallj.core.Expr

package object imports {
  implicit def toResolveImportOps(expr: Expr): ResolveImports.Ops = new ResolveImports.Ops(expr)
}
