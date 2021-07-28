package org.dhallj.parser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import org.dhallj.core.Expr;
import org.dhallj.parser.support.Parser;

/** Parses text input into Dhall expressions. */
public final class DhallParser {
  private static final Charset UTF_8 = Charset.forName("UTF-8");

  public static Expr.Parsed parse(String input) {
    return Parser.parse(input);
  }

  public static Expr.Parsed parse(InputStream input) throws IOException {
    return parse(input, UTF_8);
  }

  public static Expr.Parsed parse(InputStream input, Charset charset) throws IOException {
    return Parser.parse(input, charset);
  }
}
