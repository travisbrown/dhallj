package org.dhallj.demo;

import org.dhallj.core.Expr;
import org.dhallj.imports.mini.Resolver;
import org.dhallj.parser.DhallParser;

public class Main {
  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      Expr parsed = DhallParser.parse(System.in);
      parsed.typeCheck();
      System.out.println(parsed.normalize().alphaNormalize().hash());
    } else {
      Expr parsed = DhallParser.parse(args[0]);
      Expr resolved = Resolver.resolve(parsed, true, null);
      resolved.typeCheck();
      System.out.println(resolved.normalize().alphaNormalize().hash());
    }
  }
}
