package org.dhallj.core;

/** Represents a section of a source document corresponding to a parsed expression. */
public abstract class Source {
  final int beginLine;
  final int beginColumn;
  final int endLine;
  final int endColumn;

  public Source(int beginLine, int beginColumn, int endLine, int endColumn) {
    this.beginLine = beginLine;
    this.beginColumn = beginColumn;
    this.endLine = endLine;
    this.endColumn = endColumn;
  }

  public abstract void printText(StringBuilder builder);

  public final String getText() {
    StringBuilder builder = new StringBuilder();
    this.printText(builder);
    return builder.toString();
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

  public final String toString() {
    StringBuilder builder = new StringBuilder("[(");
    builder.append(this.beginLine);
    builder.append(", ");
    builder.append(this.beginColumn);
    builder.append(") (");
    builder.append(this.endLine);
    builder.append(", ");
    builder.append(this.endColumn);
    builder.append(")]\n");
    this.printText(builder);
    return builder.toString();
  }

  private static final class FromString extends Source {
    private final String text;

    FromString(String text, int beginLine, int beginColumn, int endLine, int endColumn) {
      super(beginLine, beginColumn, endLine, endColumn);
      this.text = text;
    }

    public final void printText(StringBuilder builder) {
      builder.append(this.text);
    }
  }

  public static final Source fromString(
      String text, int beginLine, int beginColumn, int endLine, int endColumn) {
    return new FromString(text, beginLine, beginColumn, endLine, endColumn);
  }
}
