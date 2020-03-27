package org.dhallj.core.normalization;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.dhallj.core.Expr;
import org.dhallj.core.Operator;

/** Static utility classes and methods for internal use. */
final class NormalizationUtilities {
  static final Expr booleanToExpr(boolean value) {
    return value ? Expr.Constants.TRUE : Expr.Constants.FALSE;
  }

  static final Expr[] prependValue = new Expr[] {Expr.makeIdentifier("a")};
  static final Expr prependExpr =
      Expr.makeOperatorApplication(
          Operator.LIST_APPEND,
          Expr.makeNonEmptyListLiteral(prependValue),
          Expr.makeIdentifier("as"));

  static final Expr indexedRecordType(Expr type) {
    List<Entry<String, Expr>> fields = new ArrayList(2);
    fields.add(new SimpleImmutableEntry("index", Expr.Constants.NATURAL));
    fields.add(new SimpleImmutableEntry("value", type));
    return Expr.makeRecordType(fields);
  }
}
