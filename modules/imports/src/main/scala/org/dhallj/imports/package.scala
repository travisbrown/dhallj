package org.dhallj

import _root_.cats.effect.Sync
import _root_.cats.implicits._
import org.dhallj.core.Expr
import org.http4s.client._

package object imports {

  implicit class ResolveImports(e: Expr) {
    def resolveImports[F[_] <: AnyRef](implicit Client: Client[F], F: Sync[F]): F[Expr] =
      ResolveImportsVisitor.mkVisitor >>= (v => e.accept(v))
  }

}
