package org.dhallj.parser;

public class Positioned {
  final int beginLine;
  final int beginColumn;
  final int endLine;
  final int endColumn;

  Positioned(int beginLine, int beginColumn, int endLine, int endColumn) {
    this.beginLine = beginLine;
    this.beginColumn = beginColumn;
    this.endLine = endLine;
    this.endColumn = endColumn;
  }

  Positioned(Token token) {
    this(token.beginLine, token.beginColumn, token.endLine, token.endColumn);
  }

  Positioned(Token first, Token last) {
    this(first.beginLine, first.beginColumn, last.endLine, last.endColumn);
  }

  Positioned(Positioned same) {
    this(same.beginLine, same.beginColumn, same.endLine, same.endColumn);
  }

  Positioned(Positioned first, Positioned last) {
    this(first.beginLine, first.beginColumn, last.endLine, last.endColumn);
  }

  public final int getBeginLine() {
    return this.beginLine;
  }

  public final int getBeginColumn() {
    return this.beginColumn;
  }

  public final int getEndLine() {
    return this.endLine;
  }

  public final int getEndColumn() {
    return this.endColumn;
  }

  static class Wrapped<A> extends Positioned {
    final A value;

    Wrapped(A value, Token token) {
      super(token.beginLine, token.beginColumn, token.endLine, token.endColumn);
      this.value = value;
    }

    Wrapped(A value, Token first, Token last) {
      super(first.beginLine, first.beginColumn, last.endLine, last.endColumn);
      this.value = value;
    }

    Wrapped(A value, Positioned same) {
      super(same);
      this.value = value;
    }
  }
}
