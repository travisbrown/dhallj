package org.dhallj.parser;

import java.io.IOException;
import java.io.InputStream;
import org.dhallj.core.Expr;

public class Dhall {
  public static Expr.Parsed parse(String input) throws ParseException {
    return DhallParser.parse(input);
  }

  public static Expr.Parsed parse(InputStream input) throws IOException, ParseException {
    return DhallParser.parse(input);
  }

  public static void test(String input) throws ParseException {
    new DhallParser(new StringProvider(input)).OPERATOR_EXPRESSION();
  }
}
