package org.dhallj.scala

import org.dhallj.core.Expr
import org.dhallj.ast._

trait Codec[A] {
  def decode(expr: Expr): Either[Codec.DecodingError, A]
  def encode(value: A): Either[Codec.EncodingError, Expr]

  def dhallType: Expr
}

object Codec {
  type Result[A] = Either[Error, A]
  type Error = Throwable
  type EncodingError = Throwable
  type DecodingError = Throwable

  def apply[A](implicit A: Codec[A]): Codec[A] = A

  implicit val codecForLong: Codec[Long] = new Codec[Long] {
    def decode(expr: Expr): Either[Codec.DecodingError, Long] =
      expr.normalize match {
        case NaturalLiteral(v) => Right(v.toLong)
        case IntegerLiteral(v) => Right(v.toLong)
        case _                 => Left(new RuntimeException("Long decoding"))
      }

    def encode(value: Long): Either[Codec.EncodingError, Expr] =
      if (value >= 0) {
        Right(NaturalLiteral(BigInt(value)))
      } else {
        Right(IntegerLiteral(BigInt(value)))
      }

    val dhallType: Expr = Expr.Constants.NATURAL
  }

  implicit def codecForOption[A: Codec]: Codec[Option[A]] = new Codec[Option[A]] {
    def decode(expr: Expr): Either[Codec.DecodingError, Option[A]] =
      expr.normalize match {
        case Application(Identifier("Some", None), value) => Codec[A].decode(value).map(Some(_))
        case Application(Identifier("None", None), _)     => Right(None)
        case _                                            => Left(new RuntimeException("Optional decoing"))
      }

    def encode(value: Option[A]): Either[Codec.EncodingError, Expr] =
      value.fold[Either[Codec.EncodingError, Expr]](Right(Application(Identifier("None"), Codec[A].dhallType)))(a =>
        Codec[A].encode(a).map(e => Application(Identifier("Some"), e))
      )

    val dhallType: Expr = Application(Identifier("Optional"), Codec[A].dhallType)
  }

  implicit def codecForList[A: Codec]: Codec[List[A]] = new Codec[List[A]] {
    def decode(expr: Expr): Either[Codec.DecodingError, List[A]] =
      expr.normalize match {
        case NonEmptyListLiteral(values) =>
          values.foldRight[Either[Codec.DecodingError, List[A]]](Right(Nil)) {
            case (v, acc) =>
              acc.flatMap(current => Codec[A].decode(v).map(_ :: current))
          }
        case EmptyListLiteral(Application(Identifier("List", None), a)) if a.equivalent(Codec[A].dhallType) =>
          Right(Nil)
        case _ => Left(new RuntimeException("List decoding"))
      }

    def encode(value: List[A]): Either[Codec.EncodingError, Expr] =
      value match {
        case Nil => Right(EmptyListLiteral(Codec[A].dhallType))
        case head :: tail =>
          tail
            .foldRight[Either[Codec.EncodingError, List[Expr]]](Right(Nil)) {
              case (v, acc) => acc.flatMap(current => Codec[A].encode(v).map(_ :: current))
            }
            .flatMap { tailExprs =>
              Codec[A].encode(head).map(headExpr => NonEmptyListLiteral(headExpr, tailExprs.toVector))
            }
      }

    val dhallType: Expr = Application(Identifier("List"), Codec[A].dhallType)
  }
}
