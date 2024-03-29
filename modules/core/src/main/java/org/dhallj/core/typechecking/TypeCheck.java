package org.dhallj.core.typechecking;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import org.dhallj.core.Expr;
import org.dhallj.core.ExternalVisitor;
import org.dhallj.core.Operator;
import org.dhallj.core.Source;
import org.dhallj.core.Expr.Constants;
import org.dhallj.core.normalization.BetaNormalize;

public final class TypeCheck implements ExternalVisitor<Expr> {
  private Context context;

  public TypeCheck(Context context) {
    this.context = context;
  }

  public TypeCheck() {
    this(Context.EMPTY);
  }

  @Override
  public final Expr onNote(Expr base, Source source) {
    return base.accept(this);
  }

  @Override
  public final Expr onNatural(BigInteger value) {
    return Constants.NATURAL;
  }

  @Override
  public final Expr onInteger(BigInteger value) {
    return Constants.INTEGER;
  }

  @Override
  public final Expr onDouble(double value) {
    return Constants.DOUBLE;
  }

  @Override
  public final Expr onDate(int year, int month, int day) {
    return Constants.DATE;
  }

  @Override
  public final Expr onTime(int hour, int minute, int second, BigDecimal fractional) {
    return Constants.TIME;
  }

  @Override
  public final Expr onTimeZone(int minutes) {
    return Constants.TIME_ZONE;
  }

  @Override
  public final Expr onBuiltIn(String name) {
    if (name.equals("Sort")) {
      throw TypeCheckFailure.makeSortError();
    } else {
      Expr type = BuiltInTypes.getType(name);
      if (type != null) {
        return type;
      } else {
        throw TypeCheckFailure.makeUnboundVariableError(name);
      }
    }
  }

  @Override
  public final Expr onIdentifier(String name, long index) {
    Expr fromContext = this.context.lookup(name, index);

    if (fromContext != null) {
      return fromContext;
    } else {
      throw TypeCheckFailure.makeUnboundVariableError(name);
    }
  }

  @Override
  public final Expr onLambda(String param, Expr input, Expr result) {
    Expr inputType = input.accept(this);
    if (Universe.fromExpr(inputType) != null) {
      Context unshiftedContext = this.context;
      Expr inputNormalized = input.accept(BetaNormalize.instance);
      this.context = this.context.insert(param, inputNormalized).increment(param);
      Expr resultType = result.accept(this);
      this.context = unshiftedContext;
      return Expr.makePi(param, inputNormalized, resultType);
    } else {
      throw TypeCheckFailure.makeLambdaInputError(inputType);
    }
  }

  @Override
  public final Expr onPi(String param, Expr input, Expr result) {
    Expr inputType = input.accept(this);
    Context unshiftedContext = this.context;
    this.context = this.context.insert(param, input).increment(param);
    Expr resultType = result.accept(this);
    this.context = unshiftedContext;

    Universe inputTypeUniverse = Universe.fromExpr(inputType);

    if (inputTypeUniverse == null) {
      throw TypeCheckFailure.makePiInputError(inputType);
    }

    Universe resultTypeUniverse = Universe.fromExpr(resultType);

    if (resultTypeUniverse == null) {
      throw TypeCheckFailure.makePiOutputError(resultType);
    }

    return Universe.functionCheck(inputTypeUniverse, resultTypeUniverse).toExpr();
  }

  @Override
  public final Expr onLet(String name, Expr type, Expr value, Expr body) {
    Expr valueType = value.accept(this);

    if (type != null) {
      // We must confirm that the annotation is well-typed.
      type.accept(this);

      if (!type.equivalent(valueType)) {
        throw TypeCheckFailure.makeAnnotationError(type, valueType);
      }
    }

    return body.substitute(name, value.accept(BetaNormalize.instance)).accept(this);
  }

  @Override
  public final Expr onText(String[] parts, Iterable<Expr> interpolated) {
    for (Expr expr : interpolated) {
      Expr exprType = expr.accept(this);
      if (!isText(exprType)) {
        throw TypeCheckFailure.makeInterpolationError(expr, exprType);
      }
    }

    return Constants.TEXT;
  }

  @Override
  public final Expr onNonEmptyList(Iterable<Expr> values, int size) {
    Iterator<Expr> it = values.iterator();
    Expr firstType = it.next().accept(this);

    if (isType(firstType.accept(this))) {
      while (it.hasNext()) {
        Expr elementType = it.next().accept(this);
        if (!elementType.equivalent(firstType)) {
          throw TypeCheckFailure.makeListTypeMismatchError(firstType, elementType);
        }
      }

      return Expr.makeApplication(Expr.Constants.LIST, firstType);
    } else {
      throw TypeCheckFailure.makeListTypeError(firstType);
    }
  }

  @Override
  public final Expr onEmptyList(Expr type) {
    // We verify that the type is well-typed.
    type.accept(this);

    Expr typeNormalized = type.accept(BetaNormalize.instance);
    Expr elementType = Expr.Util.getListArg(typeNormalized);

    if (elementType != null && isType(elementType.accept(this))) {
      return Expr.makeApplication(Constants.LIST, elementType);
    } else {
      throw TypeCheckFailure.makeListTypeError(elementType);
    }
  }

  @Override
  public final Expr onRecord(Iterable<Entry<String, Expr>> fields, int size) {
    if (size == 0) {
      return Constants.EMPTY_RECORD_TYPE;
    } else {
      Map<String, Expr> fieldTypes = new TreeMap<>();

      for (Entry<String, Expr> field : fields) {
        fieldTypes.put(
            field.getKey(), field.getValue().accept(this).accept(BetaNormalize.instance));
      }

      Expr recordType = Expr.makeRecordType(fieldTypes.entrySet());

      // The inferred type must also be well-typed.
      recordType.accept(this);
      return recordType;
    }
  }

  @Override
  public final Expr onRecordType(Iterable<Entry<String, Expr>> fields, int size) {
    // Need to check for duplicates here; see: https://github.com/travisbrown/dhallj/issues/6
    Set<String> fieldNamesSeen = new HashSet<>(size);

    Universe max = Universe.TYPE;

    for (Entry<String, Expr> field : fields) {
      String fieldName = field.getKey();

      if (!fieldNamesSeen.add(fieldName)) {
        throw TypeCheckFailure.makeFieldDuplicateError(fieldName);
      }

      Expr type = field.getValue().accept(this);
      Universe universe = Universe.fromExpr(type);

      if (universe != null) {
        max = max.max(universe);
      } else {
        throw TypeCheckFailure.makeFieldTypeError(fieldName, type);
      }
    }

    return max.toExpr();
  }

  @Override
  public final Expr onUnionType(Iterable<Entry<String, Expr>> fields, int size) {
    Set<String> fieldNamesSeen = new HashSet<>(size);

    Universe max = Universe.TYPE;

    for (Entry<String, Expr> field : fields) {
      String fieldName = field.getKey();

      if (!fieldNamesSeen.add(fieldName)) {
        throw TypeCheckFailure.makeAlternativeDuplicateError(fieldName);
      }

      Expr alternativeType = field.getValue();

      if (alternativeType != null) {
        Expr type = alternativeType.accept(this);
        Universe universe = Universe.fromExpr(type);

        if (universe != null) {
          max = max.max(universe);
        } else {
          throw TypeCheckFailure.makeAlternativeTypeError(fieldName, type);
        }
      }
    }

    return max.toExpr();
  }

  @Override
  public final Expr onFieldAccess(Expr base, String fieldName) {
    Expr baseType = base.accept(this);
    List<Entry<String, Expr>> fields = Expr.Util.asRecordType(baseType);

    if (fields != null) {
      for (Entry<String, Expr> field : fields) {
        if (field.getKey().equals(fieldName)) {
          return field.getValue();
        }
      }
      throw TypeCheckFailure.makeFieldAccessRecordMissingError(fieldName);
    } else {
      Expr baseNormalized = base.accept(BetaNormalize.instance);
      List<Entry<String, Expr>> alternatives = Expr.Util.asUnionType(baseNormalized);
      if (alternatives != null) {
        for (Entry<String, Expr> alternative : alternatives) {
          if (alternative.getKey().equals(fieldName)) {
            if (alternative.getValue() == null) {
              return baseNormalized;
            } else {
              return Expr.makePi(fieldName, alternative.getValue(), baseNormalized);
            }
          }
        }
        throw TypeCheckFailure.makeFieldAccessUnionMissingError(fieldName);
      } else {
        throw TypeCheckFailure.makeFieldAccessError();
      }
    }
  }

  @Override
  public final Expr onProjection(Expr base, String[] fieldNames) {
    List<Entry<String, Expr>> fields = Expr.Util.asRecordType(base.accept(this));

    if (fields != null) {
      Map<String, Expr> fieldMap = new HashMap<>();

      for (Entry<String, Expr> field : fields) {
        fieldMap.put(field.getKey(), field.getValue());
      }

      List<Entry<String, Expr>> newFields = new ArrayList();
      List<String> missing = null;

      for (String fieldName : fieldNames) {
        Expr value = fieldMap.remove(fieldName);

        if (value == null) {
          if (missing == null) {
            missing = new ArrayList<>();
          }
          missing.add(fieldName);
        } else {
          newFields.add(new SimpleImmutableEntry<>(fieldName, value));
        }
      }

      if (missing == null) {
        return Expr.makeRecordType(newFields);
      } else {
        throw TypeCheckFailure.makeFieldAccessRecordMissingError(missing.get(0));
      }
    } else {
      throw TypeCheckFailure.makeProjectionError();
    }
  }

  @Override
  public final Expr onProjectionByType(Expr base, Expr type) {
    List<Entry<String, Expr>> fields = Expr.Util.asRecordType(base.accept(this));

    if (fields == null) {
      throw TypeCheckFailure.makeProjectionError();
    } else {
      List<Entry<String, Expr>> projected =
          Expr.Util.asRecordType(type.accept(BetaNormalize.instance));

      if (projected == null) {
        throw TypeCheckFailure.makeProjectionError();
      } else {
        Map<String, Expr> fieldMap = new HashMap<>();

        for (Entry<String, Expr> field : fields) {
          fieldMap.put(field.getKey(), field.getValue());
        }

        for (Entry<String, Expr> projectedEntry : projected) {
          String fieldName = projectedEntry.getKey();
          Expr value = fieldMap.get(fieldName);
          Expr projectedValue = projectedEntry.getValue();

          if (value == null || !value.equivalent(projectedValue)) {
            throw TypeCheckFailure.makeFieldAccessRecordMissingError(fieldName);
          }
        }

        return Expr.makeRecordType(projected);
      }
    }
  }

  @Override
  public final Expr onApplication(Expr base, Expr arg) {
    Expr baseType = base.accept(this);
    Expr argType = arg.accept(this);

    Expr result = baseType.accept(new TypeCheckApplication(arg, argType, this));

    if (result != null) {
      return result;
    } else {
      throw TypeCheckFailure.makeApplicationError(base, arg);
    }
  }

  @Override
  public final Expr onOperatorApplication(Operator operator, Expr lhs, Expr rhs) {
    Expr lhsType = lhs.accept(this);
    Expr rhsType = rhs.accept(this);
    switch (operator) {
      case OR:
      case AND:
      case EQUALS:
      case NOT_EQUALS:
        if (isBool(lhsType) && isBool(rhsType)) {
          return Constants.BOOL;
        } else {
          throw TypeCheckFailure.makeOperatorError(operator);
        }
      case PLUS:
      case TIMES:
        if (isNatural(lhsType) && isNatural(rhsType)) {
          return Constants.NATURAL;
        } else {
          throw TypeCheckFailure.makeOperatorError(operator);
        }
      case TEXT_APPEND:
        if (isText(lhsType) && isText(rhsType)) {
          return Constants.TEXT;
        } else {
          throw TypeCheckFailure.makeOperatorError(operator);
        }
      case LIST_APPEND:
        Expr lhsListElementType = Expr.Util.getListArg(lhsType);
        Expr rhsListElementType = Expr.Util.getListArg(rhsType);

        if (lhsListElementType != null && rhsListElementType != null) {
          if (lhsListElementType.equivalent(rhsListElementType)) {
            return lhsType;
          } else {
            throw TypeCheckFailure.makeListAppendError(lhsListElementType, rhsListElementType);
          }

        } else {
          throw TypeCheckFailure.makeOperatorError(operator);
        }
      case COMBINE:
        Expr combineTypes = Expr.makeOperatorApplication(Operator.COMBINE_TYPES, lhsType, rhsType);

        // Type-check the type-level version, although we don't use the result;
        try {
          combineTypes.accept(this);
        } catch (TypeCheckFailure e) {
          throw TypeCheckFailure.makeOperatorError(operator);
        }

        return combineTypes.accept(BetaNormalize.instance);
      case PREFER:
        List<Entry<String, Expr>> lhsTypeRecordType = Expr.Util.asRecordType(lhsType);
        List<Entry<String, Expr>> rhsTypeRecordType = Expr.Util.asRecordType(rhsType);

        if (lhsTypeRecordType != null && rhsTypeRecordType != null) {
          return Expr.makeRecordType(prefer(lhsTypeRecordType, rhsTypeRecordType));
        } else {
          throw TypeCheckFailure.makeOperatorError(operator);
        }
      case COMBINE_TYPES:
        List<Entry<String, Expr>> lhsRecordType =
            Expr.Util.asRecordType(lhs.accept(BetaNormalize.instance));
        List<Entry<String, Expr>> rhsRecordType =
            Expr.Util.asRecordType(rhs.accept(BetaNormalize.instance));

        if (lhsRecordType != null && rhsRecordType != null) {
          if (isType(rhsType) && !rhsRecordType.iterator().hasNext()) {
            return lhsType;
          } else {
            Universe lhsTypeUniverse = Universe.fromExpr(lhsType);
            Universe rhsTypeUniverse = Universe.fromExpr(rhsType);

            if (lhsTypeUniverse != null && rhsTypeUniverse != null) {
              // TODO: report collisions correctly.
              checkRecursiveTypeMerge(lhsRecordType, rhsRecordType);

              return lhsTypeUniverse.max(rhsTypeUniverse).toExpr();
            } else {
              throw TypeCheckFailure.makeOperatorError(operator);
            }
          }
        } else {
          throw TypeCheckFailure.makeOperatorError(operator);
        }
      case IMPORT_ALT:
        // TODO: Confirm that this is correct.
        return lhsType.accept(this);
      case EQUIVALENT:
        Expr lhsTypeType = lhsType.accept(this);
        Expr rhsTypeType = rhsType.accept(this);

        if (lhsTypeType != null
            && rhsTypeType != null
            && isType(lhsTypeType)
            && isType(rhsTypeType)) {
          if (lhsType.equivalent(rhsType)) {
            return Constants.TYPE;
          } else {
            throw TypeCheckFailure.makeEquivalenceError(lhsType, rhsType);
          }
        } else {
          throw TypeCheckFailure.makeOperatorError(operator);
        }

      case COMPLETE:
        return Expr.Util.desugarComplete(lhs, rhs).accept(this);
      default:
        return null;
    }
  }

  @Override
  public final Expr onIf(Expr predicate, Expr thenValue, Expr elseValue) {
    Expr predicateType = predicate.accept(this);
    if (isBool(predicateType)) {
      Expr thenType = thenValue.accept(this);
      Expr elseType = elseValue.accept(this);

      if (thenType.equals(Expr.Constants.SORT) || elseType.equals(Expr.Constants.SORT)) {
        throw TypeCheckFailure.makeSortError();
      }

      if (thenType.equivalent(elseType)) {
        return thenType;
      } else {
        throw TypeCheckFailure.makeIfBranchTypeMismatchError(thenType, elseType);
      }
    } else {
      throw TypeCheckFailure.makeIfPredicateError(predicateType);
    }
  }

  @Override
  public final Expr onAnnotated(Expr base, Expr type) {
    Expr inferredType = base.accept(this);
    if (inferredType.equivalent(type)) {
      return inferredType;
    } else {
      throw TypeCheckFailure.makeAnnotationError(type, inferredType);
    }
  }

  @Override
  public final Expr onAssert(Expr base) {
    Expr baseType = base.accept(this);

    if (isType(baseType)) {
      Expr normalized = base.accept(BetaNormalize.instance);
      Boolean isEquivalent = normalized.accept(CheckEquivalence.instance);
      if (isEquivalent != null && isEquivalent) {
        return normalized;
      }
    }
    throw TypeCheckFailure.makeAssertError(base);
  }

  @Override
  public final Expr onMerge(Expr handlers, Expr union, Expr type) {
    Expr handlersType = handlers.accept(this);
    List<Entry<String, Expr>> handlersTypeFields = Expr.Util.asRecordType(handlersType);

    if (handlersTypeFields == null) {
      // The handlers argument is not a record.
      throw TypeCheckFailure.makeMergeHandlersTypeError(handlersType);
    } else {
      Expr unionType = union.accept(this);
      List<Entry<String, Expr>> unionTypeFields = Expr.Util.asUnionType(unionType);

      if (unionTypeFields != null) {
        Expr inferredType = getMergeInferredType(handlersTypeFields, unionTypeFields);

        if (inferredType != null) {
          if (type == null || inferredType.equivalent(type)) {
            return inferredType;
          } else {
            throw TypeCheckFailure.makeMergeInvalidAnnotationError(type, inferredType);
          }
        } else if (type != null) {
          return type;
        } else {
          // Both were empty (this shouldn't happen).
          throw TypeCheckFailure.makeMergeUnionTypeError(type);
        }
      } else {
        Expr optionalElementType = Expr.Util.getOptionalArg(unionType);
        if (optionalElementType == null) {
          throw TypeCheckFailure.makeMergeUnionTypeError(unionType);
        } else {
          Expr inferredType =
              getMergeInferredType(
                  handlersTypeFields, makeOptionalConstructors(optionalElementType));

          if (inferredType != null) {
            if (type == null || inferredType.equivalent(type)) {
              return inferredType;
            } else {
              throw TypeCheckFailure.makeMergeInvalidAnnotationError(type, inferredType);
            }
          } else if (type != null) {
            return type;
          } else {
            // Both were empty (this shouldn't happen).
            throw TypeCheckFailure.makeMergeUnionTypeError(type);
          }
        }
      }
    }
  }

  @Override
  public final Expr onToMap(Expr base, Expr type) {
    Expr baseType = base.accept(this);
    List<Entry<String, Expr>> baseAsRecord = Expr.Util.asRecordType(baseType);

    if (baseAsRecord == null) {
      throw TypeCheckFailure.makeToMapTypeError(baseType);
    } else {
      Expr firstType = null;

      for (Entry<String, Expr> entry : baseAsRecord) {
        Expr fieldType = entry.getValue();

        if (!isType(fieldType.accept(this))) {
          throw TypeCheckFailure.makeToMapRecordKindError(fieldType);
        } else {
          if (firstType == null) {
            firstType = fieldType;
          } else {
            if (!fieldType.equivalent(firstType)) {
              throw TypeCheckFailure.makeToMapRecordTypeMismatchError(firstType, fieldType);
            }
          }
        }
      }

      // The input record is non-empty.
      if (firstType != null) {
        Entry[] inferredTypeFields = {
          new SimpleImmutableEntry<>(Constants.MAP_KEY_FIELD_NAME, Constants.TEXT),
          new SimpleImmutableEntry<>(Constants.MAP_VALUE_FIELD_NAME, firstType)
        };

        Expr inferredType =
            Expr.makeApplication(Constants.LIST, Expr.makeRecordType(inferredTypeFields));

        if (type == null || type.equivalent(inferredType)) {
          return inferredType;
        } else {
          throw TypeCheckFailure.makeToMapResultTypeMismatchError(type, inferredType);
        }
      } else {
        if (type == null) {
          throw TypeCheckFailure.makeToMapMissingAnnotationError();
        } else {
          Expr typeType = type.accept(this);

          if (!isType(typeType)) {
            throw TypeCheckFailure.makeToMapInvalidAnnotationError(type);
          } else {
            Expr typeNormalized = type.accept(BetaNormalize.instance);
            Expr listElementType = Expr.Util.getListArg(typeNormalized);

            if (listElementType == null) {
              throw TypeCheckFailure.makeToMapInvalidAnnotationError(type);
            } else {
              List<Entry<String, Expr>> typeFields = Expr.Util.asRecordType(listElementType);

              if (typeFields == null) {
                throw TypeCheckFailure.makeToMapInvalidAnnotationError(type);
              } else {
                boolean sawKey = false;
                boolean sawValue = false;

                for (Entry<String, Expr> typeField : typeFields) {
                  if (sawKey && sawValue) {
                    throw TypeCheckFailure.makeToMapInvalidAnnotationError(type);
                  } else {
                    if (typeField.getKey().equals(Constants.MAP_KEY_FIELD_NAME)) {
                      if (isText(typeField.getValue())) {
                        sawKey = true;
                      } else {
                        throw TypeCheckFailure.makeToMapInvalidAnnotationError(type);
                      }
                    } else if (typeField.getKey().equals(Constants.MAP_VALUE_FIELD_NAME)) {
                      sawValue = true;
                    } else {
                      throw TypeCheckFailure.makeToMapInvalidAnnotationError(type);
                    }
                  }
                }

                if (sawKey && sawValue) {
                  return typeNormalized;
                } else {
                  throw TypeCheckFailure.makeToMapInvalidAnnotationError(type);
                }
              }
            }
          }
        }
      }
    }
  }

  @Override
  public final Expr onWith(Expr base, String[] path, Expr value) {
    List<Entry<String, Expr>> baseAsRecordLiteral = Expr.Util.asRecordLiteral(base);

    if (baseAsRecordLiteral == null) {
      throw TypeCheckFailure.makeWithTypeError(base.accept(this));
    } else {
      List<Entry<String, Expr>> result = new ArrayList<>();
      boolean found = false;

      for (Entry<String, Expr> field : baseAsRecordLiteral) {
        String key = field.getKey();

        if (key.equals(path[0])) {
          found = true;

          Expr newValue;

          if (path.length == 1) {
            newValue = value.accept(this);
          } else {
            String[] remainingPath = new String[path.length - 1];
            System.arraycopy(path, 1, remainingPath, 0, path.length - 1);

            newValue = Expr.makeWith(field.getValue(), remainingPath, value).accept(this);
          }

          result.add(new SimpleImmutableEntry<>(key, newValue));
        } else {
          result.add(new SimpleImmutableEntry<>(key, field.getValue().accept(this)));
        }
      }

      if (!found) {
        Expr newValue;

        if (path.length == 1) {
          newValue = value.accept(this);
        } else {
          String[] remainingPath = new String[path.length - 1];
          System.arraycopy(path, 1, remainingPath, 0, path.length - 1);

          newValue =
              Expr.makeWith(Expr.Constants.EMPTY_RECORD_LITERAL, remainingPath, value).accept(this);
        }

        result.add(new SimpleImmutableEntry<>(path[0], newValue));
      }

      Entry<String, Expr>[] resultArray = result.toArray(new Entry[result.size()]);

      Arrays.sort(resultArray, entryComparator);

      return Expr.makeRecordType(resultArray);
    }
  }

  @Override
  public final Expr onMissingImport(Expr.ImportMode mode, byte[] hash) {
    throw TypeCheckFailure.makeUnresolvedImportError();
  }

  @Override
  public final Expr onEnvImport(String value, Expr.ImportMode mode, byte[] hash) {
    throw TypeCheckFailure.makeUnresolvedImportError();
  }

  @Override
  public final Expr onLocalImport(Path path, Expr.ImportMode mode, byte[] hash) {
    throw TypeCheckFailure.makeUnresolvedImportError();
  }

  @Override
  public final Expr onClasspathImport(Path path, Expr.ImportMode mode, byte[] hash) {
    throw TypeCheckFailure.makeUnresolvedImportError();
  }

  @Override
  public final Expr onRemoteImport(URI url, Expr using, Expr.ImportMode mode, byte[] hash) {
    throw TypeCheckFailure.makeUnresolvedImportError();
  }

  static final boolean isBool(Expr expr) {
    String asBuiltIn = Expr.Util.asBuiltIn(expr);
    return asBuiltIn != null && asBuiltIn.equals("Bool");
  }

  static final boolean isText(Expr expr) {
    String asBuiltIn = Expr.Util.asBuiltIn(expr);
    return asBuiltIn != null && asBuiltIn.equals("Text");
  }

  static final boolean isList(Expr expr) {
    String asBuiltIn = Expr.Util.asBuiltIn(expr);
    return asBuiltIn != null && asBuiltIn.equals("List");
  }

  static final boolean isNatural(Expr expr) {
    String asBuiltIn = Expr.Util.asBuiltIn(expr);
    return asBuiltIn != null && asBuiltIn.equals("Natural");
  }

  static final boolean isOptional(Expr expr) {
    String asBuiltIn = Expr.Util.asBuiltIn(expr);
    return asBuiltIn != null && asBuiltIn.equals("Optional");
  }

  static final boolean isType(Expr expr) {
    String asBuiltIn = Expr.Util.asBuiltIn(expr);
    return asBuiltIn != null && asBuiltIn.equals("Type");
  }

  private final void checkRecursiveTypeMerge(
      List<Entry<String, Expr>> lhs, List<Entry<String, Expr>> rhs) {

    Map<String, Expr> lhsMap = new HashMap<>();

    for (Entry<String, Expr> entry : lhs) {
      lhsMap.put(entry.getKey(), entry.getValue());
    }

    for (Entry<String, Expr> entry : rhs) {
      Expr rhsValue = entry.getValue();
      Expr lhsValue = lhsMap.get(entry.getKey());

      if (lhsValue != null) {
        Expr.makeOperatorApplication(Operator.COMBINE_TYPES, lhsValue, rhsValue).accept(this);
      }
    }
  }

  private static final List<Entry<String, Expr>> makeOptionalConstructors(Expr type) {
    List<Entry<String, Expr>> constructors = new ArrayList<>();
    constructors.add((Entry<String, Expr>) new SimpleImmutableEntry<>("None", (Expr) null));
    constructors.add((Entry<String, Expr>) new SimpleImmutableEntry<>("Some", type));
    return constructors;
  }

  private static final Expr getMergeInferredType(
      List<Entry<String, Expr>> handlerTypes, List<Entry<String, Expr>> constructors) {
    Map<String, Expr> handlerTypeMap = new HashMap<>();

    for (Entry<String, Expr> handlerTypeField : handlerTypes) {
      handlerTypeMap.put(handlerTypeField.getKey(), handlerTypeField.getValue());
    }

    Expr resultType = null;

    for (Entry<String, Expr> constructor : constructors) {
      String fieldName = constructor.getKey();
      Expr handlerType = handlerTypeMap.remove(fieldName);

      if (handlerType == null) {
        throw TypeCheckFailure.makeMergeHandlerMissingError(fieldName);
      } else {
        final Expr constructorType = constructor.getValue();

        if (constructorType == null) {
          // We have an empty constructor.
          if (resultType == null) {
            // This is the first constructor.
            resultType = handlerType;
          } else {
            // We check that the handler type is the same as previous result types.
            if (!handlerType.equivalent(resultType)) {
              throw TypeCheckFailure.makeMergeHandlerTypeMismatchError(resultType, handlerType);
            }
          }
        } else {
          // We have a constructor with a type, so we have to make sure the handler is a function.
          Expr handlerResultType =
              handlerType.accept(
                  new ExternalVisitor.Constant<Expr>(null) {
                    public Expr onPi(String name, Expr input, Expr result) {
                      if (!input.equivalent(constructorType)) {
                        throw TypeCheckFailure.makeMergeHandlerTypeInvalidError(
                            constructorType, input);
                      } else {
                        Expr inferredResultType = result.decrement(name);

                        if (!inferredResultType.accept(NonNegativeIndices.instance)) {
                          throw TypeCheckFailure.makeMergeHandlerTypeDisallowedError(
                              inferredResultType);
                        }

                        return inferredResultType;
                      }
                    }
                  });

          if (handlerResultType == null) {
            throw TypeCheckFailure.makeMergeHandlerTypeNotFunctionError(
                fieldName, constructorType, handlerType);
          } else if (resultType == null) {
            resultType = handlerResultType;
          } else {
            // We check that the handler result type is the same as previous result types.
            if (!handlerResultType.equivalent(resultType)) {
              throw TypeCheckFailure.makeMergeHandlerTypeMismatchError(
                  resultType, handlerResultType);
            }
          }
        }
      }
    }

    if (handlerTypeMap.isEmpty()) {
      return resultType;
    } else {
      throw TypeCheckFailure.makeMergeHandlerUnusedError(handlerTypeMap.keySet().iterator().next());
    }
  }

  private static final Entry<String, Expr>[] prefer(
      List<Entry<String, Expr>> base, List<Entry<String, Expr>> updates) {
    Map<String, Expr> updateMap = new LinkedHashMap<>();

    for (Entry<String, Expr> field : updates) {
      updateMap.put(field.getKey(), field.getValue());
    }

    List<Entry<String, Expr>> result = new ArrayList<>();

    for (Entry<String, Expr> field : base) {
      String key = field.getKey();

      Expr inUpdates = updateMap.remove(key);

      if (inUpdates == null) {
        result.add(field);
      } else {
        result.add(new SimpleImmutableEntry<>(key, inUpdates));
      }
    }

    for (Entry<String, Expr> field : updateMap.entrySet()) {
      result.add(field);
    }

    Entry<String, Expr>[] resultArray = result.toArray(new Entry[result.size()]);

    Arrays.sort(resultArray, entryComparator);

    return resultArray;
  }

  /** Java 8 introduce {@code comparingByKey}, but we can roll our own pretty easily. */
  private static final Comparator<Entry<String, Expr>> entryComparator =
      new Comparator<Entry<String, Expr>>() {
        public int compare(Entry<String, Expr> a, Entry<String, Expr> b) {
          return a.getKey().compareTo(b.getKey());
        }
      };
}
