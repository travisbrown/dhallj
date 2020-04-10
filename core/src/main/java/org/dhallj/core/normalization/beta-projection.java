package org.dhallj.core.normalization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.dhallj.core.Expr;
import org.dhallj.core.ExternalVisitor;
import org.dhallj.core.Operator;

final class BetaNormalizeProjection {
  static final Expr apply(Expr base, final String[] fieldNames) {
    if (fieldNames.length == 0) {
      return Expr.Constants.EMPTY_RECORD_LITERAL;
    }

    Expr result =
        base.accept(
            new ExternalVisitor.Constant<Expr>(null) {
              @Override
              public Expr onRecord(Iterable<Entry<String, Expr>> fields, int size) {
                Set<String> fieldNameSet = new HashSet(fieldNames.length);

                for (String fieldName : fieldNames) {
                  fieldNameSet.add(fieldName);
                }

                Map<String, Expr> selected = new TreeMap();

                for (Entry<String, Expr> entry : fields) {
                  if (fieldNameSet.contains(entry.getKey())) {
                    selected.put(entry.getKey(), entry.getValue());
                  }
                }
                return Expr.makeRecordLiteral(selected.entrySet());
              }

              @Override
              public Expr onProjection(Expr base0, String[] fieldNames0) {
                return Expr.makeProjection(base0, fieldNames).acceptVis(BetaNormalize.instance);
              }

              @Override
              public Expr onOperatorApplication(Operator operator, Expr lhs, Expr rhs) {
                if (operator.equals(Operator.PREFER)) {
                  Iterable<Entry<String, Expr>> rhsFields = Expr.Util.asRecordLiteral(rhs);
                  if (rhsFields != null) {

                    Set<String> rhsKeys = new HashSet<String>();
                    Set<String> leftFields = new TreeSet<String>();
                    Set<String> rightFields = new TreeSet<String>();

                    for (Entry<String, Expr> entry : rhsFields) {
                      rhsKeys.add(entry.getKey());
                    }

                    for (String fieldName : fieldNames) {
                      if (rhsKeys.contains(fieldName)) {
                        rightFields.add(fieldName);
                      } else {
                        leftFields.add(fieldName);
                      }
                    }

                    return Expr.makeOperatorApplication(
                            Operator.PREFER,
                            Expr.makeProjection(
                                lhs, leftFields.toArray(new String[leftFields.size()])),
                            Expr.makeProjection(
                                rhs, rightFields.toArray(new String[leftFields.size()])))
                        .acceptVis(BetaNormalize.instance);
                  }
                }
                return null;
              }
            });

    if (result != null) {
      return result;
    } else {
      String[] newFieldNames = new String[fieldNames.length];
      System.arraycopy(fieldNames, 0, newFieldNames, 0, fieldNames.length);
      Arrays.sort(newFieldNames);
      return Expr.makeProjection(base, newFieldNames);
    }
  }
}
