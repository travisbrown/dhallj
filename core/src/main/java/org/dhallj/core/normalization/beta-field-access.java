package org.dhallj.core.normalization;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import org.dhallj.core.Expr;
import org.dhallj.core.Operator;
import org.dhallj.core.util.FieldUtilities;
import org.dhallj.core.visitor.ConstantVisitor;

final class BetaNormalizeFieldAccess {
  static final Expr apply(Expr base, final String fieldName) {
    Expr result =
        base.acceptExternal(
            new ConstantVisitor.External<Expr>(null) {
              @Override
              public Expr onRecordLiteral(Iterable<Entry<String, Expr>> fields, int size) {
                return FieldUtilities.lookup(fields, fieldName);
              }

              @Override
              public Expr onProjection(Expr base0, String[] fieldNames0) {
                return Expr.makeFieldAccess(base0, fieldName).acceptVis(BetaNormalize.instance);
              }

              @Override
              public Expr onOperatorApplication(Operator operator, Expr lhs, Expr rhs) {
                if (operator.equals(Operator.PREFER)) {
                  Iterable<Entry<String, Expr>> lhsFields = Expr.Util.asRecordLiteral(lhs);
                  if (lhsFields != null) {
                    Entry<String, Expr> lhsFound = FieldUtilities.lookupEntry(lhsFields, fieldName);

                    if (lhsFound != null) {
                      List<Entry<String, Expr>> singleton = new ArrayList();
                      singleton.add(lhsFound);

                      return Expr.makeFieldAccess(
                          Expr.makeOperatorApplication(
                              Operator.PREFER, Expr.makeRecordLiteral(singleton), rhs),
                          fieldName);
                    } else {
                      return Expr.makeFieldAccess(rhs, fieldName);
                    }
                  } else {
                    Iterable<Entry<String, Expr>> rhsFields = Expr.Util.asRecordLiteral(rhs);
                    if (rhsFields != null) {
                      Expr rhsFound = FieldUtilities.lookup(rhsFields, fieldName);

                      if (rhsFound != null) {
                        return rhsFound;
                      } else {
                        return Expr.makeFieldAccess(lhs, fieldName)
                            .acceptVis(BetaNormalize.instance);
                      }
                    }
                  }
                } else if (operator.equals(Operator.COMBINE)) {
                  Iterable<Entry<String, Expr>> lhsFields = Expr.Util.asRecordLiteral(lhs);
                  if (lhsFields != null) {
                    Entry<String, Expr> lhsFound = FieldUtilities.lookupEntry(lhsFields, fieldName);

                    if (lhsFound != null) {
                      List<Entry<String, Expr>> singleton = new ArrayList();
                      singleton.add(lhsFound);

                      return Expr.makeFieldAccess(
                          Expr.makeOperatorApplication(
                              Operator.COMBINE, Expr.makeRecordLiteral(singleton), rhs),
                          fieldName);
                    } else {
                      return Expr.makeFieldAccess(rhs, fieldName);
                    }
                  } else {
                    Iterable<Entry<String, Expr>> rhsFields = Expr.Util.asRecordLiteral(rhs);
                    if (rhsFields != null) {
                      Entry<String, Expr> rhsFound =
                          FieldUtilities.lookupEntry(rhsFields, fieldName);

                      if (rhsFound != null) {
                        List<Entry<String, Expr>> singleton = new ArrayList();
                        singleton.add(rhsFound);

                        return Expr.makeFieldAccess(
                            Expr.makeOperatorApplication(
                                Operator.COMBINE, lhs, Expr.makeRecordLiteral(singleton)),
                            fieldName);
                      } else {
                        return Expr.makeFieldAccess(lhs, fieldName)
                            .acceptVis(BetaNormalize.instance);
                      }
                    }
                  }
                }
                return null;
              }
            });

    if (result != null) {
      return result;
    } else {
      return Expr.makeFieldAccess(base, fieldName);
    }
  }
}
