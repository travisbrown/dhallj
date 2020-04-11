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
    }

    return currentLambda;
  }

  static final Expr apply(Expr base, final List<Expr> args) {
    String builtIn = Expr.Util.asBuiltIn(base);

    if (builtIn != null) {
      if (builtIn.equals("Natural/fold")) {
        Expr result = naturalFold(base, args);

        if (result != null) {
          return result;
        }

      } else if (builtIn.equals("List/fold") && args.size() > 1) {
        Expr result = listFold(base, args);

        if (result != null) {
          return result;
        }
      } else if (builtIn.equals("Optional/fold") && args.size() > 1) {
        Expr result = optionalFold(base, args);

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

  private static Expr indexedRecordType(Expr type) {
    Entry[] fields = {
      new SimpleImmutableEntry("index", Expr.Constants.NATURAL),
      new SimpleImmutableEntry("value", type)
    };
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
        builder.append("\\\\\"");
      } else if (c == '$') {
        builder.append("\\\\u0024");
      } else if (c == '\\') {
        builder.append("\\\\");
      } else if (c >= '\u0000' && c <= '\u001f') {
        builder.append('\\');
        builder.append(String.format("\\u%04X", (long) c));
      } else {
        builder.append(c);
      }
    }
    builder.append("\\\"");

    return builder.toString();
  }

  private static final Expr arity1(String identifier, Expr arg) {
    if (identifier.equals("Natural/isZero")) {
      BigInteger argAsNaturalLiteral = Expr.Util.asNaturalLiteral(arg);

      if (argAsNaturalLiteral != null) {
        return NormalizationUtilities.booleanToExpr(argAsNaturalLiteral.equals(BigInteger.ZERO));
      }
    } else if (identifier.equals("Natural/even")) {
      BigInteger argAsNaturalLiteral = Expr.Util.asNaturalLiteral(arg);

      if (argAsNaturalLiteral != null) {
        return NormalizationUtilities.booleanToExpr(isBigIntegerEven(argAsNaturalLiteral));
      }
    } else if (identifier.equals("Natural/odd")) {
      BigInteger argAsNaturalLiteral = Expr.Util.asNaturalLiteral(arg);

      if (argAsNaturalLiteral != null) {
        return NormalizationUtilities.booleanToExpr(!isBigIntegerEven(argAsNaturalLiteral));
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
          .accept(BetaNormalize.instance);
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
          .accept(BetaNormalize.instance);
    } else if (identifier.equals("Optional/build")) {
      return Expr.makeApplication(
              Expr.makeApplication(
                  Expr.makeApplication(arg2, Expr.makeApplication(Expr.Constants.OPTIONAL, arg1)),
                  Expr.makeLambda(
                      "a",
                      arg1,
                      Expr.makeApplication(Expr.Constants.SOME, Expr.makeIdentifier("a")))),
              Expr.makeApplication(Expr.Constants.NONE, arg1))
          .accept(BetaNormalize.instance);
    }
    if (identifier.equals("Natural/subtract")) {
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
    BigInteger firstAsNaturalLiteral = Expr.Util.asNaturalLiteral(args.get(0));

    if (firstAsNaturalLiteral != null) {
      List<Expr> newArgs = new ArrayList<Expr>(4);

      // Temporarily necessary to match a bug in the Haskell implementation.
      if (args.size() == 1) {
        newArgs.add(args.get(0));
        newArgs.add(Expr.makeIdentifier("natural"));
        newArgs.add(Expr.makeIdentifier("succ"));
        newArgs.add(Expr.makeIdentifier("zero"));
      } else if (args.size() == 2) {
        newArgs.add(args.get(0));
        newArgs.add(args.get(1));
        newArgs.add(Expr.makeIdentifier("succ"));
        newArgs.add(Expr.makeIdentifier("zero"));
      } else if (args.size() == 3) {
        newArgs.add(args.get(0));
        newArgs.add(args.get(1));
        newArgs.add(args.get(2).increment("zero"));
        newArgs.add(Expr.makeIdentifier("zero"));
      } else {
        newArgs.addAll(args);
      }

      Expr applied = null;
      if (firstAsNaturalLiteral.equals(BigInteger.ZERO)) {
        applied = newArgs.get(3);
      } else {
        applied =
            Expr.makeApplication(
                    newArgs.get(2),
                    Expr.makeApplication(
                        Expr.makeApplication(
                            Expr.makeApplication(
                                Expr.makeApplication(
                                    Expr.Constants.NATURAL_FOLD,
                                    Expr.makeNaturalLiteral(
                                        firstAsNaturalLiteral.subtract(BigInteger.ONE))),
                                newArgs.get(1)),
                            newArgs.get(2)),
                        newArgs.get(3)))
                .accept(BetaNormalize.instance);
      }

      if (applied == null) {
        return null;
      }

      if (args.size() == 1) {
        return Expr.makeLambda(
            "natural",
            Expr.Constants.TYPE,
            Expr.makeLambda(
                "succ",
                Expr.makePi(Expr.makeIdentifier("natural"), Expr.makeIdentifier("natural")),
                Expr.makeLambda("zero", Expr.makeIdentifier("natural"), applied)));
      } else if (args.size() == 2) {
        return Expr.makeLambda(
            "succ",
            Expr.makePi(newArgs.get(1), newArgs.get(1)),
            Expr.makeLambda("zero", newArgs.get(1).increment("succ"), applied));
      } else if (args.size() == 3) {
        return Expr.makeLambda("zero", newArgs.get(1), applied);
      } else if (args.size() == 4) {
        return applied;
      } else {
        return Expr.makeApplication(applied, drop(newArgs, 4)).accept(BetaNormalize.instance);
      }
    }
    return null;
  }

  static final Expr listFold(Expr base, final List<Expr> args) {
    List<Expr> newArgs = new ArrayList<Expr>(5);

    if (args.size() == 2) {
      newArgs.add(args.get(0).increment("list").increment("cons").increment("nil"));
      newArgs.add(args.get(1).increment("list").increment("cons").increment("nil"));
      newArgs.add(Expr.makeIdentifier("list"));
      newArgs.add(Expr.makeIdentifier("cons"));
      newArgs.add(Expr.makeIdentifier("nil"));
    } else if (args.size() == 3) {
      newArgs.add(args.get(0).increment("cons").increment("nil"));
      newArgs.add(args.get(1).increment("cons").increment("nil"));
      newArgs.add(args.get(2).increment("cons").increment("nil"));
      newArgs.add(Expr.makeIdentifier("cons"));
      newArgs.add(Expr.makeIdentifier("nil"));
    } else if (args.size() == 4) {
      newArgs.add(args.get(0).increment("nil"));
      newArgs.add(args.get(1).increment("nil"));
      newArgs.add(args.get(2).increment("nil"));
      newArgs.add(args.get(3).increment("nil"));
      newArgs.add(Expr.makeIdentifier("nil"));
    } else {
      newArgs.addAll(args);
    }

    List<Expr> listArg = Expr.Util.asListLiteral(newArgs.get(1));

    if (listArg != null) {

      Expr applied = null;
      if (!listArg.isEmpty()) {
        Expr head = listArg.get(0);

        Expr tail;
        if (listArg.size() == 1) {
          tail = Expr.makeEmptyListLiteral(newArgs.get(0));

        } else {
          List<Expr> listArgTail = new ArrayList<>(listArg);
          listArgTail.remove(0);

          tail = Expr.makeNonEmptyListLiteral(listArgTail);
        }
        applied =
            Expr.makeApplication(
                    Expr.makeApplication(newArgs.get(3), head),
                    Expr.makeApplication(
                        Expr.makeApplication(
                            Expr.makeApplication(
                                Expr.makeApplication(
                                    Expr.makeApplication(Expr.Constants.LIST_FOLD, newArgs.get(0)),
                                    tail),
                                newArgs.get(2)),
                            newArgs.get(3)),
                        newArgs.get(4)))
                .accept(BetaNormalize.instance);
      } else {
        applied = newArgs.get(4);
      }

      if (args.size() == 2) {
        return Expr.makeLambda(
            "list",
            Expr.Constants.TYPE,
            Expr.makeLambda(
                "cons",
                Expr.makePi(
                    newArgs.get(0),
                    Expr.makePi(Expr.makeIdentifier("list"), Expr.makeIdentifier("list"))),
                Expr.makeLambda("nil", Expr.makeIdentifier("list"), applied)));
      } else if (args.size() == 3) {
        return Expr.makeLambda(
            "cons",
            Expr.makePi(newArgs.get(2).increment("cons"), newArgs.get(2).increment("cons")),
            Expr.makeLambda("nil", newArgs.get(2).increment("cons"), applied));
      } else if (args.size() == 4) {
        return Expr.makeLambda("nil", newArgs.get(2).increment("cons"), applied);
      } else if (args.size() == 5) {
        return applied;
      } else {
        return Expr.makeApplication(applied, drop(newArgs, 5)).accept(BetaNormalize.instance);
      }
    }
    return null;
  }

  static final Expr optionalFold(Expr base, final List<Expr> args) {
    List<Expr> newArgs = new ArrayList<Expr>(5);

    if (args.size() == 2) {
      newArgs.add(args.get(0).increment("optional").increment("some").increment("none"));
      newArgs.add(args.get(1).increment("optional").increment("some").increment("none"));
      newArgs.add(Expr.makeIdentifier("optional"));
      newArgs.add(Expr.makeIdentifier("some"));
      newArgs.add(Expr.makeIdentifier("none"));
    } else if (args.size() == 3) {
      newArgs.add(args.get(0).increment("some").increment("none"));
      newArgs.add(args.get(1).increment("some").increment("none"));
      newArgs.add(args.get(2).increment("some").increment("none"));
      newArgs.add(Expr.makeIdentifier("some"));
      newArgs.add(Expr.makeIdentifier("none"));
    } else if (args.size() == 4) {
      newArgs.add(args.get(0).increment("none"));
      newArgs.add(args.get(1).increment("none"));
      newArgs.add(args.get(2).increment("none"));
      newArgs.add(args.get(3).increment("none"));
      newArgs.add(Expr.makeIdentifier("none"));
    } else {
      newArgs.addAll(args);
    }

    Expr someArg = Expr.Util.getSomeArg(newArgs.get(1));
    Expr noneArg = Expr.Util.getNoneArg(newArgs.get(1));

    if (someArg != null || noneArg != null) {
      Expr applied =
          (someArg != null)
              ? (Expr.makeApplication(newArgs.get(3), someArg).accept(BetaNormalize.instance))
              : newArgs.get(4);

      if (args.size() == 2) {
        return Expr.makeLambda(
            "optional",
            Expr.Constants.TYPE,
            Expr.makeLambda(
                "some",
                Expr.makePi(Expr.makeIdentifier("optional"), Expr.makeIdentifier("optional")),
                Expr.makeLambda("none", Expr.makeIdentifier("optional"), applied)));
      } else if (args.size() == 3) {
        return Expr.makeLambda(
            "some",
            Expr.makePi(newArgs.get(2).increment("some"), newArgs.get(2).increment("some")),
            Expr.makeLambda("none", newArgs.get(2).increment("some"), applied));
      } else if (args.size() == 4) {
        return Expr.makeLambda("none", newArgs.get(2).increment("some"), applied);
      } else if (newArgs.size() == 5) {
        return applied;
      } else {
        return Expr.makeApplication(applied, drop(newArgs, 5)).accept(BetaNormalize.instance);
      }
    }
    return null;
  }
}
