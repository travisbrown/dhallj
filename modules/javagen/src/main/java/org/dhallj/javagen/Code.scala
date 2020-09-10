package org.dhallj.javagen

case class Code(content: String, defs: Vector[Code] = Vector.empty) {
  final private def replace(in: String, target: Int, newName: String): String = {
    var last = 0
    var next = in.indexOf(Code.marker, last)
    val builder = new StringBuilder()

    while (next >= 0) {
      builder.append(in.substring(last, next))
      val index = in.substring(next + Code.marker.length, next + Code.marker.length + Code.indexLength).toInt
      if (index == target) {
        builder.append(newName)
      } else {
        builder.append(in.substring(next, next + Code.marker.length + Code.indexLength))
      }

      last = next + Code.marker.length + Code.indexLength
      next = in.indexOf(Code.marker, last)
    }

    builder.append(in.substring(last))
    return builder.toString()
  }

  def mapContent(f: String => String): Code = Code(f(Code.makeIdentifier(0)), Vector(this))

  def merge(other: Code)(f: (String, String) => String): Code =
    Option(other) match {
      case Some(otherValue) => Code(f(Code.makeIdentifier(0), Code.makeIdentifier(1)), Vector(this, other))
      case None             => this.copy(content = f(content, "null"))
    }

  def merge(other0: Code, other1: Code)(f: (String, String, String) => String): Code =
    Option(other1) match {
      case Some(otherValue) =>
        Code(f(Code.makeIdentifier(0), Code.makeIdentifier(1), Code.makeIdentifier(2)), Vector(this, other0, other1))
      case None => Code(f(Code.makeIdentifier(0), Code.makeIdentifier(1), "null"), Vector(this, other0))
    }

  protected def toFields(known: Map[Code, (String, String)]): (String, Map[Code, (String, String)]) =
    known.get(this) match {
      case Some((name, impl)) => (name, known)
      case None =>
        val (newContent, newKnown) = this.defs.zipWithIndex.foldLeft((this.content, known)) {
          case ((accContent, accKnown), (code, i)) =>
            val (childName, newKnown) = code.toFields(accKnown)

            (this.replace(accContent, i, childName), newKnown)
        }

        val nextName = Code.makeFieldName(newKnown.size)
        (nextName, newKnown.updated(this, (nextName, newContent)))
    }

  def toClassDef(packageName: String, className: String): String = {
    val (topLevelFieldName, fields) = toFields(Map.empty)

    val fieldDefs = fields.values.toList
      .sortBy(_._1)
      .map { case (name, impl) =>
        s"  private static final Expr $name = $impl;"
      }
      .mkString("\n")

    s"""package $packageName;
       |
       |import java.math.BigInteger;
       |import java.util.AbstractMap.SimpleImmutableEntry;
       |import java.util.ArrayList;
       |import java.util.List;
       |import java.util.Map.Entry;
       |import org.dhallj.core.Expr;
       |import org.dhallj.core.Operator;
       |
       |public final class $className {
       |$fieldDefs
       |
       |public static final Expr instance = $topLevelFieldName;
       |}
       |""".stripMargin
  }
}

object Code {
  private[javagen] val marker: String = "__"
  private[javagen] val indexLength: Int = 4
  private[javagen] def makeIdentifier(n: Int): String =
    String.format(s"%s%0${indexLength}d", marker, Int.box(n))
  private[javagen] def makeFieldName(n: Int): String = f"f$n%06d"

  def mergeAll(other: Vector[Code])(f: Vector[String] => String): Code =
    Code(f(0.until(other.size).map(makeIdentifier).toVector), other)

  def mergeAll[A](other: Vector[A])(extract: A => Option[Code])(f: Vector[(A, String)] => String): Code = {
    var i = -1
    val values = other.map { value =>
      if (extract(value).isDefined) {
        i += 1
        (value, makeIdentifier(i))
      } else {
        (value, "null")
      }
    }

    Code(f(values), other.flatMap(extract(_)))
  }
}
