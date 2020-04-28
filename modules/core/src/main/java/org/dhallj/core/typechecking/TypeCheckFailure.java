package org.dhallj.core.typechecking;

import org.dhallj.core.DhallException;
import org.dhallj.core.Expr;
import org.dhallj.core.Operator;

public final class TypeCheckFailure extends DhallException {
  @Override
  public Throwable fillInStackTrace() {
    // This is a failure type; stack traces aren't useful.
    return this;
  }

  TypeCheckFailure(String message) {
    super(message);
  }

  static final TypeCheckFailure makeSortError() {
    return new TypeCheckFailure("Sort has no type, kind, or sort");
  }

  static final TypeCheckFailure makeUnboundVariableError(String name) {
    return new TypeCheckFailure("Unbound variable: " + name);
  }

  static final TypeCheckFailure makeOperatorError(Operator operator) {
    String message;

    if (operator == Operator.OR
        || operator == Operator.AND
        || operator == Operator.EQUALS
        || operator == Operator.NOT_EQUALS) {
      message = operator.toString() + " only works on Bools";
    } else if (operator == Operator.PLUS || operator == Operator.TIMES) {
      message = operator.toString() + " only works on Naturals";
    } else if (operator == Operator.TEXT_APPEND) {
      message = operator.toString() + " only works on Text";
    } else if (operator == Operator.LIST_APPEND) {
      message = operator.toString() + " only works on Lists";
    } else if (operator == Operator.COMBINE || operator == Operator.PREFER) {
      message = "You can only combine records";
    } else if (operator == Operator.COMBINE_TYPES) {
      message = operator.toString() + " requires arguments that are record types";
    } else if (operator == Operator.EQUIVALENT) {
      message = "Incomparable expression";
    } else {
      message = "Operator error on " + operator.toString();
    }

    return new TypeCheckFailure(message);
  }

  static final TypeCheckFailure makeListAppendError(Expr lhs, Expr rhs) {
    return new TypeCheckFailure("You can only append Lists with matching element types");
  }

  static final TypeCheckFailure makeEquivalenceError(Expr lhs, Expr rhs) {
    return new TypeCheckFailure("You can only append Lists with matching element types");
  }

  static final TypeCheckFailure makeInterpolationError(Expr interpolated, Expr interpolatedType) {
    return new TypeCheckFailure("You can only interpolate Text");
  }

  static final TypeCheckFailure makeSomeApplicationError(Expr arg, Expr argType) {
    return new TypeCheckFailure("Some argument has the wrong type");
  }

  static final TypeCheckFailure makeBuiltInApplicationError(String name, Expr arg, Expr argType) {
    return new TypeCheckFailure("Can't apply " + name);
  }

  static final TypeCheckFailure makeApplicationTypeError(Expr expected, Expr received) {
    return new TypeCheckFailure("Wrong type of function argument");
  }

  static final TypeCheckFailure makeApplicationError(Expr base, Expr arg) {
    return new TypeCheckFailure("Not a function");
  }

  static final TypeCheckFailure makeUnresolvedImportError() {
    return new TypeCheckFailure("Can't type-check unresolved import");
  }

  static final TypeCheckFailure makeIfPredicateError(Expr type) {
    return new TypeCheckFailure("Invalid predicate for if");
  }

  static final TypeCheckFailure makeIfBranchTypeMismatchError(Expr thenType, Expr elseType) {
    return new TypeCheckFailure("if branches must have matching types");
  }

  static final TypeCheckFailure makeIfBranchError(Expr type) {
    return new TypeCheckFailure("if branch is not a term");
  }

  static final TypeCheckFailure makeLambdaInputError(Expr type) {
    return new TypeCheckFailure("Invalid function input");
  }

  static final TypeCheckFailure makeAssertError(Expr type) {
    return new TypeCheckFailure("Not an equivalence");
  }

  static final TypeCheckFailure makeFieldAccessError() {
    return new TypeCheckFailure("Not a record or union");
  }

  static final TypeCheckFailure makeFieldAccessRecordMissingError(String fieldName) {
    return new TypeCheckFailure("Missing record field: " + fieldName);
  }

  static final TypeCheckFailure makeFieldAccessUnionMissingError(String fieldName) {
    return new TypeCheckFailure("Missing constructor: " + fieldName);
  }

  static final TypeCheckFailure makeProjectionError() {
    return new TypeCheckFailure("Not a record");
  }

  static final TypeCheckFailure makeFieldTypeError(String fieldName, Expr type) {
    return new TypeCheckFailure("Invalid field type");
  }

  static final TypeCheckFailure makeFieldDuplicateError(String fieldName) {
    return new TypeCheckFailure("duplicate field: " + fieldName);
  }

  static final TypeCheckFailure makeListTypeMismatchError(Expr type1, Expr type2) {
    return new TypeCheckFailure("List elements should all have the same type");
  }

  static final TypeCheckFailure makeListTypeError(Expr type) {
    return new TypeCheckFailure("Invalid type for List");
  }

  static final TypeCheckFailure makeAnnotationError(Expr expected, Expr received) {
    return new TypeCheckFailure("Expression doesn't match annotation");
  }

  static final TypeCheckFailure makeAlternativeTypeError(String fieldName, Expr type) {
    return new TypeCheckFailure("Invalid alternative type");
  }

  /** Not sure under what conditions this wouldn't be caught by the parser. */
  static final TypeCheckFailure makeAlternativeDuplicateError(String fieldName) {
    return new TypeCheckFailure("duplicate field: " + fieldName);
  }

  static final TypeCheckFailure makeMergeHandlersTypeError(Expr type) {
    return new TypeCheckFailure("merge expects a record of handlers");
  }

  static final TypeCheckFailure makeMergeUnionTypeError(Expr type) {
    return new TypeCheckFailure("toMap expects a union or an Optional");
  }

  static final TypeCheckFailure makeMergeHandlerMissingError(String fieldName) {
    return new TypeCheckFailure("Missing handler: " + fieldName);
  }

  static final TypeCheckFailure makeMergeHandlerUnusedError(String fieldName) {
    return new TypeCheckFailure("Unused handler: " + fieldName);
  }

  static final TypeCheckFailure makeMergeHandlerTypeInvalidError(Expr expected, Expr type) {
    return new TypeCheckFailure("Wrong handler input type");
  }

  static final TypeCheckFailure makeMergeHandlerTypeNotFunctionError(
      String fieldName, Expr expected, Expr type) {
    return new TypeCheckFailure("Handler for " + fieldName + " is not a function");
  }

  static final TypeCheckFailure makeMergeHandlerTypeMismatchError(Expr type1, Expr type2) {
    return new TypeCheckFailure("Handlers should have the same output type");
  }

  static final TypeCheckFailure makeMergeHandlerTypeDisallowedError(Expr type) {
    return new TypeCheckFailure("Disallowed handler type");
  }

  static final TypeCheckFailure makeMergeInvalidAnnotationError(Expr expected, Expr inferred) {
    return new TypeCheckFailure("Expression doesn't match annotation");
  }

  static final TypeCheckFailure makeToMapTypeError(Expr type) {
    return new TypeCheckFailure("toMap expects a record value");
  }

  static final TypeCheckFailure makeToMapRecordKindError(Expr type) {
    return new TypeCheckFailure("toMap expects a record of kind Type");
  }

  static final TypeCheckFailure makeToMapRecordTypeMismatchError(Expr type1, Expr type2) {
    return new TypeCheckFailure("toMap expects a homogenous record");
  }

  static final TypeCheckFailure makeToMapResultTypeMismatchError(Expr expected, Expr inferred) {
    return new TypeCheckFailure("toMap result type doesn't match annotation");
  }

  static final TypeCheckFailure makeToMapMissingAnnotationError() {
    return new TypeCheckFailure("An empty toMap requires a type annotation");
  }

  static final TypeCheckFailure makeToMapInvalidAnnotationError(Expr type) {
    return new TypeCheckFailure("An empty toMap was annotated with an invalid type");
  }
}
