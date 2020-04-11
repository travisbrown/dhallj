package org.dhallj.parser;

import java.io.IOException;
import java.io.InputStream;
import org.dhallj.core.Expr;
import org.dhallj.parser.support.Parser;

/** Parses text input into Dhall expressions. */
public final class DhallParser {
  public static Expr.Parsed parse(String input) {
    return Parser.parse(input);
  }

  public static Expr.Parsed parse(InputStream input) throws IOException {
    return Parser.parse(input);
  }
}
