package org.dhallj.core.typechecking;

import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.dhallj.core.Expr;
import org.dhallj.core.Import;
import org.dhallj.core.Operator;
import org.dhallj.core.Source;
import org.dhallj.core.Thunk;
import org.dhallj.core.Visitor;
import org.dhallj.core.Expr.Constants;
import org.dhallj.core.ast.IsIdentifier;
import org.dhallj.core.visitor.ConstantVisitor;

public final class TypeCheck implements Visitor.Internal<Expr> {
  private Context context;

  public TypeCheck(Context context) {
    this.context = context;
  }

  public TypeCheck() {
    this(Context.EMPTY);
  }

  private static boolean isBool(Expr expr) {
    return expr.acceptExternal(isBool);
  }

  private static boolean isText(Expr expr) {
    return expr.acceptExternal(isText);
  }

  private static boolean isNatural(Expr expr) {
    return expr.acceptExternal(isNatural);
  }

  private static boolean isType(Expr expr) {
    return expr.acceptExternal(isType);
  }

  private static RuntimeException fail(String message) {
    return new RuntimeException(message);
  }

  public final Expr onIdentifier(String name, long index) {
    if (index == 0) {
      Expr builtIn = this.builtInTypes.get(name);
      if (builtIn != null) {
        return builtIn;
      }
    }

    Expr fromContext = this.context.lookup(name, index);

    if (fromContext != null) {
      return fromContext;
    } else {
      throw fail(String.format("Unknown identifier: %s", name));
    }
  }

  public final Expr onOperatorApplication(Operator operator, Thunk<Expr> lhs, Thunk<Expr> rhs) {
    Expr l = lhs.apply();
    Expr r = rhs.apply();
    switch (operator) {
      case OR:
      case AND:
      case EQUALS:
      case NOT_EQUALS:
        if (isBool(l) && isBool(r)) {
          return Constants.BOOL;
        } else {
          throw fail("not a Bool");
        }
      case TEXT_APPEND:
        if (isText(l) && isText(r)) {
          return Constants.TEXT;
        } else {
          throw fail("++ not a Text");
        }
      case PLUS:
      case TIMES:
        if (isNatural(l) && isNatural(r)) {
          return Constants.TEXT;
        } else {
          throw fail("not a Natural");
        }
      case COMBINE:

      default:
        return null;
    }
  }

  public final Expr onDoubleLiteral(double value) {
    return Constants.DOUBLE;
  }

  public final Expr onNaturalLiteral(BigInteger value) {
    return Constants.NATURAL;
  }

  public final Expr onIntegerLiteral(BigInteger value) {
    return Constants.INTEGER;
  }

  public final Expr onTextLiteral(String[] parts, Iterable<Thunk<Expr>> interpolated) {
    return Constants.TEXT;
  }

  public final Expr onApplication(Thunk<Expr> base, Thunk<Expr> arg) {
    Expr baseEval = base.apply();
    final Expr argEval = arg.apply();

    Expr result =
        baseEval.acceptExternal(
            new ConstantVisitor.External<Expr>(null) {

              @Override
              public Expr onIdentifier(String name, long index) {
                if (name.equals("Some") && index == 0 && isType(argEval.accept(TypeCheck.this))) {
                  return Expr.makeApplication(Expr.Constants.OPTIONAL, argEval);
                }
                return null;
              }

              @Override
              public Expr onPi(String param, Expr input, Expr result) {
                if (input.equivalent(argEval)) {
                  return result
                      .substitute(param, argEval.increment(param))
                      .decrement(param)
                      .normalize();
                } else {
                  return null;
                }
              }
            });

    return result;
  }

  public final Expr onIf(Thunk<Expr> cond, Thunk<Expr> thenValue, Thunk<Expr> elseValue) {
    if (isBool(cond.apply())) {
      Expr thenType = thenValue.apply();
      Expr elseType = elseValue.apply();

      if (thenType.equivalent(elseType)) {
        if (isType(thenType.accept(this)) && isType(elseType.accept(this))) {
          return thenType;
        } else {
          throw fail("IF branch not a term");
        }
      } else {
        throw fail("IF branches not same type");
      }
    } else {
      throw fail("IF condition not a Bool");
    }
  }

  public final Expr onLambda(String param, Thunk<Expr> input, Thunk<Expr> result) {
    return null;
  }

  public final Expr onPi(String param, Thunk<Expr> input, Thunk<Expr> result) {
    return null;
  }

  public final Expr onAssert(Thunk<Expr> base) {
    return null;
  }

  public final Expr onFieldAccess(Thunk<Expr> base, String fieldName) {
    return null;
  }

  public final Expr onProjection(Thunk<Expr> base, String[] fieldNames) {
    return null;
  }

  public final Expr onProjectionByType(Thunk<Expr> base, Thunk<Expr> tpe) {
    return null;
  }

  public final Expr onRecordLiteral(Iterable<Entry<String, Thunk<Expr>>> fields, int size) {
    return null;
  }

  public final Expr onRecordType(Iterable<Entry<String, Thunk<Expr>>> fields, int size) {
    return null;
  }

  public final Expr onUnionType(Iterable<Entry<String, Thunk<Expr>>> fields, int size) {
    return null;
  }

  public final Expr onNonEmptyListLiteral(Iterable<Thunk<Expr>> values, int size) {
    Iterator<Thunk<Expr>> it = values.iterator();
    Expr first = it.next().apply();

    while (it.hasNext()) {
      if (!it.next().apply().equivalent(first)) {
        throw fail("heterogenous list");
      }
    }

    return Expr.makeApplication(Expr.Constants.LIST, first);
  }

  public final Expr onEmptyListLiteral(Thunk<Expr> tpe) {
    Expr tpeEval = tpe.apply();
    boolean result =
        tpeEval.acceptExternal(
            new ConstantVisitor.External<Boolean>(false) {
              @Override
              public Boolean onApplication(Expr base, Expr arg) {
                String baseAsIdentifier = base.asSimpleIdentifier();

                if (baseAsIdentifier == null || !baseAsIdentifier.equals("List")) {
                  throw fail("non-empty list type problem");
                } else {
                  return true;
                }
              }
            });

    if (result) {
      return tpeEval;
    } else {
      throw fail("non-empty list type problem");
    }
  }

  public final Expr onLet(String name, Thunk<Expr> type, Thunk<Expr> value, Thunk<Expr> body) {
    return null;
  }

  public final Expr onAnnotated(Thunk<Expr> base, Thunk<Expr> type) {
    return null;
  }

  public final Expr onToMap(Thunk<Expr> base, Thunk<Expr> type) {
    return null;
  }

  public final Expr onMerge(Thunk<Expr> left, Thunk<Expr> right, Thunk<Expr> type) {
    return null;
  }

  public final Expr onNote(Thunk<Expr> base, Source source) {
    return base.apply();
  }

  public final Expr onLocalImport(Path path, Import.Mode mode) {
    throw fail("cannot type-check import");
  }

  public final Expr onRemoteImport(URI url, Import.Mode mode) {
    throw fail("cannot type-check import");
  }

  public final Expr onEnvImport(String value, Import.Mode mode) {
    throw fail("cannot type-check import");
  }

  public final Expr onMissingImport(Import.Mode mode) {
    throw fail("cannot type-check import");
  }

  private static final Visitor<Expr, Boolean> isBool = new IsIdentifier("Bool");
  private static final Visitor<Expr, Boolean> isText = new IsIdentifier("Text");
  private static final Visitor<Expr, Boolean> isNatural = new IsIdentifier("Natural");
  private static final Visitor<Expr, Boolean> isType = new IsIdentifier("Type");

  private static final int BUILT_IN_SIZE = 34;
  private static final Map<String, Expr> builtInTypes = new HashMap(BUILT_IN_SIZE);

  static {
    Expr typeToType = Expr.makePi(Constants.TYPE, Constants.TYPE);
    Expr naturalToBool = Expr.makePi(Constants.NATURAL, Constants.BOOL);
    Expr naturalToNatural = Expr.makePi(Constants.NATURAL, Constants.NATURAL);
    Expr _natural = Expr.makeIdentifier("natural");
    Expr naturalType =
        Expr.makePi(
            "natural",
            Constants.TYPE,
            Expr.makePi(
                "succ", Expr.makePi(_natural, _natural), Expr.makePi("zero", _natural, _natural)));
    Expr _a = Expr.makeIdentifier("a");
    Expr listA = Expr.makeApplication(Constants.LIST, _a);
    Expr optionalA = Expr.makeApplication(Constants.OPTIONAL, _a);
    Expr listAToOptionalA = Expr.makePi("a", Expr.Constants.TYPE, Expr.makePi(listA, optionalA));

    Expr _list = Expr.makeIdentifier("list");
    Expr listType =
        Expr.makePi(
            "list",
            Constants.TYPE,
            Expr.makePi(
                "cons",
                Expr.makePi(_a, Expr.makePi(_list, _list)),
                Expr.makePi("nil", _list, _list)));

    Expr _optional = Expr.makeIdentifier("optional");
    Expr optionalType =
        Expr.makePi(
            "optional",
            Constants.TYPE,
            Expr.makePi(
                "just", Expr.makePi(_a, _optional), Expr.makePi("nothing", _optional, _optional)));

    builtInTypes.put("Kind", Constants.SORT);
    builtInTypes.put("Type", Constants.KIND);
    builtInTypes.put("Bool", Constants.TYPE);
    builtInTypes.put("True", Constants.BOOL);
    builtInTypes.put("False", Constants.BOOL);
    builtInTypes.put("Natural", Constants.TYPE);
    builtInTypes.put("Integer", Constants.TYPE);
    builtInTypes.put("Text", Constants.TYPE);
    builtInTypes.put("Double", Constants.TYPE);
    builtInTypes.put("List", typeToType);
    builtInTypes.put("Optional", typeToType);
    builtInTypes.put(
        "None",
        Expr.makePi(
            "A",
            Constants.TYPE,
            Expr.makeApplication(Constants.OPTIONAL, Expr.makeIdentifier("A"))));
    builtInTypes.put("Text/show", Expr.makePi(Constants.TEXT, Constants.TEXT));

    builtInTypes.put("Natural/build", Expr.makePi(naturalType, Constants.NATURAL));
    builtInTypes.put("Natural/build", Expr.makePi(Constants.NATURAL, naturalType));
    builtInTypes.put("Natural/isZero", naturalToBool);
    builtInTypes.put("Natural/even", naturalToBool);
    builtInTypes.put("Natural/odd", naturalToBool);
    builtInTypes.put("Natural/toInteger", Expr.makePi(Constants.NATURAL, Constants.INTEGER));
    builtInTypes.put("Natural/show", Expr.makePi(Constants.NATURAL, Constants.TEXT));
    builtInTypes.put("Natural/subtract", Expr.makePi(Constants.NATURAL, naturalToNatural));

    builtInTypes.put("Integer/show", Expr.makePi(Constants.INTEGER, Constants.TEXT));
    builtInTypes.put("Integer/toDouble", Expr.makePi(Constants.INTEGER, Constants.DOUBLE));
    builtInTypes.put("Integer/negate", Expr.makePi(Constants.INTEGER, Constants.INTEGER));
    builtInTypes.put("Integer/clamp", Expr.makePi(Constants.INTEGER, Constants.NATURAL));

    builtInTypes.put("Double/show", Expr.makePi(Constants.DOUBLE, Constants.TEXT));

    builtInTypes.put("List/build", Expr.makePi("a", Constants.TYPE, Expr.makePi(listType, listA)));
    builtInTypes.put("List/fold", Expr.makePi("a", Constants.TYPE, Expr.makePi(listA, listType)));
    builtInTypes.put(
        "List/length",
        Expr.makePi("a", Expr.Constants.TYPE, Expr.makePi(listA, Constants.NATURAL)));
    builtInTypes.put("List/head", listAToOptionalA);
    builtInTypes.put("List/last", listAToOptionalA);

    List<Entry<String, Expr>> indexedRecord = new ArrayList(2);
    indexedRecord.add(new SimpleImmutableEntry("index", Constants.NATURAL));
    indexedRecord.add(new SimpleImmutableEntry("value", _a));

    builtInTypes.put(
        "List/indexed",
        Expr.makePi(
            "a",
            Constants.TYPE,
            Expr.makePi(
                listA, Expr.makeApplication(Constants.LIST, Expr.makeRecordType(indexedRecord)))));
    builtInTypes.put("List/reverse", Expr.makePi("a", listA, listA));

    builtInTypes.put(
        "Optional/build", Expr.makePi("a", Constants.TYPE, Expr.makePi(optionalType, optionalA)));
    builtInTypes.put(
        "Optional/fold", Expr.makePi("a", Constants.TYPE, Expr.makePi(optionalA, optionalType)));
    builtInTypes.put("Some", Constants.SOME);
  }
}
