package org.dhallj.core.typechecking;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import org.dhallj.core.Import;
import org.dhallj.core.Operator;
import org.dhallj.core.Source;
import org.dhallj.core.Visitor;
import org.dhallj.core.Expr.Constants;
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

  public final Expr onIdentifier(String name, long index) {
    Expr fromContext = this.context.lookup(name, index);

    if (fromContext != null) {
      return fromContext;
    } else {
      throw TypeCheckFailure.makeUnboundVariableError(name);
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
          combineTypes.acceptExternal(this);
        } catch (TypeCheckFailure e) {
          throw TypeCheckFailure.makeOperatorError(operator);
        }

        return combineTypes.acceptVis(BetaNormalize.instance);
      case PREFER:
        List<Entry<String, Expr>> lhsTypeRecordType = lhsType.asRecordType();
        List<Entry<String, Expr>> rhsTypeRecordType = rhsType.asRecordType();

        if (lhsTypeRecordType != null && rhsTypeRecordType != null) {
          return Expr.makeRecordType(prefer(lhsTypeRecordType, rhsTypeRecordType));
        } else {
          throw TypeCheckFailure.makeOperatorError(operator);
        }
      case COMBINE_TYPES:
        List<Entry<String, Expr>> lhsRecordType =
            lhs.acceptVis(BetaNormalize.instance).asRecordType();
        List<Entry<String, Expr>> rhsRecordType =
            rhs.acceptVis(BetaNormalize.instance).asRecordType();

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
        return lhsType.acceptExternal(this);
      case EQUIVALENT:
        Expr lhsTypeType = lhsType.acceptExternal(this);
        Expr rhsTypeType = rhsType.acceptExternal(this);

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
    for (Expr expr : interpolated) {
      Expr exprType = expr.acceptExternal(this);
      if (!isText(exprType)) {
        throw TypeCheckFailure.makeInterpolationError(expr, exprType);
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
              public Expr onBuiltIn(String name) {
                if (name.equals("Some")) {
                  if (isType(argType.acceptExternal(TypeCheck.this))) {
                    return Expr.makeApplication(Expr.Constants.OPTIONAL, argType);
                  } else {
                    throw TypeCheckFailure.makeSomeApplicationError(arg, argType);
                  }
                }
                throw TypeCheckFailure.makeBuiltInApplicationError(name, arg, argType);
              }

              @Override
              public Expr onPi(String param, Expr input, Expr result) {
                if (input.equivalent(argType)) {
                  return result
                      .substitute(param, arg.increment(param))
                      .decrement(param)
                      .normalize();
                } else {
                  throw TypeCheckFailure.makeApplicationTypeError(input, argType);
                }
              }
            });

    if (result != null) {
      return result;
    } else {
      throw TypeCheckFailure.makeApplicationError(base, arg);
    }
  }

  public final Expr onIf(Expr predicate, Expr thenValue, Expr elseValue) {
    Expr predicateType = predicate.acceptExternal(this);
    if (isBool(predicateType)) {
      Expr thenType = thenValue.acceptExternal(this);
      Expr elseType = elseValue.acceptExternal(this);

      boolean thenValueIsTerm = isType(thenType.acceptExternal(this));
      boolean elseValueIsTerm = isType(elseType.acceptExternal(this));

      if (thenValueIsTerm && elseValueIsTerm) {
        if (thenType.equivalent(elseType)) {
          return thenType;
        } else {
          throw TypeCheckFailure.makeIfBranchTypeMismatchError(thenType, elseType);
        }
      } else {
        if (!thenValueIsTerm) {
          throw TypeCheckFailure.makeIfBranchError(thenType);
        } else {
          throw TypeCheckFailure.makeIfBranchError(elseType);
        }
      }
    } else {
      throw TypeCheckFailure.makeIfPredicateError(predicateType);
    }
  }

  public final Expr onLambda(String param, Expr input, Expr result) {
    Expr inputType = input.acceptExternal(this);
    if (Universe.fromExpr(inputType) != null) {
      Context unshiftedContext = this.context;
      Expr inputNormalized = input.acceptVis(BetaNormalize.instance);
      this.context = this.context.insert(param, inputNormalized).increment(param);
      Expr resultType = result.acceptExternal(this);
      this.context = unshiftedContext;
      return Expr.makePi(param, inputNormalized, resultType);
    } else {
      throw TypeCheckFailure.makeLambdaInputError(inputType);
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
      Expr normalized = base.acceptVis(BetaNormalize.instance);
      Boolean isEquivalent = normalized.acceptExternal(CheckEquivalence.instance);
      if (isEquivalent != null && isEquivalent) {
        return normalized;
      }
    }
    throw TypeCheckFailure.makeAssertError(base);
  }

  public final Expr onFieldAccess(Expr base, String fieldName) {
    Expr baseType = base.acceptExternal(this);
    List<Entry<String, Expr>> fields = baseType.asRecordType();

    if (fields != null) {
      for (Entry<String, Expr> field : fields) {
        if (field.getKey().equals(fieldName)) {
          return field.getValue();
        }
      }
      throw TypeCheckFailure.makeFieldAccessRecordMissingError(fieldName);
    } else {
      Expr baseNormalized = base.acceptVis(BetaNormalize.instance);
      List<Entry<String, Expr>> alternatives = baseNormalized.asUnionType();
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

  public final Expr onProjection(Expr base, String[] fieldNames) {
    List<Entry<String, Expr>> fields = base.acceptExternal(this).asRecordType();

    if (fields != null) {
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
            missing = new ArrayList<>();
          }
          missing.add(fieldName);
        } else {
          newFields.add(new SimpleImmutableEntry(fieldName, value));
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

  public final Expr onProjectionByType(Expr base, Expr type) {
    List<Entry<String, Expr>> fields = base.acceptExternal(this).asRecordType();

    if (fields != null) {
      List<Entry<String, Expr>> projected = type.acceptVis(BetaNormalize.instance).asRecordType();

      if (projected != null) {
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
          throw TypeCheckFailure.makeFieldAccessRecordMissingError(missing.get(0));
        }
      } else {
        throw TypeCheckFailure.makeProjectionError();
      }
    } else {
      throw TypeCheckFailure.makeProjectionError();
    }
  }

  public final Expr onRecordLiteral(Iterable<Entry<String, Expr>> fields, int size) {
    if (size == 0) {
      return Constants.EMPTY_RECORD_TYPE;
    } else {
      Map<String, Expr> fieldTypes = new TreeMap();

      for (Entry<String, Expr> field : fields) {
        fieldTypes.put(
            field.getKey(),
            field.getValue().acceptExternal(this).acceptVis(BetaNormalize.instance));
      }

      Expr recordType = Expr.makeRecordType(fieldTypes.entrySet());

      // The inferred type must also be well-typed.
      recordType.acceptExternal(this);
      return recordType;
    }
  }

  public final Expr onRecordType(Iterable<Entry<String, Expr>> fields, int size) {
    Universe max = Universe.TYPE;

    for (Entry<String, Expr> field : fields) {
      Universe universe = Universe.fromExpr(field.getValue().acceptExternal(this));

      if (universe != null) {
        max = max.max(universe);
      } else {
        throw TypeCheckFailure.makeFieldTypeError(field.getKey());
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
        String fieldName = field.getKey();

        if (!seen.contains(fieldName)) {
          seen.add(fieldName);

          Expr alternativeType = field.getValue();

          if (alternativeType != null) {
            Universe universe = Universe.fromExpr(alternativeType.acceptExternal(this));

            if (universe != null) {
              if (firstUniverse == null) {
                firstUniverse = universe;
                first = field;
              } else {
                if (universe != firstUniverse) {
                  throw TypeCheckFailure.makeAlternativeTypeMismatchError(alternativeType);
                }
              }
            } else {
              throw TypeCheckFailure.makeAlternativeTypeError(alternativeType);
            }
          }
        } else {
          throw TypeCheckFailure.makeAlternativeDuplicateError(fieldName);
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
    Expr firstType = it.next().acceptExternal(this);

    if (isType(firstType.acceptExternal(this))) {
      while (it.hasNext()) {
        Expr elementType = it.next().acceptExternal(this);
        if (!elementType.equivalent(firstType)) {
          throw TypeCheckFailure.makeListTypeMismatchError(firstType, elementType);
        }
      }

      return Expr.makeApplication(Expr.Constants.LIST, firstType);
    } else {
      throw TypeCheckFailure.makeListTypeError(firstType);
    }
  }

  public final Expr onEmptyListLiteral(Expr type) {
    Expr typeType = type.acceptExternal(this);

    Expr typeNormalized = type.acceptVis(BetaNormalize.instance);
    Expr elementType = Expr.Util.getListArg(typeNormalized);

    if (elementType != null && isType(elementType.acceptExternal(this))) {
      return Expr.makeApplication(Constants.LIST, elementType);
    } else {
      throw TypeCheckFailure.makeListTypeError(elementType);
    }
  }

  public final Expr onLet(String name, Expr type, Expr value, Expr body) {
    Expr valueType = value.acceptExternal(this);

    if (type != null) {
      if (!type.equivalent(valueType)) {
        throw TypeCheckFailure.makeAnnotationError(type, valueType);
      }
    }

    return body.substitute(name, value.acceptVis(BetaNormalize.instance).increment(name))
        .decrement(name)
        .acceptExternal(this);
  }

  public final Expr onAnnotated(Expr base, Expr type) {
    Expr inferredType = base.acceptExternal(this);
    if (inferredType.equivalent(type)) {
      return inferredType;
    } else {
      throw TypeCheckFailure.makeAnnotationError(type, inferredType);
    }
  }

  public final Expr onToMap(Expr base, Expr type) {
    Expr baseType = base.acceptExternal(this);
    List<Entry<String, Expr>> baseAsRecord = baseType.asRecordType();

    if (baseAsRecord == null) {
      throw TypeCheckFailure.makeToMapTypeError(baseType);
    } else {
      Expr firstType = null;
      Entry<String, Expr> first = null;

      for (Entry<String, Expr> entry : baseAsRecord) {
        Expr fieldType = entry.getValue();

        if (!isType(fieldType.acceptExternal(this))) {
          throw TypeCheckFailure.makeToMapRecordKindError(fieldType);
        } else {
          if (firstType == null) {
            firstType = fieldType;
            first = entry;
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
          new SimpleImmutableEntry(Constants.MAP_KEY_FIELD_NAME, Constants.TEXT),
          new SimpleImmutableEntry(Constants.MAP_VALUE_FIELD_NAME, firstType)
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
          Expr typeType = type.acceptExternal(this);

          if (!isType(typeType)) {
            throw TypeCheckFailure.makeToMapInvalidAnnotationError(type);
          } else {
            Expr typeNormalized = type.acceptVis(BetaNormalize.instance);
            Expr listElementType = Expr.Util.getListArg(typeNormalized);

            if (listElementType == null) {
              throw TypeCheckFailure.makeToMapInvalidAnnotationError(type);
            } else {
              List<Entry<String, Expr>> typeFields = listElementType.asRecordType();

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

  public final Expr onMerge(Expr handlers, Expr union, Expr type) {
    Expr handlersType = handlers.acceptExternal(this);
    List<Entry<String, Expr>> handlersTypeFields = handlersType.asRecordType();

    if (handlersTypeFields == null) {
      throw TypeCheckFailure.makeMergeHandlersTypeError(handlersType);
    } else {
      Expr unionType = union.acceptExternal(this);
      List<Entry<String, Expr>> unionTypeFields = unionType.asUnionType();

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
          throw TypeCheckFailure.makeMergeInvalidAnnotationError(type, inferredType);
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
            throw TypeCheckFailure.makeMergeInvalidAnnotationError(type, inferredType);
          }
        }
      }
    }
  }

  public final Expr onNote(Expr base, Source source) {
    return base.acceptExternal(this);
  }

  public final Expr onLocalImport(Path path, Import.Mode mode, byte[] hash) {
    throw TypeCheckFailure.makeUnresolvedImportError();
  }

  public final Expr onRemoteImport(URI url, Expr using, Import.Mode mode, byte[] hash) {
    throw TypeCheckFailure.makeUnresolvedImportError();
  }

  public final Expr onEnvImport(String value, Import.Mode mode, byte[] hash) {
    throw TypeCheckFailure.makeUnresolvedImportError();
  }

  public final Expr onMissingImport(Import.Mode mode, byte[] hash) {
    throw TypeCheckFailure.makeUnresolvedImportError();
  }

  static boolean isBool(Expr expr) {
    String asBuiltIn = expr.asBuiltIn();
    return asBuiltIn != null && asBuiltIn.equals("Bool");
  }

  static boolean isText(Expr expr) {
    String asBuiltIn = expr.asBuiltIn();
    return asBuiltIn != null && asBuiltIn.equals("Text");
  }

  static boolean isList(Expr expr) {
    String asBuiltIn = expr.asBuiltIn();
    return asBuiltIn != null && asBuiltIn.equals("List");
  }

  static boolean isNatural(Expr expr) {
    String asBuiltIn = expr.asBuiltIn();
    return asBuiltIn != null && asBuiltIn.equals("Natural");
  }

  static boolean isOptional(Expr expr) {
    String asBuiltIn = expr.asBuiltIn();
    return asBuiltIn != null && asBuiltIn.equals("Optional");
  }

  static boolean isType(Expr expr) {
    String asBuiltIn = expr.asBuiltIn();
    return asBuiltIn != null && asBuiltIn.equals("Type");
  }

  private final void checkRecursiveTypeMerge(
      List<Entry<String, Expr>> lhs, List<Entry<String, Expr>> rhs) {

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

  private List<Entry<String, Expr>> makeOptionalConstructors(Expr type) {
    List<Entry<String, Expr>> constructors = new ArrayList<>();
    constructors.add(new SimpleImmutableEntry("None", null));
    constructors.add(new SimpleImmutableEntry("Some", type));
    return constructors;
  }

  private Expr getMergeInferredType(
      List<Entry<String, Expr>> handlerTypes, List<Entry<String, Expr>> constructors) {
    Map<String, Expr> handlerTypeMap = new HashMap();

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
              handlerType.acceptExternal(
                  new ConstantVisitor.External<Expr>(null) {
                    public Expr onPi(String name, Expr input, Expr result) {
                      if (!input.equivalent(constructorType)) {
                        throw TypeCheckFailure.makeMergeHandlerTypeInvalidError(
                            constructorType, input);
                      } else {
                        Expr inferredResultType = result.decrement(name);

                        if (!inferredResultType.acceptVis(NonNegativeIndices.instance)) {
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
    Map<String, Expr> updateMap = new LinkedHashMap();

    for (Entry<String, Expr> field : updates) {
      updateMap.put(field.getKey(), field.getValue());
    }

    List<Entry<String, Expr>> result = new ArrayList();

    for (Entry<String, Expr> field : base) {
      String key = field.getKey();

      Expr inUpdates = updateMap.remove(key);

      if (inUpdates == null) {
        result.add(field);
      } else {
        result.add(new SimpleImmutableEntry(key, inUpdates));
      }
    }

    for (Entry<String, Expr> field : updateMap.entrySet()) {
      result.add(field);
    }

    Entry<String, Expr>[] resultArray =
        result.toArray((Entry<String, Expr>[]) Array.newInstance(Entry.class, result.size()));

    Arrays.sort(resultArray, FieldUtilities.entryComparator);

    return resultArray;
  }
}
