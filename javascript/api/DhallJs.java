package org.dhallj.js;

import org.dhallj.core.Expr;
import org.dhallj.parser.DhallParser;
import jsinterop.annotations.JsType;

@JsType
public class DhallJs {
  public static String parse(String input) {
    return DhallParser.parse(input).toString();
  }

  public static String normalize(String input) {
    return DhallParser.parse(input).normalize().toString();
  }

  public static String typeCheck(String input) {
    return Expr.Util.typeCheck(DhallParser.parse(input)).toString();
  }
}