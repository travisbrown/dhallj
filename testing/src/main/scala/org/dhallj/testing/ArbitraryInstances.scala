package org.dhallj.testing

import org.dhallj.ast._
import org.dhallj.core.Expr
import org.scalacheck.{Arbitrary, Gen, Shrink}

trait ArbitraryInstances {
  def genNameString: Gen[String]
  def genTextString: Gen[String]
  def defaultMaxDepth: Int = 3
  def defaultMaxInterpolated: Int = 3
  def defaultMaxFields: Int = 5

  def genValidNameString: Gen[String] = genNameString.map(_.replace("`", "")).map {
    case "" => "x"
    case other => other
  }

  def isValidName(name: String): Boolean = List('\u0000', '`').forall(name.indexOf(_) == -1) && name.nonEmpty

  def genIdentifier: Gen[Expr] = for {
    name <- genValidNameString
    index <- Gen.oneOf(Gen.const(0L), Arbitrary.arbitrary[Long].map(_.abs))
  } yield Identifier(name, index)

  def genForType(tpe: Expr): Option[Gen[Expr]] = tpe match {
    case Expr.Constants.NATURAL => Some(Arbitrary.arbitrary[BigInt].map(value => NaturalLiteral(value.abs)))
    case Expr.Constants.INTEGER => Some(Arbitrary.arbitrary[BigInt].map(IntegerLiteral(_)))
    case Expr.Constants.DOUBLE => Some(Arbitrary.arbitrary[Double].map(DoubleLiteral(_)))
    case Expr.Constants.BOOL => Some(
      Arbitrary.arbitrary[Boolean].map(value => if (value) Expr.Constants.TRUE else Expr.Constants.FALSE)
    )
    case Application(Expr.Constants.LIST, elementType) => genForType(elementType).map(
      Gen.buildableOf[Vector[Expr], Expr](_).map(values =>
        if (values.isEmpty) {
          EmptyListLiteral(Application(Expr.Constants.LIST, elementType))
        } else {
          NonEmptyListLiteral(values.head, values.tail)
        }
      )
    )
    case Application(Expr.Constants.OPTIONAL, elementType) => genForType(elementType).map(genElementType =>
      Gen.oneOf(
        genElementType.map(Application(Expr.Constants.SOME, _)),
        Gen.const(Application(Expr.Constants.NONE, elementType))
      )
    )
    case Expr.Constants.TEXT => Some(genText(defaultMaxDepth))
    case RecordType(fields) => Some(
      fields.foldLeft(Gen.const(Map.empty[String, Expr])) {
        case (acc, (name, tpe)) => genForType(tpe) match {
          case Some(genFieldType) => acc.flatMap(m => genFieldType.map(m.updated(name, _)))
          case None => acc
        }
      }.map(RecordLiteral(_))
    )
    case _ => None
  }

  def genText(maxDepth: Int): Gen[Expr] = if (maxDepth == 0) genTextString.map(TextLiteral(_)) else {
    Gen.oneOf(
      genTextString.map(TextLiteral(_)),
      for {
        first <- genTextString
        rest <- Gen.buildableOfN[Vector[(Expr, String)], (Expr, String)](
          defaultMaxInterpolated,
          genText(maxDepth - 1).flatMap(interpolated => genTextString.map((interpolated, _)))
        )
      } yield TextLiteral(first, rest)
    )
  }

  val genType: Gen[Expr] = Gen.oneOf(
    List(
      Expr.Constants.NATURAL,
      Expr.Constants.INTEGER,
      Expr.Constants.DOUBLE,
      Expr.Constants.BOOL,
      Expr.Constants.TEXT
    )
  )

  def genType(maxDepth: Int): Gen[Expr] = if (maxDepth == 0) genType else {
    Gen.oneOf(
      genType,
      genType(maxDepth - 1).map(Application(Expr.Constants.LIST, _)),
      genType(maxDepth - 1).map(Application(Expr.Constants.OPTIONAL, _)),
      Gen.buildableOfN[Vector[(String, Expr)], (String, Expr)](
        defaultMaxFields,
        genValidNameString.flatMap(name => genType(maxDepth - 1).map((name, _)))
      ).map(fields => RecordType(fields.toMap)),
      Gen.buildableOfN[Vector[(String, Option[Expr])], (String, Option[Expr])](
        defaultMaxFields,
        genValidNameString.flatMap(name => Gen.option(genType(maxDepth - 1)).map((name, _)))
      ).map(fields => UnionType(fields.toMap))
    )
  }

  implicit val arbitraryWellTypedExpr: Arbitrary[WellTypedExpr] = Arbitrary(
    Gen.oneOf(
      genType(defaultMaxDepth),
      genType(defaultMaxDepth).flatMap(tpe => genForType(tpe).getOrElse(genType(defaultMaxDepth))),
      genType(defaultMaxDepth).flatMap(tpe =>
        genForType(tpe).map(_.map(expr => Annotated(expr, tpe))).getOrElse(genType(defaultMaxDepth))
      )
    ).map(WellTypedExpr(_))
  )


  implicit val shrinkWellTypedExpr: Shrink[WellTypedExpr] = Shrink(expr =>
    (
      expr.value match {
        case NaturalLiteral(value) => Shrink.shrink(value).map(NaturalLiteral(_))
        case IntegerLiteral(value) => Shrink.shrink(value).map(IntegerLiteral(_))
        case DoubleLiteral(value) => Shrink.shrink(value).map(DoubleLiteral(_))
        case RecordLiteral(fields) => safeFieldsShrink.shrink(fields).map(_.filterKeys(isValidName).toMap).map(RecordLiteral(_))
        case RecordType(fields) => safeFieldsShrink.shrink(fields).map(_.filterKeys(isValidName).toMap).map(RecordType(_))
        case UnionType(fields) => safeOptionFieldsShrink.shrink(fields).map(_.filterKeys(isValidName).toMap).map(UnionType(_))
        case other => Stream.empty
      }
    ).map(WellTypedExpr(_))
  )

  val shrinkExprFromWellTypedExpr: Shrink[Expr] = Shrink.xmap[WellTypedExpr, Expr](_.value, WellTypedExpr(_))

  val safeNameShrink: Shrink[String] = Shrink { name => Shrink.shrink(name).filter(isValidName) }

  lazy val safeFieldsShrink: Shrink[Map[String, Expr]] = Shrink.shrinkContainer2[Map, String, Expr](
    implicitly,
    Shrink.shrinkTuple2(safeNameShrink, shrinkExprFromWellTypedExpr),
    implicitly
  )

  lazy val safeOptionFieldsShrink: Shrink[Map[String, Option[Expr]]] =
    Shrink.shrinkContainer2[Map, String, Option[Expr]](
      implicitly,
      Shrink.shrinkTuple2(safeNameShrink, Shrink.shrinkOption(shrinkExprFromWellTypedExpr)),
      implicitly
    )
}
