package org.dhallj

import org.dhallj.core.Expr

package object javagen {
  def toJavaCode(expr: Expr, packageName: String, className: String): String =
    expr.accept(ToCodeVisitor.instance).toClassDef(packageName, className)
}
