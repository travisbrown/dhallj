package org.dhallj.parser.support;

import org.dhallj.core.Operator;

final class OperatorPrecedenceTable implements JavaCCParserConstants {
  private static final int MAX_OPERATOR_TOKEN_KIND = 256;
  private static final int[] table = new int[MAX_OPERATOR_TOKEN_KIND];

  static {
    for (int i = 0; i < MAX_OPERATOR_TOKEN_KIND; i++) {
      table[i] = -1;
    }

    table[OR] = Operator.OR.getPrecedence();
    table[AND] = Operator.AND.getPrecedence();
    table[EQUALS] = Operator.EQUALS.getPrecedence();
    table[NOT_EQUALS] = Operator.NOT_EQUALS.getPrecedence();
    table[PLUS] = Operator.PLUS.getPrecedence();
    table[TIMES] = Operator.TIMES.getPrecedence();
    table[TEXT_APPEND] = Operator.TEXT_APPEND.getPrecedence();
    table[LIST_APPEND] = Operator.LIST_APPEND.getPrecedence();
    table[COMBINE] = Operator.COMBINE.getPrecedence();
    table[PREFER] = Operator.PREFER.getPrecedence();
    table[COMBINE_TYPES] = Operator.COMBINE_TYPES.getPrecedence();
    table[IMPORT_ALT] = Operator.IMPORT_ALT.getPrecedence();
    table[EQUIVALENT] = Operator.EQUIVALENT.getPrecedence();
  }

  static final int get(int tokenKind) {
    if (tokenKind >= MAX_OPERATOR_TOKEN_KIND) {
      return -1;
    } else {
      return table[tokenKind];
    }
  }
}
