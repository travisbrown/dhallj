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

  static final Expr apply(Expr base, final Expr arg) {

    String identifier = base.asSimpleIdentifier();

    if (identifier != null) {
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
            .accept(BetaNormalize.instance);
      }
    } else {
      Expr result =
          base.acceptExternal(
              new ConstantVisitor.External<Expr>(null) {

                public Expr onLambda(String param, Expr input, Expr result) {
                  return result.substitute(param, arg.increment(param)).decrement(param);
                }

                public Expr onApplication(Expr base0, Expr arg0) {
                  String base0AsIdentifier = base0.asSimpleIdentifier();

                  if (base0AsIdentifier != null) {
                    if (base0AsIdentifier.equals("List/build")) {
                      Expr listA = Expr.makeApplication(Expr.Constants.LIST, arg0);

                      return Expr.makeApplication(
                          Expr.makeApplication(
                              Expr.makeApplication(arg, listA),
                              Expr.makeLambda(
                                  "a",
                                  arg0,
                                  Expr.makeLambda(
                                      "as",
                                      Expr.makeApplication(
                                          Expr.Constants.LIST, arg0.increment("a")),
                                      NormalizationUtilities.prependExpr))),
                          Expr.makeEmptyListLiteral(listA));
                    } else if (base0AsIdentifier.equals("Optional/build")) {
                      return Expr.makeApplication(
                          Expr.makeApplication(
                              Expr.makeApplication(
                                  arg, Expr.makeApplication(Expr.Constants.OPTIONAL, arg0)),
                              Expr.makeLambda(
                                  "a",
                                  arg0,
                                  Expr.makeApplication(
                                      Expr.Constants.SOME, Expr.makeIdentifier("a")))),
                          Expr.makeApplication(Expr.Constants.NONE, arg0));
                    } else if (base0AsIdentifier.equals("Natural/subtract")) {
                      Expr first = arg0;
                      Expr second = arg;
                      BigInteger firstAsNaturalLiteral = first.asNaturalLiteral();
                      BigInteger secondAsNaturalLiteral = second.asNaturalLiteral();

                      if (firstAsNaturalLiteral != null) {
                        if (secondAsNaturalLiteral != null) {
                          if (firstAsNaturalLiteral.compareTo(secondAsNaturalLiteral) < 0) {
                            return Expr.makeNaturalLiteral(
                                secondAsNaturalLiteral.subtract(firstAsNaturalLiteral));
                          } else {
                            return Expr.Constants.ZERO;
                          }
                        } else {
                          if (firstAsNaturalLiteral.equals(BigInteger.ZERO)) {
                            return second;
                          }
                        }
                      } else {
                        if (secondAsNaturalLiteral != null
                            && secondAsNaturalLiteral.equals(BigInteger.ZERO)) {
                          return Expr.Constants.ZERO;
                        }
                      }

                      if (first.equivalent(second)) {
                        return Expr.Constants.ZERO;
                      }
                    } else if (base0AsIdentifier.equals("List/length")) {
                      List<Expr> argAsListLiteral = arg.asListLiteral();

                      if (argAsListLiteral != null) {
                        return Expr.makeNaturalLiteral(BigInteger.valueOf(argAsListLiteral.size()));
                      }
                    } else if (base0AsIdentifier.equals("List/reverse")) {
                      List<Expr> argAsListLiteral = arg.asListLiteral();

                      if (argAsListLiteral != null) {
                        List<Expr> result = new ArrayList(argAsListLiteral);
                        Collections.reverse(result);
                        if (result.isEmpty()) {
                          return arg;
                        } else {
                          return Expr.makeNonEmptyListLiteral(result);
                        }
                      }
                    } else if (base0AsIdentifier.equals("List/head")) {
                      List<Expr> argAsListLiteral = arg.asListLiteral();

                      if (argAsListLiteral != null) {
                        if (argAsListLiteral.isEmpty()) {
                          return Expr.makeApplication(Expr.Constants.NONE, arg0);
                        } else {
                          return Expr.makeApplication(Expr.Constants.SOME, argAsListLiteral.get(0));
                        }
                      }
                    } else if (base0AsIdentifier.equals("List/last")) {
                      List<Expr> argAsListLiteral = arg.asListLiteral();

                      if (argAsListLiteral != null) {
                        if (argAsListLiteral.isEmpty()) {
                          return Expr.makeApplication(Expr.Constants.NONE, arg0);
                        } else {
                          return Expr.makeApplication(
                              Expr.Constants.SOME,
                              argAsListLiteral.get(argAsListLiteral.size() - 1));
                        }
                      }
                    } else if (base0AsIdentifier.equals("List/indexed")) {
                      List<Expr> argAsListLiteral = arg.asListLiteral();

                      if (argAsListLiteral != null) {
                        List<Expr> result = new ArrayList(argAsListLiteral.size());
                        int i = 0;

                        for (Expr value : argAsListLiteral) {
                          List<Entry<String, Expr>> fields = new ArrayList();
                          fields.add(
                              new SimpleImmutableEntry(
                                  "index", Expr.makeNaturalLiteral(BigInteger.valueOf(i++))));
                          fields.add(new SimpleImmutableEntry("value", value));
                          result.add(Expr.makeRecordLiteral(fields));
                        }

                        if (result.isEmpty()) {
                          return Expr.makeEmptyListLiteral(
                              Expr.makeApplication(Expr.Constants.LIST, indexedRecordType(arg0)));
                        } else {
                          return Expr.makeNonEmptyListLiteral(result);
                        }
                      }
                    }
                    return null;
                  } else {
                    List<Expr> collected =
                        base0.acceptExternal(new CollectApplication(base0, arg0));
                    String firstAsIdentifier = collected.get(0).asSimpleIdentifier();

                    if (firstAsIdentifier != null) {
                      if (firstAsIdentifier.equals("Optional/fold")) {
                        if (collected.size() == 5) {
                          Entry<Expr, Expr> optionArg =
                              collected.get(2).acceptExternal(AsApplication.instance);
                          if (optionArg != null) {
                            String constructor = optionArg.getKey().asSimpleIdentifier();

                            if (constructor != null) {
                              if (constructor.equals("Some")) {
                                return Expr.makeApplication(arg0, optionArg.getValue());
                              } else {
                                return arg;
                              }
                            }
                          }
                        }
                      } else if (firstAsIdentifier.equals("List/fold")) {
                        if (collected.size() == 5) {
                          List<Expr> listArg = collected.get(2).asListLiteral();
                          if (listArg != null) {
                            if (!listArg.isEmpty()) {
                              Expr head = listArg.get(0);
                              listArg.remove(0);

                              Expr tail;
                              if (listArg.isEmpty()) {
                                tail = Expr.makeEmptyListLiteral(collected.get(1));

                              } else {
                                tail = Expr.makeNonEmptyListLiteral(listArg);
                              }
                              return Expr.makeApplication(
                                  Expr.makeApplication(collected.get(4), head),
                                  Expr.makeApplication(
                                      Expr.makeApplication(
                                          Expr.makeApplication(
                                              Expr.makeApplication(
                                                  Expr.makeApplication(
                                                      Expr.Constants.LIST_FOLD, collected.get(1)),
                                                  tail),
                                              collected.get(3)),
                                          collected.get(4)),
                                      arg));
                            } else {
                              return arg;
                            }
                          }
                        }
                      } else if (firstAsIdentifier.equals("Natural/fold")) {
                        if (collected.size() == 4) {
                          BigInteger firstAsNaturalLiteral = collected.get(1).asNaturalLiteral();

                          if (firstAsNaturalLiteral != null) {
                            if (firstAsNaturalLiteral.equals(BigInteger.ZERO)) {
                              return arg;
                            } else {
                              return Expr.makeApplication(
                                  collected.get(3),
                                  Expr.makeApplication(
                                      Expr.makeApplication(
                                          Expr.makeApplication(
                                              Expr.makeApplication(
                                                  Expr.Constants.NATURAL_FOLD,
                                                  Expr.makeNaturalLiteral(
                                                      firstAsNaturalLiteral.subtract(
                                                          BigInteger.ONE))),
                                              collected.get(2)),
                                          collected.get(3)),
                                      arg));
                            }
                          }
                        }
                      }
                    }
                  }
                  return null;
                }
              });

      if (result != null) {
        return result.accept(BetaNormalize.instance);
      }
    }

    return Expr.makeApplication(base, arg);
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
    StringBuilder builder = new StringBuilder();

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

    return builder.toString();
  }
}
