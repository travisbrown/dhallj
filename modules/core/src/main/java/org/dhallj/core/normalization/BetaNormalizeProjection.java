package org.dhallj.core.normalization;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.dhallj.core.Expr;
import org.dhallj.core.ExternalVisitor;
import org.dhallj.core.Operator;

final class BetaNormalizeProjection extends ExternalVisitor.Constant<Expr> {
  private final String[] fieldNames;

  private BetaNormalizeProjection(String[] fieldNames) {
    super(null);
    this.fieldNames = fieldNames;
  }

  static final Expr apply(Expr base, final String[] fieldNames) {
    if (fieldNames.length == 0) {
      return Expr.Constants.EMPTY_RECORD_LITERAL;
    }

    Expr result = base.accept(new BetaNormalizeProjection(fieldNames));

    if (result != null) {
      return result;
    } else {
      // We have to sort the field names if we can't reduce.
      String[] newFieldNames = new String[fieldNames.length];
      System.arraycopy(fieldNames, 0, newFieldNames, 0, fieldNames.length);
      Arrays.sort(newFieldNames);
      return Expr.makeProjection(base, newFieldNames);
    }
  }

  @Override
  public Expr onRecord(Iterable<Entry<String, Expr>> fields, int size) {
    Set<String> fieldNameSet = new HashSet(this.fieldNames.length);

    for (String fieldName : this.fieldNames) {
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
  public Expr onProjection(Expr base, String[] fieldNames) {
    return Expr.makeProjection(base, this.fieldNames).accept(BetaNormalize.instance);
  }

  @Override
  public Expr onOperatorApplication(Operator operator, Expr lhs, Expr rhs) {
    if (operator.equals(Operator.PREFER)) {
      List<Entry<String, Expr>> rhsFields = Expr.Util.asRecordLiteral(rhs);
      if (rhsFields != null) {

        Set<String> rhsKeys = new HashSet<String>();
        Set<String> leftFields = new TreeSet<String>();
        Set<String> rightFields = new TreeSet<String>();

        for (Entry<String, Expr> entry : rhsFields) {
          rhsKeys.add(entry.getKey());
        }

        for (String fieldName : this.fieldNames) {
          if (rhsKeys.contains(fieldName)) {
            rightFields.add(fieldName);
          } else {
            leftFields.add(fieldName);
          }
        }

        return Expr.makeOperatorApplication(
                Operator.PREFER,
                Expr.makeProjection(lhs, leftFields.toArray(new String[leftFields.size()])),
                Expr.makeProjection(rhs, rightFields.toArray(new String[leftFields.size()])))
            .accept(BetaNormalize.instance);
      }
    }
    return null;
  }
}
