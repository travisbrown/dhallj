package org.dhallj.imports

import org.dhallj.core.Expr
import org.dhallj.core.Expr.Util.{asListLiteral, asRecordLiteral, asSimpleTextLiteral}
import org.http4s.{Header, Headers}

import scala.jdk.CollectionConverters._

object ToHeaders {

  // See https://discourse.dhall-lang.org/t/valid-expressions-for-using-headers/205
  // For the moment, this is consistent with the Haskell implementation
  def apply(expr: Expr): Headers = {
    if (expr eq null) Headers.empty else {
      //TODO do we need to .accept(this) on expr?
      val e = expr.normalize
      val l = asListLiteral(e)
      if (l eq null) {
        Headers.empty
      } else {
        val hs: List[Header] = l.asScala.toList.flatMap { e =>
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
                  Some(Header(key, value))
                } else None
              } else if (map.contains("mapKey") && map.contains("mapValue")) {
                val key = asSimpleTextLiteral(map("mapKey"))
                val value = asSimpleTextLiteral(map("mapValue"))

                if ((key ne null) && (value ne null)) {
                  Some(Header(key, value))
                } else None
              } else None
            } else None
          }
        }
        Headers(hs)
      }
    }
  }

}
