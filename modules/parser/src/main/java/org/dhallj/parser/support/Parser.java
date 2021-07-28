package org.dhallj.parser.support;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import org.dhallj.core.DhallException.ParsingFailure;
import org.dhallj.core.Expr;

/** Wrapper for the JavaCC-generated parser. */
public final class Parser {
  public static Expr.Parsed parse(String input) {
    try {
      return new JavaCCParser(new StringProvider(input)).TOP_LEVEL();
    } catch (ParseException underlying) {
      throw new ParsingFailure(underlying.getMessage(), underlying);
    } catch (TokenMgrException underlying) {
      throw new ParsingFailure(underlying.getMessage(), underlying);
    }
  }

  public static Expr.Parsed parse(InputStream input, Charset charset) throws IOException {
    try {
      return new JavaCCParser(new StreamProvider(input, charset.name())).TOP_LEVEL();
    } catch (ParseException underlying) {
      throw new ParsingFailure(underlying.getMessage(), underlying);
    } catch (TokenMgrException underlying) {
      throw new ParsingFailure(underlying.getMessage(), underlying);
    }
  }
}
