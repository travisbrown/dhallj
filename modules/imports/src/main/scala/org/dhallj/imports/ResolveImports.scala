package org.dhallj.imports

import cats.effect.Sync
import org.dhallj.core.Expr
import org.http4s.client.Client

object ResolveImports {
  def apply[F[_] <: AnyRef](expr: Expr)(implicit Client: Client[F], F: Sync[F]): F[Expr] =
    F.flatMap(ResolveImportsVisitor[F])(expr.accept(_))

  def apply[F[_] <: AnyRef](
    semanticCache: ImportCache[F],
    semiSemanticCache: ImportCache[F]
  )(expr: Expr)(implicit Client: Client[F], F: Sync[F]): F[Expr] =
    expr.accept(ResolveImportsVisitor[F](semanticCache, semiSemanticCache))

  def apply[F[_] <: AnyRef](
    semanticCache: ImportCache[F]
  )(expr: Expr)(implicit Client: Client[F], F: Sync[F]): F[Expr] =
    expr.accept(ResolveImportsVisitor[F](semanticCache, new ImportCache.NoopImportCache))

  final class Ops(val expr: Expr) extends AnyVal {
    def resolveImports[F[_] <: AnyRef](implicit Client: Client[F], F: Sync[F]): F[Expr] =
      ResolveImports.apply[F](expr)
  }
}
