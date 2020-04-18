package org.dhallj.core.normalization;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.dhallj.core.Expr;
import org.dhallj.core.Operator;

final class BetaNormalizeOperatorApplication {
  static final Expr apply(Operator operator, Expr lhs, Expr rhs) {
    if (operator.isBoolOperator()) {
      Boolean lhsAsBool = Expr.Util.asBoolLiteral(lhs);
      Boolean rhsAsBool = Expr.Util.asBoolLiteral(rhs);

      if (operator.equals(Operator.OR)) {
        if (lhsAsBool != null) {
          return lhsAsBool ? lhs : rhs;
        } else if (rhsAsBool != null) {
          return rhsAsBool ? rhs : lhs;
        } else if (lhs.equivalent(rhs)) {
          return lhs;
        }
      } else if (operator.equals(Operator.AND)) {
        if (lhsAsBool != null) {
          return lhsAsBool ? rhs : lhs;
        } else if (rhsAsBool != null) {
          return rhsAsBool ? lhs : rhs;
        } else if (lhs.equivalent(rhs)) {
          return lhs;
        }
      } else if (operator.equals(Operator.EQUALS)) {
        if (lhsAsBool != null && lhsAsBool) {
          return rhs;
        } else if (rhsAsBool != null && rhsAsBool) {
          return lhs;
        } else if (lhs.equivalent(rhs)) {
          return Expr.Constants.TRUE;
        }
      } else if (operator.equals(Operator.NOT_EQUALS)) {
        if (lhsAsBool != null && !lhsAsBool) {
          return rhs;
        } else if (rhsAsBool != null && !rhsAsBool) {
          return lhs;
        } else if (lhs.equivalent(rhs)) {
          return Expr.Constants.FALSE;
        }
      }
    } else if (operator.equals(Operator.PLUS)) {
      BigInteger lhsAsNaturalLiteral = Expr.Util.asNaturalLiteral(lhs);
      BigInteger rhsAsNaturalLiteral = Expr.Util.asNaturalLiteral(rhs);

      if (lhsAsNaturalLiteral != null) {
        if (rhsAsNaturalLiteral != null) {
          return Expr.makeNaturalLiteral(lhsAsNaturalLiteral.add(rhsAsNaturalLiteral));
        } else if (lhsAsNaturalLiteral.equals(BigInteger.ZERO)) {
          return rhs;
        }
      } else if (rhsAsNaturalLiteral != null && rhsAsNaturalLiteral.equals(BigInteger.ZERO)) {
        return lhs;
      }
    } else if (operator.equals(Operator.TIMES)) {
      BigInteger lhsAsNaturalLiteral = Expr.Util.asNaturalLiteral(lhs);
      BigInteger rhsAsNaturalLiteral = Expr.Util.asNaturalLiteral(rhs);

      if (lhsAsNaturalLiteral != null) {
        if (rhsAsNaturalLiteral != null) {
          return Expr.makeNaturalLiteral(lhsAsNaturalLiteral.multiply(rhsAsNaturalLiteral));
        } else if (lhsAsNaturalLiteral.equals(BigInteger.ZERO)) {
          return lhs;
        } else if (lhsAsNaturalLiteral.equals(BigInteger.ONE)) {
          return rhs;
        }
      } else if (rhsAsNaturalLiteral != null) {
        if (rhsAsNaturalLiteral.equals(BigInteger.ZERO)) {
          return rhs;
        } else if (rhsAsNaturalLiteral.equals(BigInteger.ONE)) {
          return lhs;
        }
      }
    } else if (operator.equals(Operator.TEXT_APPEND)) {
      String[] parts = {"", "", ""};
      List<Expr> interpolated = new ArrayList(2);
      interpolated.add(lhs);
      interpolated.add(rhs);

      return Expr.makeTextLiteral(parts, interpolated).accept(BetaNormalize.instance);
    } else if (operator.equals(Operator.LIST_APPEND)) {
      List<Expr> lhsAsListLiteral = Expr.Util.asListLiteral(lhs);
      List<Expr> rhsAsListLiteral = Expr.Util.asListLiteral(rhs);

      if (lhsAsListLiteral != null) {
        if (lhsAsListLiteral.isEmpty()) {
          return rhs;
        } else if (rhsAsListLiteral != null) {
          List<Expr> result = new ArrayList(lhsAsListLiteral.size() + rhsAsListLiteral.size());
          result.addAll(lhsAsListLiteral);
          result.addAll(rhsAsListLiteral);
          return Expr.makeNonEmptyListLiteral(result);
        }
      } else if (rhsAsListLiteral != null && rhsAsListLiteral.isEmpty()) {
        return lhs;
      }
    } else if (operator.equals(Operator.PREFER)) {
      List<Entry<String, Expr>> lhsAsRecordLiteral = Expr.Util.asRecordLiteral(lhs);
      List<Entry<String, Expr>> rhsAsRecordLiteral = Expr.Util.asRecordLiteral(rhs);

      if (lhsAsRecordLiteral != null) {
        if (rhsAsRecordLiteral != null) {
          Map<String, Expr> asMap = new TreeMap<>();

          for (Entry<String, Expr> entry : lhsAsRecordLiteral) {
            asMap.put(entry.getKey(), entry.getValue());
          }

          for (Entry<String, Expr> entry : rhsAsRecordLiteral) {
            asMap.put(entry.getKey(), entry.getValue());
          }

          return Expr.makeRecordLiteral(asMap.entrySet());
        } else if (!lhsAsRecordLiteral.iterator().hasNext()) {
          return rhs;
        }

      } else if (rhsAsRecordLiteral != null && !rhsAsRecordLiteral.iterator().hasNext()) {
        return lhs;
      } else if (lhs.equivalent(rhs)) {
        return rhs;
      }
    } else if (operator.equals(Operator.COMPLETE)) {
      return Expr.Util.desugarComplete(lhs, rhs).accept(BetaNormalize.instance);
    } else if (operator.equals(Operator.COMBINE)) {
      List<Entry<String, Expr>> firstAsRecordLiteral = Expr.Util.asRecordLiteral(lhs);
      List<Entry<String, Expr>> secondAsRecordLiteral = Expr.Util.asRecordLiteral(rhs);

      if (firstAsRecordLiteral != null) {
        if (secondAsRecordLiteral != null) {
          return mergeRecursive(lhs, rhs, firstAsRecordLiteral, secondAsRecordLiteral)
              .accept(BetaNormalize.instance);
        } else {
          if (!firstAsRecordLiteral.iterator().hasNext()) {
            return rhs;
          }
        }
      } else if (secondAsRecordLiteral != null && !secondAsRecordLiteral.iterator().hasNext()) {
        return lhs;
      } else {
        return Expr.makeOperatorApplication(Operator.COMBINE, lhs, rhs);
      }

    } else if (operator.equals(Operator.COMBINE_TYPES)) {
      List<Entry<String, Expr>> firstAsRecordType = Expr.Util.asRecordType(lhs);
      List<Entry<String, Expr>> secondAsRecordType = Expr.Util.asRecordType(rhs);

      if (firstAsRecordType != null) {
        if (secondAsRecordType != null) {
          return mergeTypesRecursive(lhs, rhs, firstAsRecordType, secondAsRecordType)
              .accept(BetaNormalize.instance);
        } else {
          if (!firstAsRecordType.iterator().hasNext()) {
            return rhs;
          }
        }
      } else if (secondAsRecordType != null && !secondAsRecordType.iterator().hasNext()) {
        return lhs;
      } else {
        return Expr.makeOperatorApplication(Operator.COMBINE_TYPES, lhs, rhs);
      }
    }

    return Expr.makeOperatorApplication(operator, lhs, rhs);
  }

  private static final Expr mergeRecursive(
      Expr first,
      Expr second,
      List<Entry<String, Expr>> firstAsRecordLiteral,
      List<Entry<String, Expr>> secondAsRecordLiteral) {
    if (firstAsRecordLiteral != null && secondAsRecordLiteral != null) {

      Map<String, Expr> asMap = new TreeMap<>();

      for (Entry<String, Expr> entry : firstAsRecordLiteral) {
        asMap.put(entry.getKey(), entry.getValue());
      }

      for (Entry<String, Expr> entry : secondAsRecordLiteral) {
        String key = entry.getKey();
        Expr value = entry.getValue();
        Expr currentValue = asMap.get(key);

        if (currentValue == null) {
          asMap.put(key, entry.getValue());
        } else {
          asMap.put(
              key,
              mergeRecursive(
                  currentValue,
                  value,
                  Expr.Util.asRecordType(currentValue),
                  Expr.Util.asRecordType(value)));
        }
      }
      return Expr.makeRecordLiteral(asMap.entrySet());
    } else {
      return Expr.makeOperatorApplication(Operator.COMBINE, first, second);
    }
  }

  private static final Expr mergeTypesRecursive(
      Expr first,
      Expr second,
      List<Entry<String, Expr>> firstAsRecordType,
      List<Entry<String, Expr>> secondAsRecordType) {
    if (firstAsRecordType != null && secondAsRecordType != null) {

      Map<String, Expr> asMap = new TreeMap<>();

      for (Entry<String, Expr> entry : firstAsRecordType) {
        asMap.put(entry.getKey(), entry.getValue());
      }

      for (Entry<String, Expr> entry : secondAsRecordType) {
        String key = entry.getKey();
        Expr value = entry.getValue();
        Expr currentValue = asMap.get(key);

        if (currentValue == null) {
          asMap.put(key, entry.getValue());
        } else {
          asMap.put(
              key,
              mergeTypesRecursive(
                  currentValue,
                  value,
                  Expr.Util.asRecordType(currentValue),
                  Expr.Util.asRecordType(value)));
        }
      }
      return Expr.makeRecordType(asMap.entrySet());
    } else {
      return Expr.makeOperatorApplication(Operator.COMBINE, first, second);
    }
  }
}
