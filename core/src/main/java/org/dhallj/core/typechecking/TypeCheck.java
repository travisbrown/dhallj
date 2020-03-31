package org.dhallj.core.typechecking;

import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import org.dhallj.core.Expr;
import org.dhallj.core.Import;
import org.dhallj.core.Operator;
import org.dhallj.core.Source;
import org.dhallj.core.Thunk;
import org.dhallj.core.Visitor;
import org.dhallj.core.Expr.Constants;
import org.dhallj.core.ast.AsApplication;
import org.dhallj.core.ast.IsIdentifier;
import org.dhallj.core.normalization.BetaNormalize;
import org.dhallj.core.util.FieldUtilities;
import org.dhallj.core.visitor.ConstantVisitor;
import org.dhallj.core.visitor.ExternalVisitor;

public final class TypeCheck implements ExternalVisitor<Expr> {
  private Context context;

  public TypeCheck(Context context) {
    this.context = context;
  }

  public TypeCheck() {
    this(Context.EMPTY);
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
      if (name.equals("Sort")) {

        throw fail("Cannot type-check Sort");
      } else {
        throw fail(String.format("Unknown identifier: %s", name));
      }
    }
  }

  public final Expr onOperatorApplication(Operator operator, Expr lhs, Expr rhs) {
    Expr lhsType = lhs.acceptExternal(this);
    Expr rhsType = rhs.acceptExternal(this);
    switch (operator) {
      case OR:
      case AND:
      case EQUALS:
      case NOT_EQUALS:
        if (isBool(lhsType) && isBool(rhsType)) {
          return Constants.BOOL;
        } else {
          throw fail("not a Bool");
        }
      case PLUS:
      case TIMES:
        if (isNatural(lhsType) && isNatural(rhsType)) {
          return Constants.NATURAL;
        } else {
          throw fail("not a Natural");
        }
      case TEXT_APPEND:
        if (isText(lhsType) && isText(rhsType)) {
          return Constants.TEXT;
        } else {
          throw fail("++ not a Text");
        }
      case LIST_APPEND:
        Entry<Expr, Expr> lhsApplication = lhsType.acceptExternal(AsApplication.instance);
        Entry<Expr, Expr> rhsApplication = rhsType.acceptExternal(AsApplication.instance);

        if (lhsApplication != null && rhsApplication != null) {
          if (isList(lhsApplication.getKey())
              && isList(rhsApplication.getKey())
              && lhsApplication.getValue().equivalent(rhsApplication.getValue())) {

            return lhsType;
          } else {
            throw fail("# not Lists with the same type");
          }

        } else {
          throw fail("# not Lists with the same type");
        }
      case COMBINE:
        Expr combineTypes = Expr.makeOperatorApplication(Operator.COMBINE_TYPES, lhsType, rhsType);

        // Type-check the type-level version, although we don't use the result;
        combineTypes.acceptExternal(this);

        return combineTypes.accept(BetaNormalize.instance);
      case PREFER:
        Iterable<Entry<String, Expr>> lhsTypeRecordType = lhsType.asRecordType();
        Iterable<Entry<String, Expr>> rhsTypeRecordType = rhsType.asRecordType();

        if (lhsTypeRecordType != null && rhsTypeRecordType != null) {
          return Expr.makeRecordType(FieldUtilities.prefer(lhsTypeRecordType, rhsTypeRecordType));
        } else {
          throw fail("prefer requires record literals");
        }
      case COMBINE_TYPES:
        Iterable<Entry<String, Expr>> lhsRecordType =
            lhs.accept(BetaNormalize.instance).asRecordType();
        Iterable<Entry<String, Expr>> rhsRecordType =
            rhs.accept(BetaNormalize.instance).asRecordType();

        if (lhsRecordType != null && rhsRecordType != null) {
          if (isType(rhsType) && !rhsRecordType.iterator().hasNext()) {
            return lhsType;
          } else {
            Universe lhsTypeUniverse = Universe.fromExpr(lhsType);
            Universe rhsTypeUniverse = Universe.fromExpr(rhsType);

            if (lhsTypeUniverse != null && rhsTypeUniverse != null) {
              checkRecursiveTypeMerge(lhsRecordType, rhsRecordType);

              return lhsTypeUniverse.max(rhsTypeUniverse).toExpr();
            } else {
              throw fail("invalid combine types");
            }
          }
        } else {
          throw fail("combine types requires record types");
        }
      case IMPORT_ALT:
        throw fail("cannot type-check import");
      case EQUIVALENT:
        Expr lhsTypeType = lhsType.acceptExternal(this);
        Expr rhsTypeType = rhsType.acceptExternal(this);

        if (lhsTypeType != null
            && rhsTypeType != null
            && isType(lhsTypeType)
            && isType(rhsTypeType)
            && lhsType.equivalent(rhsType)) {
          return Constants.TYPE;
        } else {
          throw fail("=== requires terms");
        }

      case COMPLETE:
        return Expr.Sugar.desugarComplete(lhs, rhs).acceptExternal(this);
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

  public final Expr onTextLiteral(String[] parts, Iterable<Expr> interpolated) {
    for (Expr i : interpolated) {
      if (!isText(i.acceptExternal(this))) {
        throw fail("interpolation not of type Text");
      }
    }

    return Constants.TEXT;
  }

  public final Expr onApplication(Expr base, final Expr arg) {
    Expr baseType = base.acceptExternal(this);
    final Expr argType = arg.acceptExternal(this);

    Expr result =
        baseType.acceptExternal(
            new ConstantVisitor.External<Expr>(null) {

              @Override
              public Expr onIdentifier(String name, long index) {
                if (name.equals("Some")
                    && index == 0
                    && isType(argType.acceptExternal(TypeCheck.this))) {
                  return Expr.makeApplication(Expr.Constants.OPTIONAL, argType);
                }
                throw fail(String.format("can't apply %s", name));
              }

              @Override
              public Expr onPi(String param, Expr input, Expr result) {
                // TODO: Shouldn't have to normalize here when we have real equivalence.
                if (input.equivalent(argType)) {
                  return result
                      .substitute(param, arg.increment(param))
                      .decrement(param)
                      .normalize();
                } else {
                  throw fail("types don't match in function application");
                }
              }
            });

    return result;
  }

  public final Expr onIf(Expr cond, Expr thenValue, Expr elseValue) {
    if (isBool(cond.acceptExternal(this))) {
      Expr thenType = thenValue.acceptExternal(this);
      Expr elseType = elseValue.acceptExternal(this);

      if (thenType.equivalent(elseType)) {
        if (isType(thenType.acceptExternal(this)) && isType(elseType.acceptExternal(this))) {
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

  public final Expr onLambda(String param, Expr input, Expr result) {
    if (Universe.fromExpr(input.acceptExternal(this)) != null) {
      Context unshiftedContext = this.context;
      Expr inputNormalized = input.accept(BetaNormalize.instance);
      this.context = this.context.insert(param, inputNormalized).increment(param);
      Expr resultType = result.acceptExternal(this);
      this.context = unshiftedContext;
      return Expr.makePi(param, inputNormalized, resultType);
    } else {
      throw fail("input type cannot be a term");
    }
  }

  public final Expr onPi(String param, Expr input, Expr result) {
    Expr inputType = input.acceptExternal(this);
    Context unshiftedContext = this.context;
    this.context = this.context.insert(param, input).increment(param);
    Expr resultType = result.acceptExternal(this);
    this.context = unshiftedContext;

    Universe inputTypeUniverse = Universe.fromExpr(inputType);
    Universe resultTypeUniverse = Universe.fromExpr(resultType);

    return FunctionCheck.check(inputTypeUniverse, resultTypeUniverse).toExpr();
  }

  public final Expr onAssert(Expr base) {
    Expr baseType = base.acceptExternal(this);

    if (isType(baseType)) {
      Expr normalized = base.accept(BetaNormalize.instance);
      if (normalized != null && normalized.acceptExternal(CheckEquivalence.instance)) {
        return normalized;
      }
    }
    throw fail("invalid assert");
  }

  public final Expr onFieldAccess(Expr base, String fieldName) {
    Expr baseType = base.acceptExternal(this);
    Iterable<Entry<String, Expr>> fields = baseType.asRecordType();

    if (fields != null) {
      for (Entry<String, Expr> field : fields) {
        if (field.getKey().equals(fieldName)) {
          return field.getValue();
        }
      }
      throw fail(String.format("%s doesn't contain a field %s", base, fieldName));
    } else {
      Expr baseNormalized = base.accept(BetaNormalize.instance);
      Iterable<Entry<String, Expr>> alternatives = baseNormalized.asUnionType();
      if (alternatives == null) {
        throw fail(
            String.format(
                "can't access field (%s) on something that isn't a record or field", fieldName));
      } else {
        for (Entry<String, Expr> alternative : alternatives) {
          if (alternative.getKey().equals(fieldName)) {
            if (alternative.getValue() == null) {
              return baseNormalized;
            } else {
              return Expr.makePi(fieldName, alternative.getValue(), baseNormalized);
            }
          }
        }
        throw fail(String.format("%s doesn't contain an alternative %s", base, fieldName));
      }
    }
  }

  public final Expr onProjection(Expr base, String[] fieldNames) {
    Iterable<Entry<String, Expr>> fields = base.acceptExternal(this).asRecordType();

    if (base == null) {
      throw fail("can't project on a non-record");
    } else {
      Map<String, Expr> fieldMap = new HashMap();

      for (Entry<String, Expr> field : fields) {
        fieldMap.put(field.getKey(), field.getValue());
      }

      List<Entry<String, Expr>> newFields = new ArrayList();
      List<String> missing = null;

      for (String fieldName : fieldNames) {
        Expr value = fieldMap.remove(fieldName);

        if (value == null) {
          if (missing == null) {
            missing = new ArrayList();
          }
          missing.add(fieldName);
        } else {
          newFields.add(new SimpleImmutableEntry(fieldName, value));
        }
      }

      if (missing == null) {
        return Expr.makeRecordType(newFields);
      } else {
        throw fail("missing fields");
      }
    }
  }

  public final Expr onProjectionByType(Expr base, Expr type) {
    Iterable<Entry<String, Expr>> fields = base.acceptExternal(this).asRecordType();

    if (fields == null) {
      throw fail("can't project on a non-record");
    } else {

      Iterable<Entry<String, Expr>> projected = type.accept(BetaNormalize.instance).asRecordType();

      if (projected == null) {
        throw fail("can't project by type with a non-record type");
      } else {

        Map<String, Expr> fieldMap = new HashMap();

        for (Entry<String, Expr> field : fields) {
          fieldMap.put(field.getKey(), field.getValue());
        }

        List<Entry<String, Expr>> newFields = new ArrayList();
        List<String> missing = null;

        for (Entry<String, Expr> projectedEntry : projected) {
          String fieldName = projectedEntry.getKey();
          Expr value = fieldMap.get(fieldName);

          if (value == null) {
            if (missing == null) {
              missing = new ArrayList();
            }
            missing.add(fieldName);
          } else {
            Expr projectedValue = projectedEntry.getValue();
            if (value.equivalent(projectedValue)) {
              newFields.add(new SimpleImmutableEntry(fieldName, value));

            } else {
              if (missing == null) {
                missing = new ArrayList();
              }
              missing.add(fieldName);
            }
          }
        }

        if (missing == null) {
          return Expr.makeRecordType(projected);
        } else {
          throw fail("missing fields");
        }
      }
    }
  }

  public final Expr onRecordLiteral(Iterable<Entry<String, Expr>> fields, int size) {
    if (size == 0) {
      return Constants.EMPTY_RECORD_TYPE;
    } else {
      Map<String, Expr> fieldTypes = new TreeMap();

      for (Entry<String, Expr> field : fields) {
        fieldTypes.put(
            field.getKey(), field.getValue().acceptExternal(this).accept(BetaNormalize.instance));
      }

      return Expr.makeRecordType(fieldTypes.entrySet());
    }
  }

  public final Expr onRecordType(Iterable<Entry<String, Expr>> fields, int size) {
    Universe max = Universe.TYPE;

    for (Entry<String, Expr> field : fields) {
      Universe universe = Universe.fromExpr(field.getValue().acceptExternal(this));

      if (universe != null) {
        max = max.max(universe);
      } else {
        throw fail("The type of a field in a record type is not Type, Kind, or Sort");
      }
    }

    return max.toExpr();
  }

  public final Expr onUnionType(Iterable<Entry<String, Expr>> fields, int size) {
    if (size == 0) {
      return Constants.TYPE;
    } else {
      Set<String> seen = new HashSet();
      Universe firstUniverse = null;
      Entry<String, Expr> first = null;

      for (Entry<String, Expr> field : fields) {
        if (!seen.contains(field.getKey())) {
          seen.add(field.getKey());
          if (field.getValue() != null) {
            Universe universe = Universe.fromExpr(field.getValue().acceptExternal(this));

            if (universe != null) {

              if (firstUniverse == null) {
                firstUniverse = universe;
                first = field;
              } else {
                if (universe != firstUniverse) {
                  throw fail("Alternative annotation mismatch");
                }
              }
            } else {
              throw fail("Alternative must be a type");
            }
          }
        } else {
          throw fail(String.format("Duplicate alternative: %s", field.getKey()));
        }
      }

      if (firstUniverse == null) {
        return Constants.TYPE;
      } else {
        return firstUniverse.toExpr();
      }
    }
  }

  public final Expr onNonEmptyListLiteral(Iterable<Expr> values, int size) {
    Iterator<Expr> it = values.iterator();
    Expr first = it.next().acceptExternal(this);
    Expr firstType = first.acceptExternal(this);

    if (isType(firstType)) {

      while (it.hasNext()) {
        if (!it.next().acceptExternal(this).equivalent(first)) {
          throw fail("heterogenous list");
        }
      }

      return Expr.makeApplication(Expr.Constants.LIST, first);
    } else {
      throw fail("list element is not a term");
    }
  }

  public final Expr onEmptyListLiteral(Expr type) {
    Expr typeType = type.acceptExternal(this);

    Expr typeNormalized = type.accept(BetaNormalize.instance);
    Expr result =
        typeNormalized.acceptExternal(
            new ConstantVisitor.External<Expr>(null) {
              @Override
              public Expr onApplication(Expr base, Expr arg) {
                String baseAsIdentifier = base.asSimpleIdentifier();

                if (baseAsIdentifier == null || !baseAsIdentifier.equals("List")) {
                  throw fail("non-empty list type problem");
                } else {
                  return arg;
                }
              }
            });

    if (result != null && isType(result.acceptExternal(this))) {
      return Expr.makeApplication(Constants.LIST, result);
    } else {
      throw fail("non-empty list type problem");
    }
  }

  public final Expr onLet(String name, Expr type, Expr value, Expr body) {
    Expr valueType = value.acceptExternal(this);

    if (type != null) {
      if (!type.equivalent(valueType)) {
        throw fail("let type doesn't match inferred type of value");
      }
    }

    return body.substitute(name, value.accept(BetaNormalize.instance).increment(name))
        .decrement(name)
        .acceptExternal(this);
  }

  public final Expr onAnnotated(Expr base, Expr type) {
    Expr inferred = base.acceptExternal(this);
    // TODO: fix with equivalence.
    if (inferred
        .accept(BetaNormalize.instance)
        .alphaNormalize()
        .equivalent(type.accept(BetaNormalize.instance).alphaNormalize())) {
      return inferred;
    } else {
      throw fail(
          String.format(
              "invalid type annotation: %s for %s", type.accept(BetaNormalize.instance), inferred));
    }
  }

  public final Expr onToMap(Expr base, Expr type) {
    Iterable<Entry<String, Expr>> baseAsRecord = base.acceptExternal(this).asRecordType();

    if (baseAsRecord != null) {
      Expr firstType = null;
      Entry<String, Expr> first = null;

      for (Entry<String, Expr> entry : baseAsRecord) {
        if (firstType == null) {
          firstType = entry.getValue();
          if (!isType(firstType.acceptExternal(this))) {
            throw fail("toMap requires a record of terms");
          }
          first = entry;
        } else {
          if (!entry.getValue().equivalent(firstType)) {
            throw fail("toMap requires a homogenous record");
          }
        }
      }

      if (firstType != null) {
        List<Entry<String, Expr>> resultTypeFields = new ArrayList(2);
        resultTypeFields.add(
            new SimpleImmutableEntry(Constants.MAP_KEY_FIELD_NAME, Constants.TEXT));
        resultTypeFields.add(new SimpleImmutableEntry(Constants.MAP_VALUE_FIELD_NAME, firstType));

        Expr resultType =
            Expr.makeApplication(Constants.LIST, Expr.makeRecordType(resultTypeFields));

        if (type == null
            || type.accept(BetaNormalize.instance)
                .alphaNormalize()
                .equivalent(resultType.alphaNormalize())) {
          return resultType;
        } else {
          throw fail("toMap type doesn't match inferred");
        }
      } else {
        if (type != null) {
          Expr typeType = type.acceptExternal(this);
          if (isType(typeType)) {

            Expr typeNormalized = type.accept(BetaNormalize.instance);
            Entry<Expr, Expr> typeApplication =
                typeNormalized.acceptExternal(AsApplication.instance);

            if (typeApplication != null && isList(typeApplication.getKey())) {
              Iterable<Entry<String, Expr>> typeFields = typeApplication.getValue().asRecordType();

              if (typeFields != null) {
                boolean sawKey = false;
                boolean sawValue = false;

                for (Entry<String, Expr> typeField : typeFields) {
                  if (sawKey && sawValue) {
                    throw fail("extraneous field in toMap type annotation");
                  } else {
                    if (typeField.getKey().equals(Constants.MAP_KEY_FIELD_NAME)) {
                      if (isText(typeField.getValue())) {
                        sawKey = true;
                      } else {
                        throw fail("toMap with an empty argument has an invalid type annotation");
                      }

                    } else if (typeField.getKey().equals(Constants.MAP_VALUE_FIELD_NAME)) {
                      sawValue = true;
                    } else {
                      throw fail("toMap with an empty argument has an invalid type annotation");
                    }
                  }
                }

                if (sawKey && sawValue) {
                  return typeNormalized;
                } else {
                  throw fail("toMap with an empty argument has an invalid type annotation");
                }
              } else {
                throw fail("toMap with an empty argument has an invalid type annotation");
              }

            } else {
              throw fail("toMap with an empty argument has an invalid type annotation");
            }
          } else {
            throw fail("toMap type annotation isn't a type");
          }
        } else {
          throw fail("toMap with an empty argument requires a type annotation");
        }
      }
    } else {
      throw fail("toMap requires a record");
    }
  }

  public final Expr onMerge(Expr left, Expr right, Expr type) {
    Iterable<Entry<String, Expr>> leftRecord = left.acceptExternal(this).asRecordType();

    if (leftRecord != null) {
      Iterable<Entry<String, Expr>> rightUnion = right.acceptExternal(this).asUnionType();

      if (rightUnion != null) {
        Expr mergeType = checkMerge(leftRecord, rightUnion);

        if (mergeType != null) {
          if (type == null || mergeType.equivalent(type)) {
            return mergeType;

          } else {
            throw fail("inferred type for merge doesn't match type annotations");
          }
        } else if (type != null) {
          return type;
        } else {

          throw fail("merge with empty arguments must have a return type");
        }
      } else {
        Expr rightType = right.acceptExternal(this);
        Entry<Expr, Expr> rightTypeApplied = rightType.acceptExternal(AsApplication.instance);
        if (isOptional(rightTypeApplied.getKey())) {
          Expr mergeType =
              checkMerge(leftRecord, makeOptionalConstructors(rightTypeApplied.getValue()));

          if (mergeType != null) {
            if (type == null || mergeType.equivalent(type)) {
              return mergeType;

            } else {
              throw fail("inferred type for merge doesn't match type annotations");
            }
          } else if (type != null) {
            return type;
          } else {

            throw fail("merge with empty arguments must have a return type");
          }
        } else {
          throw fail("merge's second argument must be a union or an optional");
        }
      }
    } else {
      throw fail("merge's first argument must be a record");
    }
  }

  public final Expr onNote(Expr base, Source source) {
    return base.acceptExternal(this);
  }

  public final Expr onLocalImport(Path path, Import.Mode mode, byte[] hash) {
    throw fail("cannot type-check import");
  }

  public final Expr onRemoteImport(URI url, Import.Mode mode, byte[] hash) {
    throw fail("cannot type-check import");
  }

  public final Expr onEnvImport(String value, Import.Mode mode, byte[] hash) {
    throw fail("cannot type-check import");
  }

  public final Expr onMissingImport(Import.Mode mode, byte[] hash) {
    throw fail("cannot type-check import");
  }

  private static final Visitor<Expr, Boolean> isBool = new IsIdentifier("Bool");
  private static final Visitor<Expr, Boolean> isText = new IsIdentifier("Text");
  private static final Visitor<Expr, Boolean> isList = new IsIdentifier("List");
  private static final Visitor<Expr, Boolean> isNatural = new IsIdentifier("Natural");
  private static final Visitor<Expr, Boolean> isOptional = new IsIdentifier("Optional");
  private static final Visitor<Expr, Boolean> isType = new IsIdentifier("Type");

  static boolean isBool(Expr expr) {
    return expr.acceptExternal(isBool);
  }

  static boolean isText(Expr expr) {
    return expr.acceptExternal(isText);
  }

  static boolean isList(Expr expr) {
    return expr.acceptExternal(isList);
  }

  static boolean isNatural(Expr expr) {
    return expr.acceptExternal(isNatural);
  }

  static boolean isOptional(Expr expr) {
    return expr.acceptExternal(isOptional);
  }

  static boolean isType(Expr expr) {
    return expr.acceptExternal(isType);
  }

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
    builtInTypes.put("Natural/fold", Expr.makePi(Constants.NATURAL, naturalType));
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
    builtInTypes.put("List/reverse", Expr.makePi("a", Constants.TYPE, Expr.makePi(listA, listA)));

    builtInTypes.put(
        "Optional/build", Expr.makePi("a", Constants.TYPE, Expr.makePi(optionalType, optionalA)));
    builtInTypes.put(
        "Optional/fold", Expr.makePi("a", Constants.TYPE, Expr.makePi(optionalA, optionalType)));
    builtInTypes.put("Some", Constants.SOME);
  }

  private final void checkRecursiveTypeMerge(
      Iterable<Entry<String, Expr>> lhs, Iterable<Entry<String, Expr>> rhs) {

    Map<String, Expr> lhsMap = new HashMap();

    for (Entry<String, Expr> entry : lhs) {
      lhsMap.put(entry.getKey(), entry.getValue());
    }

    for (Entry<String, Expr> entry : rhs) {
      Expr rhsValue = entry.getValue();
      Expr lhsValue = lhsMap.get(entry.getKey());

      if (lhsValue != null) {
        Expr.makeOperatorApplication(Operator.COMBINE_TYPES, lhsValue, rhsValue)
            .acceptExternal(this);
      }
    }
  }

  private Iterable<Entry<String, Expr>> makeOptionalConstructors(Expr type) {
    List<Entry<String, Expr>> constructors = new ArrayList();
    constructors.add(new SimpleImmutableEntry("None", null));
    constructors.add(new SimpleImmutableEntry("Some", type));
    return constructors;
  }

  private Expr checkMerge(
      Iterable<Entry<String, Expr>> handlerTypes, Iterable<Entry<String, Expr>> constructors) {
    Map<String, Expr> handlerTypeMap = new HashMap();

    for (Entry<String, Expr> handlerTypeField : handlerTypes) {
      handlerTypeMap.put(handlerTypeField.getKey(), handlerTypeField.getValue());
    }

    Expr resultType = null;

    for (Entry<String, Expr> constructor : constructors) {
      Expr handlerType = handlerTypeMap.remove(constructor.getKey());
      if (handlerType != null) {
        final Expr constructorType = constructor.getValue();

        if (constructorType == null) {
          // We have an empty constructor.
          if (resultType == null) {
            // This is the first constructor.
            resultType = handlerType;
          } else {
            // We check that the handler type is the same as previous result types.
            if (!handlerType.equivalent(resultType)) {
              throw fail("handler types aren't consistent");
            }
          }
        } else {
          // We have a constructor with a type, so we have to make sure the handler is a function.
          Expr handlerResultType =
              handlerType.acceptExternal(
                  new ConstantVisitor.External<Expr>(null) {
                    public Expr onPi(String param, Expr input, Expr result) {
                      if (input.equivalent(constructorType)) {
                        return result.decrement(param);
                      } else {
                        throw fail(
                            String.format(
                                "handler result type %s doesn't match %s", input, constructorType));
                      }
                    }
                  });

          if (handlerResultType == null) {
            throw fail("handler isn't a function");
          } else if (resultType == null) {
            resultType = handlerResultType;
          } else {
            // We check that the handler result type is the same as previous result types.
            // TODO: fix when we have real equivalence.
            if (!handlerResultType.normalize().same(resultType.normalize())) {
              throw fail("handler types aren't consistent");
            }
          }
        }
      } else {
        throw fail(String.format("missing handler for merge: %s", constructor.getKey()));
      }
    }

    if (handlerTypeMap.isEmpty()) {
      return resultType;
    } else {
      throw fail("extra handler for merge");
    }
  }
}
