package org.dhallj.codec

import cats.Traverse
import cats.instances.either._
import cats.instances.vector._
import org.dhallj.core.Expr
import org.dhallj.core.typechecking.TypeCheckFailure
import org.dhallj.ast._

trait Decoder[A] { self =>
  def decode(expr: Expr): Decoder.Result[A]
  def isValidType(typeExpr: Expr): Boolean

  /**
   * Can any value of this Dhall type be decoded into this Scala type?
   */
  def isExactType(typeExpr: Expr): Boolean

  def map[B](f: A => B): Decoder[B] = new Decoder[B] {
    def decode(expr: Expr): Decoder.Result[B] = self.decode(expr).map(f)
    def isValidType(typeExpr: Expr): Boolean = self.isValidType(typeExpr)
    def isExactType(typeExpr: Expr): Boolean = self.isExactType(typeExpr)
  }
}

object Decoder {
  type Result[A] = Either[DecodingFailure, A]

  def apply[A](implicit A: Decoder[A]): Decoder[A] = A

  implicit val decodeLong: Decoder[Long] = new Decoder[Long] {
    def decode(expr: Expr): Result[Long] = expr.normalize match {
      case NaturalLiteral(v) =>
        if (v <= Long.MaxValue) {
          Right(v.toLong)
        } else {
          Left(new DecodingFailure("Long", expr))
        }
      case IntegerLiteral(v) =>
        if (v <= Long.MaxValue && v >= Long.MinValue) {
          Right(v.toLong)
        } else {
          Left(new DecodingFailure("Long", expr))
        }
      case other => Left(new DecodingFailure("Long", other))
    }

    def isValidType(typeExpr: Expr): Boolean =
      typeExpr == Expr.Constants.NATURAL || typeExpr == Expr.Constants.INTEGER

    def isExactType(typeExpr: Expr): Boolean = false
  }

  implicit val decodeInt: Decoder[Int] = new Decoder[Int] {
    def decode(expr: Expr): Result[Int] = expr.normalize match {
      case NaturalLiteral(v) =>
        if (v <= Int.MaxValue) {
          Right(v.toInt)
        } else {
          Left(new DecodingFailure("Int", expr))
        }
      case IntegerLiteral(v) =>
        if (v <= Int.MaxValue && v >= Int.MinValue) {
          Right(v.toInt)
        } else {
          Left(new DecodingFailure("Int", expr))
        }
      case other => Left(new DecodingFailure("Int", other))
    }

    def isValidType(typeExpr: Expr): Boolean =
      typeExpr == Expr.Constants.NATURAL || typeExpr == Expr.Constants.INTEGER

    def isExactType(typeExpr: Expr): Boolean = false
  }

  implicit val decodeBigInt: Decoder[BigInt] = new Decoder[BigInt] {
    def decode(expr: Expr): Result[BigInt] = expr.normalize match {
      case NaturalLiteral(v) => Right(v)
      case IntegerLiteral(v) => Right(v)
      case other             => Left(new DecodingFailure("BigInt", other))
    }

    def isValidType(typeExpr: Expr): Boolean =
      typeExpr == Expr.Constants.NATURAL || typeExpr == Expr.Constants.INTEGER

    def isExactType(typeExpr: Expr): Boolean = typeExpr == Expr.Constants.INTEGER
  }

  implicit val decodeDouble: Decoder[Double] = new Decoder[Double] {
    def decode(expr: Expr): Result[Double] = expr.normalize match {
      case DoubleLiteral(v)  => Right(v)
      case NaturalLiteral(v) => Right(v.toDouble)
      case IntegerLiteral(v) => Right(v.toDouble)
      case other             => Left(new DecodingFailure("Double", other))
    }

    def isValidType(typeExpr: Expr): Boolean =
      typeExpr == Expr.Constants.DOUBLE ||
        typeExpr == Expr.Constants.NATURAL ||
        typeExpr == Expr.Constants.INTEGER

    def isExactType(typeExpr: Expr): Boolean = isValidType(typeExpr)
  }

  implicit val decodeString: Decoder[String] = new Decoder[String] {
    def decode(expr: Expr): Result[String] = expr.normalize match {
      case TextLiteral((v, Vector())) => Right(v)
      case other                      => Left(new DecodingFailure("String", other))
    }

    def isValidType(typeExpr: Expr): Boolean = typeExpr == Expr.Constants.TEXT
    def isExactType(typeExpr: Expr): Boolean = false
  }

  implicit val decodeBoolean: Decoder[Boolean] = new Decoder[Boolean] {
    def decode(expr: Expr): Result[Boolean] = expr.normalize match {
      case BoolLiteral(v) => Right(v)
      case other          => Left(new DecodingFailure("Boolean", other))
    }

    def isValidType(typeExpr: Expr): Boolean = typeExpr == Expr.Constants.BOOL
    def isExactType(typeExpr: Expr): Boolean = isValidType(typeExpr)
  }

  implicit def decodeOption[A: Decoder]: Decoder[Option[A]] = new Decoder[Option[A]] {
    def decode(expr: Expr): Result[Option[A]] = expr.normalize match {
      case Application(Expr.Constants.SOME, value) => Decoder[A].decode(value).map(Some(_))
      case Application(Expr.Constants.NONE, elementType) if Decoder[A].isValidType(elementType) =>
        Right(None)
      case other => Left(new DecodingFailure("Optional", other))
    }

    def isValidType(typeExpr: Expr): Boolean = typeExpr match {
      case Application(Expr.Constants.OPTIONAL, elementType) => Decoder[A].isValidType(elementType)
      case _                                                 => false
    }

    def isExactType(typeExpr: Expr): Boolean = typeExpr match {
      case Application(Expr.Constants.OPTIONAL, elementType) => Decoder[A].isExactType(elementType)
      case _                                                 => false
    }
  }

  implicit def decodeVector[A: Decoder]: Decoder[Vector[A]] = new Decoder[Vector[A]] {
    def decode(expr: Expr): Result[Vector[A]] = expr.normalize match {
      case NonEmptyListLiteral(values) =>
        Traverse[Vector].traverse(values)(Decoder[A].decode)
      case EmptyListLiteral(Application(Expr.Constants.LIST, elementType)) if Decoder[A].isValidType(elementType) =>
        Right(Vector.empty)
      case other => Left(new DecodingFailure("Vector", other))
    }

    def isValidType(typeExpr: Expr): Boolean = typeExpr match {
      case Application(Expr.Constants.LIST, elementType) => Decoder[A].isValidType(elementType)
      case _                                             => false
    }

    def isExactType(typeExpr: Expr): Boolean = typeExpr match {
      case Application(Expr.Constants.LIST, elementType) => Decoder[A].isExactType(elementType)
      case _                                             => false
    }
  }

  implicit def decodeList[A: Decoder]: Decoder[List[A]] = new Decoder[List[A]] {
    def decode(expr: Expr): Result[List[A]] = expr.normalize match {
      case NonEmptyListLiteral(values) =>
        Traverse[Vector].traverse(values)(Decoder[A].decode).map(_.toList)
      case EmptyListLiteral(Application(Expr.Constants.LIST, elementType)) if Decoder[A].isValidType(elementType) =>
        Right(Nil)
      case other => Left(new DecodingFailure("List", other))
    }

    def isValidType(typeExpr: Expr): Boolean = typeExpr match {
      case Application(Expr.Constants.LIST, elementType) => Decoder[A].isValidType(elementType)
      case _                                             => false
    }

    def isExactType(typeExpr: Expr): Boolean = typeExpr match {
      case Application(Expr.Constants.LIST, elementType) => Decoder[A].isExactType(elementType)
      case _                                             => false
    }
  }

  implicit def decodeFunction1[A: Encoder, B: Decoder]: Decoder[A => B] = new Decoder[A => B] {
    def decode(expr: Expr): Result[A => B] = expr.normalize match {
      case f @ Lambda(_, inputType, result) if Encoder[A].isExactType(inputType) =>
        val inferredType =
          try {
            Right(Expr.Util.typeCheck(f))
          } catch {
            case e: TypeCheckFailure =>
              Left(new DecodingFailure("Function1", f))
          }

        inferredType.flatMap {
          case Pi(_, inputType, outputType)
              if Encoder[A].isExactType(inputType) && Decoder[B].isExactType(outputType) =>
            // This is still a little hand-wavy.
            Right((a: A) => Decoder[B].decode(Application(f, Encoder[A].encode(a)).normalize).toOption.get)

          case _ => Left(new DecodingFailure("Function1", f))
        }

      case other => Left(new DecodingFailure("Function1", other))
    }

    def isValidType(typeExpr: Expr): Boolean = typeExpr match {
      case Pi(_, inputType, outputType) =>
        Encoder[A].isExactType(inputType) && Decoder[B].isValidType(outputType)
      case _ => false
    }

    def isExactType(typeExpr: Expr): Boolean = typeExpr match {
      case Pi(_, inputType, outputType) =>
        Encoder[A].isExactType(inputType) && Decoder[B].isExactType(outputType)
      case _ => false
    }
  }
}
