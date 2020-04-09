package org.dhallj.parser.support;

import org.dhallj.core.Expr;

final class LetBinding {
  final String name;
  final Expr.Parsed type;
  final Expr.Parsed value;
  final String text0;
  final String text1;
  final String text2;
  final int beginLine;
  final int beginColumn;

  LetBinding(
      String name,
      Expr.Parsed type,
      Expr.Parsed value,
      String text0,
      String text1,
      String text2,
      int beginLine,
      int beginColumn) {
    this.name = name;
    this.type = type;
    this.value = value;
    this.text0 = text0;
    this.text1 = text1;
    this.text2 = text2;
    this.beginLine = beginLine;
    this.beginColumn = beginColumn;
  }
}
