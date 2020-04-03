package org.dhallj.core.normalization;

import java.math.BigInteger;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import org.dhallj.core.Expr;
import org.dhallj.core.Operator;
import org.dhallj.core.ast.AsApplication;
import org.dhallj.core.ast.CollectApplication;
import org.dhallj.core.visitor.ConstantVisitor;

final class BetaNormalizeApplication {

  private static class ApplyLambda extends ConstantVisitor.External<Expr> {
    private final Expr arg;

    ApplyLambda(Expr arg) {
      super(null);
      this.arg = arg;
    }

    @Override
    public Expr onLambda(String name, Expr type, Expr result) {
      return result.substitute(name, arg.increment(name)).decrement(name);
    }
  }

  private static Expr applyLambdas(Expr base, final List<Expr> args) {
    Expr currentLambda = null;
    Expr current = base;
    int i = 0;

    while (current != null && i < args.size()) {
      current = current.acceptExternal(new ApplyLambda(args.get(i)));
      if (current == null) {
        break;
      } else {
        currentLambda = current.acceptVis(BetaNormalize.instance);
        i += 1;
      }
    }

    if (currentLambda != null) {
      for (int j = i; j < args.size(); j++) {
        currentLambda = Expr.makeApplication(currentLambda, args.get(j));
      }
    }

    return currentLambda;
  }

  static final Expr apply(Expr base, final List<Expr> args) {

    String identifier = base.asSimpleIdentifier();
    final Expr arg = args.get(0);

    if (identifier != null) {
      if (identifier.equals("Natural/fold")) {
        if (args.size() >= 4) {
          Expr result = naturalFold(base, args);

          if (result != null) {
            return result;
          }
        }
      } else if (identifier.equals("List/fold")) {
        if (args.size() >= 5) {
          Expr result = listFold(base, args);

          if (result != null) {
            return result;
          }
        }
      } else if (identifier.equals("Optional/fold")) {
        if (args.size() >= 5) {
          Expr result = optionalFold(base, args);

          if (result != null) {
            return result;
          }
        }
      } else if (args.size() == 1) {
        Expr result = arity1(identifier, args.get(0));

        if (result != null) {
          return result;
        }
      } else if (args.size() == 2) {
        Expr result = arity2(identifier, args.get(0), args.get(1));

        if (result != null) {
          return result;
        }
      }
    } else {
      Expr result = applyLambdas(base, args);

      if (result != null) {
        return result;
      }
    }

    return Expr.makeApplication(base, args);
  }

  private static Expr indexedRecordType(Expr type) {
    List<Entry<String, Expr>> fields = new ArrayList(2);
    fields.add(new SimpleImmutableEntry("index", Expr.Constants.NATURAL));
    fields.add(new SimpleImmutableEntry("value", type));
    return Expr.makeRecordType(fields);
  }

  private static BigInteger TWO = new BigInteger("2");

  private static boolean isBigIntegerEven(BigInteger value) {
    return value.mod(TWO).equals(BigInteger.ZERO);
  }

  private static boolean isBigIntegerNatural(BigInteger value) {
    return value.compareTo(BigInteger.ZERO) >= 0;
  }

  private static String escapeText(String input) {
    StringBuilder builder = new StringBuilder("\\\"");

    for (int i = 0; i < input.length(); i++) {
      char c = input.charAt(i);

      if (c == '"') {
        builder.append("\\\"");
      } else if (c == '$') {
        builder.append('\u0024');
      } else if (c == '\\') {
        builder.append("\\\\");
      } else if (c == '\b') {
        builder.append("\\b");
      } else if (c == '\f') {
        builder.append("\\f");
      } else if (c == '\n') {
        builder.append("\\n");
      } else if (c == '\r') {
        builder.append("\\r");
      } else if (c == '\t') {
        builder.append("\\t");
      } else if (c >= '\u0000' && c <= '\u001f') {
        builder.append(String.format("\\u%04X", c));
      } else {
        builder.append(c);
      }
    }
    builder.append("\\\"");

    return builder.toString();
  }

  private static final Expr arity1(String identifier, Expr arg) {
    if (identifier.equals("Natural/isZero")) {
      BigInteger argAsNaturalLiteral = arg.asNaturalLiteral();

      if (argAsNaturalLiteral != null) {
        return NormalizationUtilities.booleanToExpr(argAsNaturalLiteral.equals(BigInteger.ZERO));
      }
    } else if (identifier.equals("Natural/even")) {
      BigInteger argAsNaturalLiteral = arg.asNaturalLiteral();

      if (argAsNaturalLiteral != null) {
        return NormalizationUtilities.booleanToExpr(isBigIntegerEven(argAsNaturalLiteral));
      }
    } else if (identifier.equals("Natural/odd")) {
      BigInteger argAsNaturalLiteral = arg.asNaturalLiteral();

      if (argAsNaturalLiteral != null) {
        return NormalizationUtilities.booleanToExpr(!isBigIntegerEven(argAsNaturalLiteral));
      }
    } else if (identifier.equals("Natural/toInteger")) {
      BigInteger argAsNaturalLiteral = arg.asNaturalLiteral();

      if (argAsNaturalLiteral != null) {
        return Expr.makeIntegerLiteral(argAsNaturalLiteral);
      }
    } else if (identifier.equals("Natural/show")) {
      BigInteger argAsNaturalLiteral = arg.asNaturalLiteral();

      if (argAsNaturalLiteral != null) {
        return Expr.makeTextLiteral(argAsNaturalLiteral.toString());
      }
    } else if (identifier.equals("Integer/negate")) {
      BigInteger argAsIntegerLiteral = arg.asIntegerLiteral();

      if (argAsIntegerLiteral != null) {
        return Expr.makeIntegerLiteral(argAsIntegerLiteral.negate());
      }
    } else if (identifier.equals("Integer/clamp")) {
      BigInteger argAsIntegerLiteral = arg.asIntegerLiteral();

      if (argAsIntegerLiteral != null) {
        if (isBigIntegerNatural(argAsIntegerLiteral)) {
          return Expr.makeNaturalLiteral(argAsIntegerLiteral);
        } else {
          return Expr.makeNaturalLiteral(BigInteger.ZERO);
        }
      }
    } else if (identifier.equals("Integer/toDouble")) {
      BigInteger argAsIntegerLiteral = arg.asIntegerLiteral();

      if (argAsIntegerLiteral != null) {
        return Expr.makeDoubleLiteral(argAsIntegerLiteral.doubleValue());
      }
    } else if (identifier.equals("Integer/show")) {
      BigInteger argAsIntegerLiteral = arg.asIntegerLiteral();

      if (argAsIntegerLiteral != null) {
        String sign = isBigIntegerNatural(argAsIntegerLiteral) ? "+" : "";
        return Expr.makeTextLiteral(sign + argAsIntegerLiteral.toString());
      }
    } else if (identifier.equals("Double/show")) {
      Double argAsDoubleLiteral = arg.asDoubleLiteral();

      if (argAsDoubleLiteral != null) {
        return Expr.makeTextLiteral(argAsDoubleLiteral.toString());
      }
    } else if (identifier.equals("Text/show")) {
      String argAsSimpleTextLiteral = arg.asSimpleTextLiteral();

      if (argAsSimpleTextLiteral != null) {
        return Expr.makeTextLiteral(escapeText(argAsSimpleTextLiteral));
      }
    } else if (identifier.equals("Natural/build")) {
      return Expr.makeApplication(
              Expr.makeApplication(
                  Expr.makeApplication(arg, Expr.Constants.NATURAL),
                  Expr.makeLambda(
                      "x",
                      Expr.Constants.NATURAL,
                      Expr.makeOperatorApplication(
                          Operator.PLUS,
                          Expr.makeIdentifier("x"),
                          Expr.makeNaturalLiteral(BigInteger.ONE)))),
              Expr.makeNaturalLiteral(BigInteger.ZERO))
          .acceptVis(BetaNormalize.instance);
    }
    // None matched, so we can't simplify.
    return null;
  }

  private static final Expr arity2(String identifier, Expr arg1, Expr arg2) {

    if (identifier.equals("List/build")) {
      Expr listA = Expr.makeApplication(Expr.Constants.LIST, arg1);

      return Expr.makeApplication(
              Expr.makeApplication(
                  Expr.makeApplication(arg2, listA),
                  Expr.makeLambda(
                      "a",
                      arg1,
                      Expr.makeLambda(
                          "as",
                          Expr.makeApplication(Expr.Constants.LIST, arg1.increment("a")),
                          NormalizationUtilities.prependExpr))),
              Expr.makeEmptyListLiteral(listA))
          .acceptVis(BetaNormalize.instance);
    } else if (identifier.equals("Optional/build")) {
      return Expr.makeApplication(
              Expr.makeApplication(
                  Expr.makeApplication(arg2, Expr.makeApplication(Expr.Constants.OPTIONAL, arg1)),
                  Expr.makeLambda(
                      "a",
                      arg1,
                      Expr.makeApplication(Expr.Constants.SOME, Expr.makeIdentifier("a")))),
              Expr.makeApplication(Expr.Constants.NONE, arg1))
          .acceptVis(BetaNormalize.instance);
    }
    if (identifier.equals("Natural/subtract")) {
      BigInteger firstAsNaturalLiteral = arg1.asNaturalLiteral();
      BigInteger secondAsNaturalLiteral = arg2.asNaturalLiteral();

      if (firstAsNaturalLiteral != null) {
        if (secondAsNaturalLiteral != null) {
          if (firstAsNaturalLiteral.compareTo(secondAsNaturalLiteral) < 0) {
            return Expr.makeNaturalLiteral(secondAsNaturalLiteral.subtract(firstAsNaturalLiteral));
          } else {
            return Expr.Constants.ZERO;
          }
        } else {
          if (firstAsNaturalLiteral.equals(BigInteger.ZERO)) {
            return arg2;
          }
        }
      } else {
        if (secondAsNaturalLiteral != null && secondAsNaturalLiteral.equals(BigInteger.ZERO)) {
          return Expr.Constants.ZERO;
        }
      }

      if (arg1.equivalent(arg2)) {
        return Expr.Constants.ZERO;
      }
    } else if (identifier.equals("List/length")) {
      List<Expr> argAsListLiteral = arg2.asListLiteral();

      if (argAsListLiteral != null) {
        return Expr.makeNaturalLiteral(BigInteger.valueOf(argAsListLiteral.size()));
      }
    } else if (identifier.equals("List/reverse")) {
      List<Expr> argAsListLiteral = arg2.asListLiteral();

      if (argAsListLiteral != null) {
        List<Expr> result = new ArrayList(argAsListLiteral);
        Collections.reverse(result);
        if (result.isEmpty()) {
          return arg2;
        } else {
          return Expr.makeNonEmptyListLiteral(result);
        }
      }
    } else if (identifier.equals("List/head")) {
      List<Expr> argAsListLiteral = arg2.asListLiteral();

      if (argAsListLiteral != null) {
        if (argAsListLiteral.isEmpty()) {
          return Expr.makeApplication(Expr.Constants.NONE, arg1);
        } else {
          return Expr.makeApplication(Expr.Constants.SOME, argAsListLiteral.get(0));
        }
      }
    } else if (identifier.equals("List/last")) {
      List<Expr> argAsListLiteral = arg2.asListLiteral();

      if (argAsListLiteral != null) {
        if (argAsListLiteral.isEmpty()) {
          return Expr.makeApplication(Expr.Constants.NONE, arg1);
        } else {
          return Expr.makeApplication(
              Expr.Constants.SOME, argAsListLiteral.get(argAsListLiteral.size() - 1));
        }
      }
    } else if (identifier.equals("List/indexed")) {
      List<Expr> argAsListLiteral = arg2.asListLiteral();

      if (argAsListLiteral != null) {
        List<Expr> result = new ArrayList(argAsListLiteral.size());
        int i = 0;

        for (Expr value : argAsListLiteral) {
          List<Entry<String, Expr>> fields = new ArrayList();
          fields.add(
              new SimpleImmutableEntry("index", Expr.makeNaturalLiteral(BigInteger.valueOf(i++))));
          fields.add(new SimpleImmutableEntry("value", value));
          result.add(Expr.makeRecordLiteral(fields));
        }

        if (result.isEmpty()) {
          return Expr.makeEmptyListLiteral(
              Expr.makeApplication(Expr.Constants.LIST, indexedRecordType(arg1)));
        } else {
          return Expr.makeNonEmptyListLiteral(result);
        }
      }
    }
    // None matched, so we can't simplify.
    return null;
  }

  private static List<Expr> drop(List<Expr> args, int count) {
    List<Expr> result = new ArrayList<Expr>(args.size() - count);
    for (int i = count; i < args.size(); i++) {
      result.add(args.get(i));
    }
    return result;
  }

  static final Expr naturalFold(Expr base, final List<Expr> args) {
    BigInteger firstAsNaturalLiteral = args.get(0).asNaturalLiteral();

    if (firstAsNaturalLiteral != null) {
      Expr applied = null;
      if (firstAsNaturalLiteral.equals(BigInteger.ZERO)) {
        applied = args.get(3);
      } else {
        applied =
            Expr.makeApplication(
                    args.get(2),
                    Expr.makeApplication(
                        Expr.makeApplication(
                            Expr.makeApplication(
                                Expr.makeApplication(
                                    Expr.Constants.NATURAL_FOLD,
                                    Expr.makeNaturalLiteral(
                                        firstAsNaturalLiteral.subtract(BigInteger.ONE))),
                                args.get(1)),
                            args.get(2)),
                        args.get(3)))
                .acceptVis(BetaNormalize.instance);
      }
      if (args.size() == 4) {
        return applied;
      } else {
        return Expr.makeApplication(applied, drop(args, 4)).acceptVis(BetaNormalize.instance);
      }
    }
    return null;
  }

  static final Expr listFold(Expr base, final List<Expr> args) {
    List<Expr> listArg = args.get(1).asListLiteral();

    if (listArg != null) {
      Expr applied = null;
      if (!listArg.isEmpty()) {
        Expr head = listArg.get(0);
        listArg.remove(0);

        Expr tail;
        if (listArg.isEmpty()) {
          tail = Expr.makeEmptyListLiteral(args.get(0));

        } else {
          tail = Expr.makeNonEmptyListLiteral(listArg);
        }
        applied =
            Expr.makeApplication(
                    Expr.makeApplication(args.get(3), head),
                    Expr.makeApplication(
                        Expr.makeApplication(
                            Expr.makeApplication(
                                Expr.makeApplication(
                                    Expr.makeApplication(Expr.Constants.LIST_FOLD, args.get(0)),
                                    tail),
                                args.get(2)),
                            args.get(3)),
                        args.get(4)))
                .acceptVis(BetaNormalize.instance);
      } else {
        applied = args.get(4);
      }

      if (args.size() == 5) {
        return applied;
      } else {
        return Expr.makeApplication(applied, drop(args, 5)).acceptVis(BetaNormalize.instance);
      }
    }
    return null;
  }

  static final Expr optionalFold(Expr base, final List<Expr> args) {
    Entry<Expr, Expr> optionArg = args.get(1).acceptExternal(AsApplication.instance);

    if (optionArg != null) {
      String constructor = optionArg.getKey().asSimpleIdentifier();

      if (constructor != null) {
        Expr applied = null;
        if (constructor.equals("Some")) {
          applied =
              Expr.makeApplication(args.get(3), optionArg.getValue())
                  .acceptVis(BetaNormalize.instance);
        } else if (constructor.equals("None")) {
          applied = args.get(4);
        }

        if (args.size() == 5) {
          return applied;
        } else {
          return Expr.makeApplication(applied, drop(args, 5)).acceptVis(BetaNormalize.instance);
        }
      }
    }
    return null;
  }
}
