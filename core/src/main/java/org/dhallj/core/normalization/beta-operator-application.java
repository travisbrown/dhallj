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
      Boolean lhsAsBool = lhs.asBoolLiteral();
      Boolean rhsAsBool = rhs.asBoolLiteral();

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
      BigInteger lhsAsNaturalLiteral = lhs.asNaturalLiteral();
      BigInteger rhsAsNaturalLiteral = rhs.asNaturalLiteral();

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
      BigInteger lhsAsNaturalLiteral = lhs.asNaturalLiteral();
      BigInteger rhsAsNaturalLiteral = rhs.asNaturalLiteral();

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
      List<Expr> lhsAsListLiteral = lhs.asListLiteral();
      List<Expr> rhsAsListLiteral = rhs.asListLiteral();

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
      Iterable<Entry<String, Expr>> lhsAsRecordLiteral = lhs.asRecordLiteral();
      Iterable<Entry<String, Expr>> rhsAsRecordLiteral = rhs.asRecordLiteral();

      if (lhsAsRecordLiteral != null && rhsAsRecordLiteral != null) {
        Map<String, Expr> asMap = new TreeMap();

        for (Entry<String, Expr> entry : lhsAsRecordLiteral) {
          asMap.put(entry.getKey(), entry.getValue());
        }

        for (Entry<String, Expr> entry : rhsAsRecordLiteral) {
          asMap.put(entry.getKey(), entry.getValue());
        }

        return Expr.makeRecordLiteral(asMap.entrySet());

      } else if (lhs.equivalent(rhs)) {
        return rhs;
      }
    } else if (operator.equals(Operator.COMPLETE)) {
      return Expr.makeAnnotated(
              Expr.makeOperatorApplication(
                  Operator.PREFER, Expr.makeFieldAccess(lhs, "default"), rhs),
              Expr.makeFieldAccess(lhs, "T"))
          .accept(BetaNormalize.instance);
    } else if (operator.equals(Operator.COMBINE)) {
      return mergeRecursive(lhs, rhs).accept(BetaNormalize.instance);
    } else if (operator.equals(Operator.COMBINE_TYPES)) {
      return mergeTypesRecursive(lhs, rhs).accept(BetaNormalize.instance);
    }

    return Expr.makeOperatorApplication(operator, lhs, rhs);
  }

  private static Expr mergeRecursive(Expr first, Expr second) {
    Iterable<Entry<String, Expr>> firstAsRecordLiteral = first.asRecordLiteral();
    Iterable<Entry<String, Expr>> secondAsRecordLiteral = second.asRecordLiteral();

    if (firstAsRecordLiteral != null && secondAsRecordLiteral != null) {

      Map<String, Expr> asMap = new TreeMap();

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
          asMap.put(key, mergeRecursive(currentValue, value));
        }
      }
      return Expr.makeRecordLiteral(asMap.entrySet());
    } else {
      return Expr.makeOperatorApplication(Operator.COMBINE, first, second);
    }
  }

  private static Expr mergeTypesRecursive(Expr first, Expr second) {
    Iterable<Entry<String, Expr>> firstAsRecordType = first.asRecordType();
    Iterable<Entry<String, Expr>> secondAsRecordType = second.asRecordType();

    if (firstAsRecordType != null && secondAsRecordType != null) {

      Map<String, Expr> asMap = new TreeMap();

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
          asMap.put(key, mergeTypesRecursive(currentValue, value));
        }
      }
      return Expr.makeRecordType(asMap.entrySet());
    } else {
      return Expr.makeOperatorApplication(Operator.COMBINE, first, second);
    }
  }
}
