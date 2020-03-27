package org.dhallj.parser;

public final class Comment extends Positioned {
  private final String content;

  Comment(String content, int beginLine, int beginColumn, int endLine, int endColumn) {
    super(beginLine, beginColumn, endLine, endColumn);
    this.content = content;
  }

  public String getContent() {
    return this.content;
  }

  public String toString() {
    return String.format(
        "%s [%d, %d, %d, %d]",
        content, getBeginLine(), getBeginColumn(), getEndLine(), getEndColumn());
  }
}
