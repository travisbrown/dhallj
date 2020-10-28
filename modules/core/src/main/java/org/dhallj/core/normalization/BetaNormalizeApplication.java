package org.dhallj.core.normalization;

import java.math.BigInteger;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import org.dhallj.core.Expr;
import org.dhallj.core.Operator;

final class BetaNormalizeApplication {
  private static Expr applyLambdas(Expr base, final List<Expr> args) {
    Expr currentLambda = null;
    Expr current = base;
    int i = 0;

    while (current != null && i < args.size()) {
      current = Expr.Util.applyAsLambda(current, args.get(i));
      if (current == null) {
        break;
      } else {
        currentLambda = current.accept(BetaNormalize.instance);
        i += 1;
      }
    }

    if (currentLambda != null) {
      for (int j = i; j < args.size(); j++) {
        currentLambda = Expr.makeApplication(currentLambda, args.get(j));
      }
      currentLambda = currentLambda.accept(BetaNormalize.instance);
    }

    return currentLambda;
  }

  private static final Expr booleanToExpr(boolean value) {
    return value ? Expr.Constants.TRUE : Expr.Constants.FALSE;
  }

  static final Expr apply(Expr base, List<Expr> args) {
    String builtIn = Expr.Util.asBuiltIn(base);

    if (builtIn != null) {
      if (builtIn.equals("Natural/fold") && args.size() >= 4) {
        Expr result = naturalFold(args);

        if (result != null) {
          return result;
        }
      } else if (builtIn.equals("List/fold") && args.size() >= 5) {
        Expr result = listFold(args);

        if (result != null) {
          return result;
        }
      } else if (builtIn.equals("Text/replace") && args.size() >= 3) {
        Expr result = textReplace(args);

        if (result != null) {
          return result;
        }
      } else if (args.size() == 1) {
        Expr result = arity1(builtIn, args.get(0));

        if (result != null) {
          return result;
        }
      } else if (args.size() == 2) {
        Expr result = arity2(builtIn, args.get(0), args.get(1));

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

  private static final Entry<String, Expr> indexField =
      new SimpleImmutableEntry<>("index", Expr.Constants.NATURAL);

  private static final Expr indexedRecordType(Expr type) {
    Entry[] fields = {indexField, new SimpleImmutableEntry<>("value", type)};
    return Expr.makeRecordType(fields);
  }

  private static final BigInteger TWO = new BigInteger("2");

  private static final boolean isBigIntegerEven(BigInteger value) {
    return value.mod(TWO).equals(BigInteger.ZERO);
  }

  private static final boolean isBigIntegerNatural(BigInteger value) {
    return value.compareTo(BigInteger.ZERO) >= 0;
  }

  private static final Expr arity1(String identifier, Expr arg) {
    if (identifier.equals("Natural/isZero")) {
      BigInteger argAsNaturalLiteral = Expr.Util.asNaturalLiteral(arg);

      if (argAsNaturalLiteral != null) {
        return booleanToExpr(argAsNaturalLiteral.equals(BigInteger.ZERO));
      }
    } else if (identifier.equals("Natural/even")) {
      BigInteger argAsNaturalLiteral = Expr.Util.asNaturalLiteral(arg);

      if (argAsNaturalLiteral != null) {
        return booleanToExpr(isBigIntegerEven(argAsNaturalLiteral));
      }
    } else if (identifier.equals("Natural/odd")) {
      BigInteger argAsNaturalLiteral = Expr.Util.asNaturalLiteral(arg);

      if (argAsNaturalLiteral != null) {
        return booleanToExpr(!isBigIntegerEven(argAsNaturalLiteral));
      }
    } else if (identifier.equals("Natural/toInteger")) {
      BigInteger argAsNaturalLiteral = Expr.Util.asNaturalLiteral(arg);

      if (argAsNaturalLiteral != null) {
        return Expr.makeIntegerLiteral(argAsNaturalLiteral);
      }
    } else if (identifier.equals("Natural/show")) {
      BigInteger argAsNaturalLiteral = Expr.Util.asNaturalLiteral(arg);

      if (argAsNaturalLiteral != null) {
        return Expr.makeTextLiteral(argAsNaturalLiteral.toString());
      }
    } else if (identifier.equals("Integer/negate")) {
      BigInteger argAsIntegerLiteral = Expr.Util.asIntegerLiteral(arg);

      if (argAsIntegerLiteral != null) {
        return Expr.makeIntegerLiteral(argAsIntegerLiteral.negate());
      }
    } else if (identifier.equals("Integer/clamp")) {
      BigInteger argAsIntegerLiteral = Expr.Util.asIntegerLiteral(arg);

      if (argAsIntegerLiteral != null) {
        if (isBigIntegerNatural(argAsIntegerLiteral)) {
          return Expr.makeNaturalLiteral(argAsIntegerLiteral);
        } else {
          return Expr.makeNaturalLiteral(BigInteger.ZERO);
        }
      }
    } else if (identifier.equals("Integer/toDouble")) {
      BigInteger argAsIntegerLiteral = Expr.Util.asIntegerLiteral(arg);

      if (argAsIntegerLiteral != null) {
        return Expr.makeDoubleLiteral(argAsIntegerLiteral.doubleValue());
      }
    } else if (identifier.equals("Integer/show")) {
      BigInteger argAsIntegerLiteral = Expr.Util.asIntegerLiteral(arg);

      if (argAsIntegerLiteral != null) {
        String sign = isBigIntegerNatural(argAsIntegerLiteral) ? "+" : "";
        return Expr.makeTextLiteral(sign + argAsIntegerLiteral.toString());
      }
    } else if (identifier.equals("Double/show")) {
      Double argAsDoubleLiteral = Expr.Util.asDoubleLiteral(arg);

      if (argAsDoubleLiteral != null) {
        return Expr.makeTextLiteral(argAsDoubleLiteral.toString());
      }
    } else if (identifier.equals("Text/show")) {
      String argAsSimpleTextLiteral = Expr.Util.asSimpleTextLiteral(arg);

      if (argAsSimpleTextLiteral != null) {
        return Expr.makeTextLiteral(Expr.Util.escapeText(argAsSimpleTextLiteral, true));
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
          .accept(BetaNormalize.instance);
    }
    // None matched, so we can't simplify.
    return null;
  }

  private static final Expr[] prependValue = new Expr[] {Expr.makeIdentifier("a")};
  private static final Expr prependExpr =
      Expr.makeOperatorApplication(
          Operator.LIST_APPEND,
          Expr.makeNonEmptyListLiteral(prependValue),
          Expr.makeIdentifier("as"));

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
                          prependExpr))),
              Expr.makeEmptyListLiteral(listA))
          .accept(BetaNormalize.instance);
    } else if (identifier.equals("Natural/subtract")) {
      BigInteger firstAsNaturalLiteral = Expr.Util.asNaturalLiteral(arg1);
      BigInteger secondAsNaturalLiteral = Expr.Util.asNaturalLiteral(arg2);

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
      List<Expr> argAsListLiteral = Expr.Util.asListLiteral(arg2);

      if (argAsListLiteral != null) {
        return Expr.makeNaturalLiteral(BigInteger.valueOf(argAsListLiteral.size()));
      }
    } else if (identifier.equals("List/reverse")) {
      List<Expr> argAsListLiteral = Expr.Util.asListLiteral(arg2);

      if (argAsListLiteral != null) {
        List<Expr> result = new ArrayList<>(argAsListLiteral);
        Collections.reverse(result);
        if (result.isEmpty()) {
          return arg2;
        } else {
          return Expr.makeNonEmptyListLiteral(result);
        }
      }
    } else if (identifier.equals("List/head")) {
      List<Expr> argAsListLiteral = Expr.Util.asListLiteral(arg2);

      if (argAsListLiteral != null) {
        if (argAsListLiteral.isEmpty()) {
          return Expr.makeApplication(Expr.Constants.NONE, arg1);
        } else {
          return Expr.makeApplication(Expr.Constants.SOME, argAsListLiteral.get(0));
        }
      }
    } else if (identifier.equals("List/last")) {
      List<Expr> argAsListLiteral = Expr.Util.asListLiteral(arg2);

      if (argAsListLiteral != null) {
        if (argAsListLiteral.isEmpty()) {
          return Expr.makeApplication(Expr.Constants.NONE, arg1);
        } else {
          return Expr.makeApplication(
              Expr.Constants.SOME, argAsListLiteral.get(argAsListLiteral.size() - 1));
        }
      }
    } else if (identifier.equals("List/indexed")) {
      List<Expr> argAsListLiteral = Expr.Util.asListLiteral(arg2);

      if (argAsListLiteral != null) {
        List<Expr> result = new ArrayList<>(argAsListLiteral.size());
        int i = 0;

        for (Expr value : argAsListLiteral) {
          List<Entry<String, Expr>> fields = new ArrayList<>();
          fields.add(
              new SimpleImmutableEntry<>(
                  "index", Expr.makeNaturalLiteral(BigInteger.valueOf(i++))));
          fields.add(new SimpleImmutableEntry<>("value", value));
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

  private static final Expr naturalFold(List<Expr> args) {
    BigInteger firstAsNaturalLiteral = Expr.Util.asNaturalLiteral(args.get(0));

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
                .accept(BetaNormalize.instance);
      }

      if (applied == null) {
        return null;
      }

      if (args.size() == 4) {
        return applied;
      } else {
        return Expr.makeApplication(applied, drop(args, 4)).accept(BetaNormalize.instance);
      }
    }
    return null;
  }

  private static final Expr listFold(List<Expr> args) {
    List<Expr> listArg = Expr.Util.asListLiteral(args.get(1));

    if (listArg != null) {
      Expr applied = null;
      if (!listArg.isEmpty()) {
        Expr head = listArg.get(0);

        Expr tail;
        if (listArg.size() == 1) {
          tail = Expr.makeEmptyListLiteral(Expr.makeApplication(Expr.Constants.LIST, args.get(0)));
        } else {
          List<Expr> listArgTail = new ArrayList<>(listArg);
          listArgTail.remove(0);

          tail = Expr.makeNonEmptyListLiteral(listArgTail);
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
                .accept(BetaNormalize.instance);
      } else {
        applied = args.get(4);
      }

      if (args.size() == 5) {
        return applied;
      } else {
        return Expr.makeApplication(applied, drop(args, 5)).accept(BetaNormalize.instance);
      }
    }
    return null;
  }

  private static final Expr textReplace(List<Expr> args) {
    String needle = Expr.Util.asSimpleTextLiteral(args.get(0));

    if (needle != null) {
      if (needle.length() == 0) {
        return args.get(2);
      } else {
        String haystack = Expr.Util.asSimpleTextLiteral(args.get(2));

        if (haystack != null) {
          String pattern = java.util.regex.Pattern.quote(needle);
          String[] parts = haystack.split(pattern, -1);

          Expr[] interpolated = new Expr[parts.length - 1];
          Expr replacement = args.get(1);

          for (int i = 0; i < parts.length - 1; i += 1) {
            interpolated[i] = replacement;
          }

          return Expr.makeTextLiteral(parts, interpolated).accept(BetaNormalize.instance);
        }
      }
    }
    return null;
  }
}
