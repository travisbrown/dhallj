package org.dhallj.demo;

import org.dhallj.core.Expr;
import org.dhallj.parser.Dhall;

public class Main {
  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      Expr parsed = Dhall.parse(System.in);
      parsed.typeCheck();
      System.out.println(parsed.normalize().alphaNormalize().hash());
    } else {
      Expr parsed = Dhall.parse(args[0]);
      parsed.typeCheck();
      System.out.println(parsed.normalize().alphaNormalize().hash());
    }
  }
}
