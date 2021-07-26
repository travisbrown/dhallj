package org.dhallj.parser;

import java.io.IOException;
import java.io.InputStream;
import org.dhallj.core.Expr;

/** Parses text input into Dhall expressions. */
public final class DhallParser {
  public static Expr parse(String input) {
    return JaccParser.parse(input);
  }

  public static Expr parse(InputStream stream) throws IOException {
    return JaccParser.parse(stream);
  }
}
