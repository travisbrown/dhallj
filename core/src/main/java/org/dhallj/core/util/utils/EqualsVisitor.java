package org.dhallj.core.util;

import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.dhallj.core.visitor.ConstantVisitor;
import org.dhallj.core.visitor.ExternalVisitor;
import org.dhallj.core.Expr;
import org.dhallj.core.Import;
import org.dhallj.core.Operator;
import org.dhallj.core.Source;
import org.dhallj.core.Visitor;

/**
 * A visitor that checks whether two expressions are the same, for temporary use until we have
 * actual equivalence.
 */
public final class EqualsVisitor implements ExternalVisitor<Visitor<Expr, Boolean>> {
  private static EqualsVisitor instance = new EqualsVisitor();

  public static boolean equals(Expr e1, Expr e2) {
    return instance.areEqual(e1, e2);
  }

  private boolean areEqual(Expr e1, Expr e2) {
    return this.areEqual(e1, e2, false);
  }

  private boolean areEqual(Expr e1, Expr e2, boolean allowNull) {
    return ((e1 == null && e2 == null)
        || ((e1 != null && e2 != null) && e2.acceptExternal(e1.acceptExternal(this))));
  }

  private <A> boolean areEqual(Iterable<Expr> e1, Iterable<Expr> e2) {
    Iterator<Expr> i1 = e1.iterator();
    Iterator<Expr> i2 = e2.iterator();

    while (i1.hasNext() || i2.hasNext()) {
      if (!i1.hasNext() || !i2.hasNext()) {
        return false;
      }
      Expr a1 = i1.next();
      Expr a2 = i2.next();
      if (!this.areEqual(a1, a2)) {
        return false;
      }
    }
    return true;
  }

  private <A> boolean areEntriesEqual(
      Iterable<Entry<String, Expr>> e1, Iterable<Entry<String, Expr>> e2) {
    return areEntriesEqual(e1, e2, false);
  }

  private <A> boolean areEntriesEqual(
      Iterable<Entry<String, Expr>> e1, Iterable<Entry<String, Expr>> e2, boolean allowNull) {
    Iterator<Entry<String, Expr>> i1 = e1.iterator();
    Iterator<Entry<String, Expr>> i2 = e2.iterator();

    while (i1.hasNext() || i2.hasNext()) {
      if (!i1.hasNext() || !i2.hasNext()) {
        return false;
      }
      Entry<String, Expr> a1 = i1.next();
      Entry<String, Expr> a2 = i2.next();
      if (!a1.getKey().equals(a2.getKey())
          || !this.areEqual(a1.getValue(), a2.getValue(), allowNull)) {
        return false;
      }
    }
    return true;
  }

  public Visitor<Expr, Boolean> onDoubleLiteral(final double value) {
    return new FalseVisitor() {
      public Boolean onDoubleLiteral(double value1) {
        return Double.compare(value, value1) == 0;
      }
    };
  }

  public Visitor<Expr, Boolean> onNaturalLiteral(final BigInteger value) {
    return new FalseVisitor() {
      public Boolean onNaturalLiteral(BigInteger value1) {
        return value.equals(value1);
      }
    };
  }

  public Visitor<Expr, Boolean> onIntegerLiteral(final BigInteger value) {
    return new FalseVisitor() {
      public Boolean onIntegerLiteral(BigInteger value1) {
        return value.equals(value1);
      }
    };
  }

  public Visitor<Expr, Boolean> onTextLiteral(
      final String[] parts, final Iterable<Expr> interpolated) {
    return new FalseVisitor() {
      public Boolean onTextLiteral(String[] parts1, Iterable<Expr> interpolated1) {
        return Arrays.equals(parts, parts1) && areEqual(interpolated, interpolated1);
      }
    };
  }

  public Visitor<Expr, Boolean> onApplication(final Expr base, final Expr arg) {
    return new FalseVisitor() {
      public Boolean onApplication(Expr base1, Expr arg1) {
        return areEqual(base, base1) && areEqual(arg, arg1);
      }
    };
  }

  public Visitor<Expr, Boolean> onOperatorApplication(
      final Operator operator, final Expr lhs, final Expr rhs) {
    return new FalseVisitor() {
      public Boolean onOperatorApplication(Operator operator1, Expr lhs1, Expr rhs1) {
        return operator.equals(operator1) && areEqual(lhs, lhs1) && areEqual(rhs, rhs1);
      }
    };
  }

  public Visitor<Expr, Boolean> onIf(final Expr cond, final Expr thenValue, final Expr elseValue) {
    return new FalseVisitor() {
      public Boolean onIf(Expr cond1, Expr thenValue1, Expr elseValue1) {
        return areEqual(cond, cond1)
            && areEqual(thenValue, thenValue1)
            && areEqual(elseValue, elseValue1);
      }
    };
  }

  public Visitor<Expr, Boolean> onLambda(final String param, final Expr input, final Expr result) {
    return new FalseVisitor() {
      public Boolean onLambda(String param1, Expr input1, Expr result1) {
        return param.equals(param1) && areEqual(input, input1) && areEqual(result, result1);
      }
    };
  }

  public Visitor<Expr, Boolean> onPi(final String param, final Expr input, final Expr result) {
    return new FalseVisitor() {
      public Boolean onPi(String param1, Expr input1, Expr result1) {
        return ((param == null && param1 == null) || param.equals(param1))
            && areEqual(input, input1)
            && areEqual(result, result1);
      }
    };
  }

  public Visitor<Expr, Boolean> onAssert(final Expr expr) {
    return new FalseVisitor() {
      public Boolean onAssert(Expr expr1) {
        return areEqual(expr, expr1);
      }
    };
  }

  public Visitor<Expr, Boolean> onFieldAccess(final Expr base, final String fieldName) {
    return new FalseVisitor() {
      public Boolean onFieldAccess(Expr base1, String fieldName1) {
        return areEqual(base, base1) && fieldName.equals(fieldName1);
      }
    };
  }

  public Visitor<Expr, Boolean> onProjection(final Expr base, final String[] fieldNames) {
    return new FalseVisitor() {
      public Boolean onProjection(Expr base1, String[] fieldNames1) {
        return areEqual(base, base1) && Arrays.equals(fieldNames, fieldNames1);
      }
    };
  }

  public Visitor<Expr, Boolean> onProjectionByType(final Expr base, final Expr tpe) {
    return new FalseVisitor() {
      public Boolean onProjectionByType(Expr base1, Expr tpe1) {
        return areEqual(base, base1) && areEqual(tpe, tpe1);
      }
    };
  }

  public Visitor<Expr, Boolean> onIdentifier(final String value, final long index) {
    return new FalseVisitor() {
      public Boolean onIdentifier(String value1, long index1) {
        return value.equals(value1) && index == index1;
      }
    };
  }

  public Visitor<Expr, Boolean> onRecordLiteral(
      final Iterable<Entry<String, Expr>> fields, final int size) {
    return new FalseVisitor() {
      public Boolean onRecordLiteral(Iterable<Entry<String, Expr>> fields1, int size1) {
        return size == size1 && areEntriesEqual(fields, fields1);
      }
    };
  }

  public Visitor<Expr, Boolean> onRecordType(
      final Iterable<Entry<String, Expr>> fields, final int size) {
    return new FalseVisitor() {
      public Boolean onRecordType(Iterable<Entry<String, Expr>> fields1, int size1) {
        return size == size1 && areEntriesEqual(fields, fields1);
      }
    };
  }

  public Visitor<Expr, Boolean> onUnionType(
      final Iterable<Entry<String, Expr>> fields, final int size) {
    return new FalseVisitor() {
      public Boolean onUnionType(Iterable<Entry<String, Expr>> fields1, int size1) {
        return size == size1 && areEntriesEqual(fields, fields1, true);
      }
    };
  }

  public Visitor<Expr, Boolean> onEmptyListLiteral(final Expr tpe) {
    return new FalseVisitor() {
      public Boolean onEmptyListLiteral(Expr tpe1) {
        return areEqual(tpe, tpe1);
      }
    };
  }

  public Visitor<Expr, Boolean> onNonEmptyListLiteral(final Iterable<Expr> values, final int size) {
    return new FalseVisitor() {
      public Boolean onNonEmptyListLiteral(Iterable<Expr> values1, int size1) {
        return size == size1 && areEqual(values, values1);
      }
    };
  }

  public Visitor<Expr, Boolean> onLet(
      final String name, final Expr type, final Expr value, final Expr body) {
    return new FalseVisitor() {
      public Boolean onLet(String name1, Expr type1, Expr value1, Expr body1) {
        return name.equals(name1)
            && areEqual(type, type1, true)
            && areEqual(value, value1)
            && areEqual(body, body1);
      }
    };
  }

  public Visitor<Expr, Boolean> onAnnotated(final Expr base, final Expr tpe) {
    return new FalseVisitor() {
      public Boolean onAnnotated(Expr base1, Expr tpe1) {
        return areEqual(base, base1) && areEqual(tpe, tpe1);
      }
    };
  }

  public Visitor<Expr, Boolean> onToMap(final Expr base, final Expr tpe) {
    return new FalseVisitor() {
      public Boolean onToMap(Expr base1, Expr tpe1) {
        return areEqual(base, base1) && areEqual(tpe, tpe1, true);
      }
    };
  }

  public Visitor<Expr, Boolean> onMerge(final Expr left, final Expr right, final Expr tpe) {
    return new FalseVisitor() {
      public Boolean onMerge(Expr left1, Expr right1, Expr tpe1) {
        return areEqual(left, left1) && areEqual(right, right1) && areEqual(tpe, tpe1, true);
      }
    };
  }

  public Visitor<Expr, Boolean> onLocalImport(final Path path, final Import.Mode mode) {
    return new FalseVisitor() {
      public Boolean onLocalImport(Path path1, Import.Mode mode1) {
        return path.equals(path1) && mode.equals(mode1);
      }
    };
  }

  public Visitor<Expr, Boolean> onRemoteImport(final URI url, final Import.Mode mode) {
    return new FalseVisitor() {
      public Boolean onRemoteImport(URI url1, Import.Mode mode1) {
        return url.equals(url1) && mode.equals(mode1);
      }
    };
  }

  public Visitor<Expr, Boolean> onEnvImport(final String value, final Import.Mode mode) {
    return new FalseVisitor() {
      public Boolean onEnvImport(String value1, Import.Mode mode1) {
        return value.equals(value1) && mode.equals(mode1);
      }
    };
  }

  public Visitor<Expr, Boolean> onMissingImport(final Import.Mode mode) {
    return new FalseVisitor() {
      public Boolean onMissingImport(Import.Mode mode1) {
        return mode.equals(mode1);
      }
    };
  }

  public Visitor<Expr, Boolean> onNote(Expr base, Source source) {
    return base.acceptExternal(this);
  }

  private abstract static class FalseVisitor extends ConstantVisitor.External<Boolean> {
    FalseVisitor() {
      super(false);
    }
  }
}
