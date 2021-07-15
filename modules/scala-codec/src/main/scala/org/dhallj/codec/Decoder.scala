package org.dhallj.codec

import cats.Traverse
import cats.instances.either._
import cats.instances.vector._
import org.dhallj.ast._
import org.dhallj.core.Expr
import org.dhallj.core.typechecking.TypeCheckFailure

trait Decoder[A] { self =>
  def decode(c: HCursor): Decoder.Result[A]
  def isValidType(typeExpr: Expr): Boolean

  /**
   * Can any value of this Dhall type be decoded into this Scala type?
   */
  def isExactType(typeExpr: Expr): Boolean

  def map[B](f: A => B): Decoder[B] = new Decoder[B] {
    def decode(c: HCursor): Decoder.Result[B] = self.decode(c).map(f)
    def isValidType(typeExpr: Expr): Boolean = self.isValidType(typeExpr)
    def isExactType(typeExpr: Expr): Boolean = self.isExactType(typeExpr)
  }

  def tryDecode(c: ACursor): Decoder.Result[A] = c match {
    case h: HCursor => decode(h)
    case _ =>
      Left(new DecodingFailure("Attempt to decode value on failed cursor", None, c.history))
  }
}

object Decoder {
  type Result[A] = Either[DecodingFailure, A]

  def apply[A](implicit A: Decoder[A]): Decoder[A] = A

  implicit val decodeLong: Decoder[Long] = new Decoder[Long] {
    override def decode(c: HCursor): Result[Long] = c.expr.normalize match {
      case NaturalLiteral(v) =>
        if (v <= Long.MaxValue) {
          Right(v.toLong)
        } else {
          Left(DecodingFailure.failedTarget("Long", c.expr, c.history))
        }
      case IntegerLiteral(v) =>
        if (v <= Long.MaxValue && v >= Long.MinValue) {
          Right(v.toLong)
        } else {
          Left(DecodingFailure.failedTarget("Long", c.expr, c.history))
        }
      case other => Left(DecodingFailure.failedTarget("Long", other, c.history))
    }

    def isValidType(typeExpr: Expr): Boolean =
      typeExpr == Expr.Constants.NATURAL || typeExpr == Expr.Constants.INTEGER

    def isExactType(typeExpr: Expr): Boolean = false
  }

  implicit val decodeInt: Decoder[Int] = new Decoder[Int] {
    def decode(c: HCursor): Result[Int] = c.expr.normalize match {
      case NaturalLiteral(v) =>
        if (v <= Int.MaxValue) {
          Right(v.toInt)
        } else {
          Left(DecodingFailure.failedTarget("Int", c.expr, c.history))
        }
      case IntegerLiteral(v) =>
        if (v <= Int.MaxValue && v >= Int.MinValue) {
          Right(v.toInt)
        } else {
          Left(DecodingFailure.failedTarget("Int", c.expr, c.history))
        }
      case other => Left(DecodingFailure.failedTarget("Int", other, c.history))
    }

    def isValidType(typeExpr: Expr): Boolean =
      typeExpr == Expr.Constants.NATURAL || typeExpr == Expr.Constants.INTEGER

    def isExactType(typeExpr: Expr): Boolean = false
  }

  implicit val decodeBigInt: Decoder[BigInt] = new Decoder[BigInt] {
    def decode(c: HCursor): Result[BigInt] = c.expr.normalize match {
      case NaturalLiteral(v) => Right(v)
      case IntegerLiteral(v) => Right(v)
      case other             => Left(DecodingFailure.failedTarget("BigInt", other, c.history))
    }

    def isValidType(typeExpr: Expr): Boolean =
      typeExpr == Expr.Constants.NATURAL || typeExpr == Expr.Constants.INTEGER

    def isExactType(typeExpr: Expr): Boolean = typeExpr == Expr.Constants.INTEGER
  }

  implicit val decodeDouble: Decoder[Double] = new Decoder[Double] {
    def decode(c: HCursor): Result[Double] = c.expr.normalize match {
      case DoubleLiteral(v)  => Right(v)
      case NaturalLiteral(v) => Right(v.toDouble)
      case IntegerLiteral(v) => Right(v.toDouble)
      case other             => Left(DecodingFailure.failedTarget("Double", other, c.history))
    }

    def isValidType(typeExpr: Expr): Boolean =
      typeExpr == Expr.Constants.DOUBLE ||
        typeExpr == Expr.Constants.NATURAL ||
        typeExpr == Expr.Constants.INTEGER

    def isExactType(typeExpr: Expr): Boolean = isValidType(typeExpr)
  }

  implicit val decodeString: Decoder[String] = new Decoder[String] {
    def decode(c: HCursor): Result[String] = c.expr.normalize match {
      case TextLiteral((v, Vector())) => Right(v)
      case other                      => Left(DecodingFailure.failedTarget("String", other, c.history))
    }

    def isValidType(typeExpr: Expr): Boolean = typeExpr == Expr.Constants.TEXT
    def isExactType(typeExpr: Expr): Boolean = false
  }

  implicit def decodeOption[A: Decoder]: Decoder[Option[A]] = new Decoder[Option[A]] {
    def decode(c: HCursor): Result[Option[A]] = c.expr.normalize match {
      case Application(Expr.Constants.SOME, value) => Decoder[A].decode(HCursor.fromExpr(value)).map(Some(_))
      case Application(Expr.Constants.NONE, elementType) if Decoder[A].isValidType(elementType) =>
        Right(None)
      case other => Left(DecodingFailure.failedTarget("Optional", other, c.history))
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
    def decode(c: HCursor): Result[Vector[A]] = c.expr.normalize match {
      case NonEmptyListLiteral(values) =>
        Traverse[Vector].traverse(values)((HCursor.fromExpr _).andThen(Decoder[A].decode))
      case EmptyListLiteral(Application(Expr.Constants.LIST, elementType)) if Decoder[A].isValidType(elementType) =>
        Right(Vector.empty)
      case other => Left(DecodingFailure.failedTarget("Vector", other, c.history))
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
    def decode(c: HCursor): Result[List[A]] = c.expr.normalize match {
      case NonEmptyListLiteral(values) =>
        Traverse[Vector].traverse(values)((HCursor.fromExpr _).andThen(Decoder[A].decode)).map(_.toList)
      case EmptyListLiteral(Application(Expr.Constants.LIST, elementType)) if Decoder[A].isValidType(elementType) =>
        Right(Nil)
      case other => Left(DecodingFailure.failedTarget("List", other, c.history))
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
    def decode(c: HCursor): Result[A => B] = c.expr.normalize match {
      case f @ Lambda(_, inputType, _) if Encoder[A].isExactType(inputType) =>
        val inferredType =
          try {
            Right(Expr.Util.typeCheck(f))
          } catch {
            case _: TypeCheckFailure =>
              Left(DecodingFailure.failedTarget("Function1", f, c.history))
          }

        inferredType.flatMap {
          case Pi(_, inputType, outputType)
              if Encoder[A].isExactType(inputType) && Decoder[B].isExactType(outputType) =>
            // This is still a little hand-wavy.
            Right((a: A) =>
              Decoder[B].decode(HCursor.fromExpr(Application(f, Encoder[A].encode(a)).normalize)).toOption.get
            )

          case _ => Left(DecodingFailure.failedTarget("Function1", f, c.history))
        }

      case other => Left(DecodingFailure.failedTarget("Function1", other, c.history))
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
