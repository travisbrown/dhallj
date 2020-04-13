package org.dhallj

import org.dhallj.core.DhallException.{ParsingFailure, ResolutionFailure}
import org.dhallj.core.Expr
import org.dhallj.core.typechecking.TypeCheckFailure
import org.dhallj.imports.mini.Resolver
import org.dhallj.parser.DhallParser

package object syntax {
  implicit final class DhallStringOps(val value: String) extends AnyVal {
    def parseExpr: Either[ParsingFailure, Expr] =
      try {
        Right(DhallParser.parse(value))
      } catch {
        case e: ParsingFailure => Left(e)
      }
  }

  implicit final class DhallExprOps(val expr: Expr) extends AnyVal {
    def apply(args: Expr*): Expr = Expr.makeApplication(expr, args.toArray)

    def typeCheck: Either[TypeCheckFailure, Expr] =
      try {
        Right(Expr.Util.typeCheck(expr))
      } catch {
        case e: TypeCheckFailure => Left(e)
      }

    def diff(other: Expr): Option[(Option[Expr], Option[Expr])] =
      Option(Expr.Util.getFirstDiff(expr, other)).map(entry => (Option(entry.getKey), Option(entry.getValue)))

    def resolve: Either[ResolutionFailure, Expr] =
      try {
        Right(Resolver.resolve(expr))
      } catch {
        case e: ResolutionFailure => Left(e)
      }

  }
}
