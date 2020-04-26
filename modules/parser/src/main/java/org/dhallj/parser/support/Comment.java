package org.dhallj.parser.support;

final class Comment {
  private final String content;
  private final int beginLine;
  private final int beginColumn;
  private final int endLine;
  private final int endColumn;

  Comment(String content, int beginLine, int beginColumn, int endLine, int endColumn) {
    this.content = content;
    this.beginLine = beginLine;
    this.beginColumn = beginColumn;
    this.endLine = endLine;
    this.endColumn = endColumn;
  }

  public String getContent() {
    return this.content;
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
}
