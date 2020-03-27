package org.dhallj.demo;

import org.dhallj.parser.Dhall;

public class Main {
  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      System.out.println(Dhall.parse(System.in).normalize().hash());
    } else {
      System.out.println(Dhall.parse(args[0]).normalize().hash());
    }
  }
}
