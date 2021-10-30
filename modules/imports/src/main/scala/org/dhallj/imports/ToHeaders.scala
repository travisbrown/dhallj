package org.dhallj.imports

import java.util.AbstractMap.SimpleImmutableEntry
import java.util.Map.Entry
import org.dhallj.core.Expr
import org.dhallj.core.Expr.Util.{asListLiteral, asRecordLiteral, asSimpleTextLiteral}
import org.dhallj.core.typechecking.TypeCheckFailure
import org.http4s.{Header, Headers}
import org.typelevel.ci.CIString

import scala.collection.JavaConverters._

object ToHeaders {
  private val validType1: Expr = Expr.makeApplication(
    Expr.Constants.LIST,
    Expr.makeRecordType(
      Array[Entry[String, Expr]](
        new SimpleImmutableEntry("mapKey", Expr.Constants.TEXT),
        new SimpleImmutableEntry("mapValue", Expr.Constants.TEXT)
      )
    )
  )

  private val validType2: Expr = Expr.makeApplication(
    Expr.Constants.LIST,
    Expr.makeRecordType(
      Array[Entry[String, Expr]](
        new SimpleImmutableEntry("header", Expr.Constants.TEXT),
        new SimpleImmutableEntry("value", Expr.Constants.TEXT)
      )
    )
  )

  private def isValidType(expr: Expr): Boolean = {
    try {
      val tpe = Expr.Util.typeCheck(expr)

      tpe == validType1 || tpe == validType2
    } catch {
      case _: TypeCheckFailure => false
    }
  }

  // See https://discourse.dhall-lang.org/t/valid-expressions-for-using-headers/205
  // For the moment, this is consistent with the Haskell implementation
  def apply(expr: Expr): Option[Headers] =
    if (expr eq null) Some(Headers.empty)
    else {
      if (!isValidType(expr)) {
        None
      } else {
        // TODO do we need to .accept(this) on expr?
        val e = expr.normalize
        val l = asListLiteral(e)
        if (l eq null) {
          Some(Headers.empty)
        } else {
          val hs: List[Header.Raw] = l.asScala.toList.flatMap { e =>
            // e should have type `List { header : Text, value Text }`
            // or `List { mapKey : Text, mapValue Text }`
            val r = asRecordLiteral(e)
            if (r eq null) {
              None
            } else {
              if (r.size == 2) {
                val map = r.asScala.map(e => e.getKey -> e.getValue).toMap
                if (map.contains("header") && map.contains("value")) {
                  val key = asSimpleTextLiteral(map("header"))
                  val value = asSimpleTextLiteral(map("value"))

                  if ((key ne null) && (value ne null)) {
                    Some(Header.Raw(CIString(key), value))
                  } else None
                } else if (map.contains("mapKey") && map.contains("mapValue")) {
                  val key = asSimpleTextLiteral(map("mapKey"))
                  val value = asSimpleTextLiteral(map("mapValue"))

                  if ((key ne null) && (value ne null)) {
                    Some(Header.Raw(CIString(key), value))
                  } else None
                } else None
              } else None
            }
          }
          Some(Headers(hs))
        }
      }
    }
}
