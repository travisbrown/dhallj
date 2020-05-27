package org.dhallj.testing

import org.dhallj.ast._
import org.dhallj.core.{Expr, Operator}
import org.scalacheck.{Arbitrary, Gen, Shrink}

trait ArbitraryInstances {
  def genNameString: Gen[String]
  def genTextString: Gen[String]
  def defaultMaxDepth: Int = 3
  def defaultMaxInterpolated: Int = 3
  def defaultMaxFields: Int = 5

  def genValidNameString: Gen[String] = genNameString.map(_.replace("`", "")).map {
    case ""    => "x"
    case other => other
  }

  def isValidName(name: String): Boolean =
    List('\u0000', '\u0001', '`').forall(c => name.indexOf(c.toInt) == -1)

  def genIdentifier: Gen[Expr] =
    for {
      name <- genValidNameString
      index <- Gen.oneOf(Gen.const(0L), Arbitrary.arbitrary[Long].map(_.abs))
    } yield Identifier(name, index)

  val genOperator: Gen[Operator] = Gen.oneOf(Operator.values())

  def genForType(tpe: Expr): Option[Gen[Expr]] = tpe match {
    case Expr.Constants.NATURAL => Some(Arbitrary.arbitrary[BigInt].map(value => NaturalLiteral(value.abs).get))
    case Expr.Constants.INTEGER => Some(Arbitrary.arbitrary[BigInt].map(IntegerLiteral(_)))
    case Expr.Constants.DOUBLE  => Some(Arbitrary.arbitrary[Double].map(DoubleLiteral(_)))
    case Expr.Constants.BOOL =>
      Some(
        Arbitrary.arbitrary[Boolean].map(value => if (value) Expr.Constants.TRUE else Expr.Constants.FALSE)
      )
    case Application(Expr.Constants.LIST, elementType) =>
      genForType(elementType).map(
        Gen
          .buildableOf[Vector[Expr], Expr](_)
          .map(values =>
            if (values.isEmpty) {
              EmptyListLiteral(Application(Expr.Constants.LIST, elementType))
            } else {
              NonEmptyListLiteral(values.head, values.tail)
            }
          )
      )
    case Application(Expr.Constants.OPTIONAL, elementType) =>
      genForType(elementType).map(genElementType =>
        Gen.oneOf(
          genElementType.map(Application(Expr.Constants.SOME, _)),
          Gen.const(Application(Expr.Constants.NONE, elementType))
        )
      )
    case Expr.Constants.TEXT => Some(genText(defaultMaxDepth))
    case RecordType(fields) =>
      Some(
        fields
          .foldLeft(Gen.const(Map.empty[String, Expr])) {
            case (acc, (name, tpe)) =>
              genForType(tpe) match {
                case Some(genFieldType) => acc.flatMap(m => genFieldType.map(m.updated(name, _)))
                case None               => acc
              }
          }
          .map(RecordLiteral(_))
      )
    case _ => None
  }

  def genText(maxDepth: Int): Gen[Expr] =
    if (maxDepth == 0) genTextString.map(TextLiteral(_))
    else {
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

  def genType(maxDepth: Int): Gen[Expr] =
    if (maxDepth == 0) genType
    else {
      Gen.oneOf(
        genType,
        genType(maxDepth - 1).map(Application(Expr.Constants.LIST, _)),
        genType(maxDepth - 1).map(Application(Expr.Constants.OPTIONAL, _)),
        Gen
          .buildableOfN[Vector[(String, Expr)], (String, Expr)](
            defaultMaxFields,
            genValidNameString.flatMap(name => genType(maxDepth - 1).map((name, _)))
          )
          .map(fields => RecordType(fields.toMap)),
        Gen
          .buildableOfN[Vector[(String, Option[Expr])], (String, Option[Expr])](
            defaultMaxFields,
            genValidNameString.flatMap(name => Gen.option(genType(maxDepth - 1)).map((name, _)))
          )
          .map(fields => UnionType(fields.toMap))
      )
    }

  implicit val arbitraryWellTypedExpr: Arbitrary[WellTypedExpr] = Arbitrary(
    Gen
      .oneOf(
        genType(defaultMaxDepth),
        genType(defaultMaxDepth).flatMap(tpe => genForType(tpe).getOrElse(genType(defaultMaxDepth)))
        /* TODO: We should test type annotations.
      genType(defaultMaxDepth).flatMap(tpe =>
        genForType(tpe).map(_.map(expr => Annotated(expr, tpe))).getOrElse(genType(defaultMaxDepth))
      )
         */
      )
      .map(WellTypedExpr(_))
  )

  def genExpr(maxDepth: Int): Gen[Expr] =
    if (maxDepth == 0) {
      arbitraryWellTypedExpr.arbitrary.map(_.value)
    } else {
      Gen.oneOf(
        arbitraryWellTypedExpr.arbitrary.map(_.value),
        for {
          operator <- genOperator
          lhs <- genExpr(maxDepth - 1)
          rhs <- genExpr(maxDepth - 1)
        } yield Expr.makeOperatorApplication(operator, lhs, rhs),
        for {
          f <- genExpr(maxDepth - 1)
          arg <- genExpr(maxDepth - 1)
        } yield Expr.makeApplication(f, arg)
      )
    }

  implicit val arbitraryExpr: Arbitrary[Expr] = Arbitrary(genExpr(defaultMaxDepth))

  implicit val shrinkExpr: Shrink[Expr] = Shrink {
    case NaturalLiteral(value) => Shrink.shrink(value).flatMap(value => NaturalLiteral(value.abs))
    case IntegerLiteral(value) => Shrink.shrink(value).map(IntegerLiteral(_))
    case DoubleLiteral(value)  => Shrink.shrink(value).map(DoubleLiteral(_))
    case RecordLiteral(fields) => safeFieldsShrink.shrink(fields).map(RecordLiteral(_))
    case RecordType(fields)    => safeFieldsShrink.shrink(fields).map(RecordType(_))
    case UnionType(fields)     => safeOptionFieldsShrink.shrink(fields).map(UnionType(_))
    case TextLiteral(first, rest) =>
      Shrink.shrink(first).zip(Shrink.shrink(rest)).map {
        case (shrunkFirst, shrunkRest) => TextLiteral(shrunkFirst, shrunkRest)
      }
    case Application(Expr.Constants.SOME, arg) => Shrink.shrink(arg).map(Application(Expr.Constants.SOME, _))
    case NonEmptyListLiteral(values) =>
      Shrink.shrink(values).filter(_.nonEmpty).map(values => NonEmptyListLiteral(values.head, values.tail))
    // The following match doesn't preserve type, but is useful for isolating parsing issues for now.
    case OperatorApplication(_, lhs, rhs) => Stream(lhs, rhs)
    case other                            => Stream.empty
  }

  implicit val shrinkWellTypedExpr: Shrink[WellTypedExpr] = Shrink.xmap[Expr, WellTypedExpr](WellTypedExpr(_), _.value)

  val safeNameShrink: Shrink[String] = Shrink(name => name.inits.toStream.init)

  lazy val safeFieldsShrink: Shrink[Map[String, Expr]] = Shrink.shrinkContainer2[Map, String, Expr](
    implicitly,
    Shrink.shrinkTuple2(safeNameShrink, shrinkExpr),
    implicitly
  )

  lazy val safeOptionFieldsShrink: Shrink[Map[String, Option[Expr]]] =
    Shrink.shrinkContainer2[Map, String, Option[Expr]](
      implicitly,
      Shrink.shrinkTuple2(safeNameShrink, Shrink.shrinkOption(shrinkExpr)),
      implicitly
    )
}
