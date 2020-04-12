package org.dhallj.cli;

import java.io.IOException;
import org.dhallj.core.Expr;
import org.dhallj.core.converters.JsonConverter;
import org.dhallj.imports.mini.Resolver;
import org.dhallj.parser.DhallParser;

public class Dhall {
  public static void main(String[] args) throws IOException {
    boolean resolveImports = false;
    boolean typeCheck = false;
    boolean normalize = false;
    boolean alphaNormalize = false;

    for (int i = 1; i < args.length; i++) {
      if (args[i].equals("--resolve")) {
        resolveImports = true;
      } else if (args[i].equals("--type-check")) {
        typeCheck = true;
      } else if (args[i].equals("--normalize")) {
        normalize = true;
      } else if (args[i].equals("--alpha")) {
        alphaNormalize = true;
      }
    }

    Expr expr = DhallParser.parse(System.in);
    Expr type = null;

    if (resolveImports) {
      expr = Resolver.resolve(expr, false);
    }

    if (normalize) {
      expr = expr.normalize();
    }

    if (alphaNormalize) {
      expr = expr.alphaNormalize();
    }

    if (typeCheck) {
      type = expr.typeCheck();
    }

    if (args.length == 0 || args[0].startsWith("--")) {
      System.out.println(expr);
    } else if (args[0].equals("hash")) {
      System.out.printf("sha256:%s\n", expr.hash());
    } else if (args[0].equals("type")) {
      if (!typeCheck) {
        type = expr.typeCheck();
      }
      System.out.println(type);
    } else if (args[0].equals("json")) {
      System.out.println(JsonConverter.toCompactString(expr));
    }
  }
}
