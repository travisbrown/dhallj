package org.dhallj.codec

import org.dhallj.core.Expr
import org.dhallj.ast._

trait Encoder[A] { self =>
  def encode(value: A, target: Option[Expr]): Expr
  def dhallType(value: Option[A], target: Option[Expr]): Expr

  final def encode(value: A): Expr = encode(value, None)
  final def isExactType(typeExpr: Expr): Boolean = typeExpr == dhallType(None, Some(typeExpr))

  def contramap[B](f: B => A): Encoder[B] = new Encoder[B] {
    def encode(value: B, target: Option[Expr]): Expr = self.encode(f(value), target)
    def dhallType(value: Option[B], target: Option[Expr]): Expr =
      self.dhallType(value.map(f), target)
  }
}

object Encoder {
  def apply[A](implicit A: Encoder[A]): Encoder[A] = A

  implicit val encodeLong: Encoder[Long] = new Encoder[Long] {
    def encode(value: Long, target: Option[Expr]): Expr = {
      val asBigInt = BigInt(value)

      target match {
        case Some(Expr.Constants.INTEGER) => IntegerLiteral(asBigInt)
        case _                            => NaturalLiteral(asBigInt).getOrElse(IntegerLiteral(asBigInt))
      }
    }

    def dhallType(value: Option[Long], target: Option[Expr]): Expr = target match {
      case Some(Expr.Constants.INTEGER) => Expr.Constants.INTEGER
      case _ =>
        value match {
          case Some(v) if v < 0 => Expr.Constants.INTEGER
          case _                => Expr.Constants.NATURAL
        }
    }
  }

  implicit val encodeInt: Encoder[Int] = encodeLong.contramap(_.toLong)

  implicit val encodeBigInt: Encoder[BigInt] = new Encoder[BigInt] {
    def encode(value: BigInt, target: Option[Expr]): Expr = target match {
      case Some(Expr.Constants.INTEGER) => IntegerLiteral(value)
      case _                            => NaturalLiteral(value).getOrElse(IntegerLiteral(value))
    }

    def dhallType(value: Option[BigInt], target: Option[Expr]): Expr = target match {
      case Some(Expr.Constants.INTEGER) => Expr.Constants.INTEGER
      case _ =>
        value match {
          case Some(v) if v < 0 => Expr.Constants.INTEGER
          case _                => Expr.Constants.NATURAL
        }
    }
  }

  implicit val encodeDouble: Encoder[Double] = new Encoder[Double] {
    def encode(value: Double, target: Option[Expr]): Expr = DoubleLiteral(value)
    def dhallType(value: Option[Double], target: Option[Expr]): Expr = Expr.Constants.DOUBLE
  }

  implicit val encodeString: Encoder[String] = new Encoder[String] {
    def encode(value: String, target: Option[Expr]): Expr = TextLiteral(value)
    def dhallType(value: Option[String], target: Option[Expr]): Expr = Expr.Constants.TEXT
  }

  implicit val encodeBoolean: Encoder[Boolean] = new Encoder[Boolean] {
    def encode(value: Boolean, target: Option[Expr]): Expr = BoolLiteral(value)
    def dhallType(value: Option[Boolean], target: Option[Expr]): Expr = Expr.Constants.BOOL
  }

  implicit def encodeOption[A: Encoder]: Encoder[Option[A]] = new Encoder[Option[A]] {
    def encode(value: Option[A], target: Option[Expr]): Expr = target match {
      case Some(Application(Expr.Constants.OPTIONAL, elementType)) =>
        value match {
          case Some(a) => Application(Expr.Constants.SOME, Encoder[A].encode(a, Some(elementType)))
          case None =>
            Application(Expr.Constants.NONE, Encoder[A].dhallType(None, Some(elementType)))
        }
      case _ =>
        value match {
          case Some(a) => Application(Expr.Constants.SOME, Encoder[A].encode(a))
          case None =>
            Application(Expr.Constants.NONE, Encoder[A].dhallType(None, None))
        }
    }

    def dhallType(value: Option[Option[A]], target: Option[Expr]): Expr = {
      val targetElementType = target.flatMap {
        case Application(Expr.Constants.OPTIONAL, elementType) => Some(elementType)
        case _                                                 => None
      }

      Application(Expr.Constants.OPTIONAL, Encoder[A].dhallType(value.flatten, targetElementType))
    }
  }

  implicit def encodeVector[A: Encoder]: Encoder[Vector[A]] = new Encoder[Vector[A]] {
    def encode(value: Vector[A], target: Option[Expr]): Expr = {
      val tpe = dhallElementType(Some(value), target)

      value match {
        case Vector() => EmptyListLiteral(Application(Expr.Constants.LIST, tpe))
        case h +: t =>
          NonEmptyListLiteral(
            Encoder[A].encode(h, Some(tpe)),
            t.map(Encoder[A].encode(_, Some(tpe)))
          )
      }
    }

    private def dhallElementType(value: Option[Vector[A]], target: Option[Expr]): Expr = {
      val targetElementType = target.flatMap {
        case Application(Expr.Constants.LIST, elementType) => Some(elementType)
        case _                                             => None
      }

      value match {
        case None           => Encoder[A].dhallType(None, targetElementType)
        case Some(Vector()) => Encoder[A].dhallType(None, targetElementType)
        case Some(h +: t) =>
          t.foldLeft(Encoder[A].dhallType(Some(h), targetElementType)) {
            case (acc, a) => Encoder[A].dhallType(Some(a), Some(acc))
          }
      }
    }

    def dhallType(value: Option[Vector[A]], target: Option[Expr]): Expr =
      Application(Expr.Constants.LIST, dhallElementType(value, target))
  }

  implicit def encodeList[A: Encoder]: Encoder[List[A]] = encodeVector[A].contramap(_.toVector)
}
