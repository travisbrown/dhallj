package org.dhallj.parser;

/** Parses text input into Dhall expressions. */
class WhitespaceHandler {
  void onWhitespace(String value, long position) {}

  void onBlockComment(String value, long position) {}

  void onLineComment(String value, long position) {}

  static final WhitespaceHandler noop = new WhitespaceHandler();
}
