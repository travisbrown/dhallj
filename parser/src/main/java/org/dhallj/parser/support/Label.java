package org.dhallj.parser.support;

final class Label extends Positioned.Wrapped<String> {
  Label(Token token) {
    super(
        (token.image.charAt(0) != '`')
            ? token.image
            : token.image.substring(1, token.image.length() - 1),
        token);
  }
}
