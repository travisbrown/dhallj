package org.dhallj.s

import java.math.BigInteger
import java.util.AbstractMap.SimpleImmutableEntry
import java.util.{List => JList, Map => JMap}
import org.dhallj.core.{Expr, Operator, Thunk, Visitor}
import org.dhallj.core.visitor.ConstantVisitor;
import scala.jdk.CollectionConverters._

abstract class EmptyVisitor[A] extends ConstantVisitor.External[Option[A]](None)

/**
 * An extremely messy sketch of Scala interop, primarily for use in tests.
 */
object ExprUtils {

  protected[s] def tupleToEntry[K, V](tuple: (K, V)): JMap.Entry[K, V] = new SimpleImmutableEntry(tuple._1, tuple._2)
  protected[s] def thunk[A](v: Visitor[Thunk[A], A], e: Expr): Thunk[A] = new Thunk[A] { def apply(): A = e.accept(v) }
}

import ExprUtils.thunk

object DoubleLiteral {
  def apply(value: Double): Expr = new Expr {
    def accept[A](visitor: Visitor[Thunk[A], A]): A = visitor.onDoubleLiteral(value)
    def acceptExternal[A](visitor: Visitor[Expr, A]): A = visitor.onDoubleLiteral(value)
  }

  def unapply(expr: Expr): Option[Double] =
    expr.acceptExternal(new EmptyVisitor[Double] {
      override def onDoubleLiteral(value: Double): Option[Double] = Some(value)
    })
}

object NaturalLiteral {
  def apply(value: BigInteger): Expr = new Expr {
    def accept[A](visitor: Visitor[Thunk[A], A]): A = visitor.onNaturalLiteral(value)
    def acceptExternal[A](visitor: Visitor[Expr, A]): A = visitor.onNaturalLiteral(value)
  }

  def unapply(expr: Expr): Option[BigInteger] =
    expr.acceptExternal(new EmptyVisitor[BigInteger] {
      override def onNaturalLiteral(value: BigInteger): Option[BigInteger] = Some(value)
    })
}

object IntegerLiteral {
  def apply(value: BigInteger): Expr = new Expr {
    def accept[A](visitor: Visitor[Thunk[A], A]): A = visitor.onIntegerLiteral(value)
    def acceptExternal[A](visitor: Visitor[Expr, A]): A = visitor.onIntegerLiteral(value)
  }

  def unapply(expr: Expr): Option[BigInteger] =
    expr.acceptExternal(new EmptyVisitor[BigInteger] {
      override def onIntegerLiteral(value: BigInteger): Option[BigInteger] = Some(value)
    })
}

object TextLiteral {
  def apply(value: String): Expr = new Expr {
    def accept[A](visitor: Visitor[Thunk[A], A]): A = visitor.onTextLiteral(Array(value), Nil.asJava)
    def acceptExternal[A](visitor: Visitor[Expr, A]): A = visitor.onTextLiteral(Array(value), Nil.asJava)
  }
}

object Identifier {
  def apply(value: String): Expr = apply(value, 0L)

  def apply(value: String, index: Long): Expr = new Expr {
    def accept[A](visitor: Visitor[Thunk[A], A]): A = visitor.onIdentifier(value, index)
    def acceptExternal[A](visitor: Visitor[Expr, A]): A = visitor.onIdentifier(value, index)
  }
}

object RecordLiteral {
  def apply(values: List[(String, Expr)]): Expr = new Expr {
    def accept[A](visitor: Visitor[Thunk[A], A]): A =
      visitor.onRecordLiteral(values.view.map {
        case (k, v) => ExprUtils.tupleToEntry((k, thunk(visitor, v)))
      }.asJava, values.size)

    def acceptExternal[A](visitor: Visitor[Expr, A]): A =
      visitor.onRecordLiteral(values.view.map {
        case (k, v) => ExprUtils.tupleToEntry((k, v))
      }.asJava, values.size)
  }
}

object RecordType {
  def apply(values: List[(String, Expr)]): Expr = new Expr {
    def accept[A](visitor: Visitor[Thunk[A], A]): A =
      visitor.onRecordType(values.view.map {
        case (k, v) => ExprUtils.tupleToEntry((k, thunk(visitor, v)))
      }.asJava, values.size)

    def acceptExternal[A](visitor: Visitor[Expr, A]): A =
      visitor.onRecordType(values.view.map {
        case (k, v) => ExprUtils.tupleToEntry((k, v))
      }.asJava, values.size)
  }
}

object UnionType {
  def apply(values: List[(String, Option[Expr])]): Expr = new Expr {
    def accept[A](visitor: Visitor[Thunk[A], A]): A =
      visitor.onUnionType(values.view.map {
        case (k, v) => ExprUtils.tupleToEntry((k, v.map(thunk(visitor, _)).getOrElse(null.asInstanceOf[Thunk[A]])))
      }.asJava, values.size)
    def acceptExternal[A](visitor: Visitor[Expr, A]): A =
      visitor.onUnionType(values.view.map {
        case (k, v) => ExprUtils.tupleToEntry((k, v.orNull))
      }.asJava, values.size)
  }
}

object NonEmptyListLiteral {
  def apply(values: List[Expr]): Expr = new Expr {
    def accept[A](visitor: Visitor[Thunk[A], A]): A =
      visitor.onNonEmptyListLiteral(values.view.map(thunk(visitor, _)).asJava, values.size)
    def acceptExternal[A](visitor: Visitor[Expr, A]): A =
      visitor.onNonEmptyListLiteral(values.asJava, values.size)
  }
}

object EmptyListLiteral {
  def apply(tpe: Expr): Expr = new Expr {
    def accept[A](visitor: Visitor[Thunk[A], A]): A = visitor.onEmptyListLiteral(thunk(visitor, tpe))
    def acceptExternal[A](visitor: Visitor[Expr, A]): A = visitor.onEmptyListLiteral(tpe)
  }
}

object Lambda {
  def apply(param: String, tpe: Expr, body: Expr): Expr = new Expr {
    def accept[A](visitor: Visitor[Thunk[A], A]): A = visitor.onLambda(param, thunk(visitor, tpe), thunk(visitor, body))
    def acceptExternal[A](visitor: Visitor[Expr, A]): A = visitor.onLambda(param, tpe, body)
  }
}

object Pi {
  def apply(tpe: Expr, body: Expr): Expr = new Expr {
    def accept[A](visitor: Visitor[Thunk[A], A]): A = visitor.onPi("_", thunk(visitor, tpe), thunk(visitor, body))
    def acceptExternal[A](visitor: Visitor[Expr, A]): A = visitor.onPi("_", tpe, body)
  }

  def apply(param: String, tpe: Expr, body: Expr): Expr = new Expr {
    def accept[A](visitor: Visitor[Thunk[A], A]): A = visitor.onPi(param, thunk(visitor, tpe), thunk(visitor, body))
    def acceptExternal[A](visitor: Visitor[Expr, A]): A = visitor.onPi(param, tpe, body)
  }
}

object Let {
  def apply(name: String, tpe: Option[Expr], value: Expr, body: Expr): Expr = new Expr {
    def accept[A](visitor: Visitor[Thunk[A], A]): A =
      visitor.onLet(
        name,
        thunk(visitor, tpe.orNull),
        thunk(visitor, value),
        thunk(visitor, body)
      )
    def acceptExternal[A](visitor: Visitor[Expr, A]): A =
      visitor.onLet(
        name,
        tpe.orNull,
        value,
        body
      )
  }
}

object If {
  def apply(condition: Expr, thenClause: Expr, elseClause: Expr): Expr = new Expr {
    def accept[A](visitor: Visitor[Thunk[A], A]): A =
      visitor.onIf(thunk(visitor, condition), thunk(visitor, thenClause), thunk(visitor, elseClause))
    def acceptExternal[A](visitor: Visitor[Expr, A]): A =
      visitor.onIf(condition, thenClause, elseClause)
  }
}

object Assert {
  def apply(value: Expr): Expr = new Expr {
    def accept[A](visitor: Visitor[Thunk[A], A]): A = visitor.onAssert(thunk(visitor, value))
    def acceptExternal[A](visitor: Visitor[Expr, A]): A = visitor.onAssert(value)
  }
}
