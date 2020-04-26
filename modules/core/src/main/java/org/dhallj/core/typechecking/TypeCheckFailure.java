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

  static TypeCheckFailure makeSortError() {
    return new TypeCheckFailure("Sort has no type, kind, or sort");
  }

  static TypeCheckFailure makeUnboundVariableError(String name) {
    return new TypeCheckFailure("Unbound variable: " + name);
  }

  static TypeCheckFailure makeOperatorError(Operator operator) {
    switch (operator) {
      case OR:
      case AND:
      case EQUALS:
      case NOT_EQUALS:
        return new TypeCheckFailure(operator.toString() + " only works on Bools");
      case PLUS:
      case TIMES:
        return new TypeCheckFailure(operator.toString() + " only works on Naturals");
      case TEXT_APPEND:
        return new TypeCheckFailure(operator.toString() + " only works on Text");
      case LIST_APPEND:
        return new TypeCheckFailure(operator.toString() + " only works on Lists");
      case COMBINE:
      case PREFER:
        return new TypeCheckFailure("You can only combine records");
      case COMBINE_TYPES:
        return new TypeCheckFailure(
            operator.toString() + " requires arguments that are record types");
      case EQUIVALENT:
        return new TypeCheckFailure("Incomparable expression");
      default:
        return new TypeCheckFailure("Operator error on " + operator.toString());
    }
  }

  static TypeCheckFailure makeListAppendError(Expr lhs, Expr rhs) {
    return new TypeCheckFailure("You can only append Lists with matching element types");
  }

  static TypeCheckFailure makeEquivalenceError(Expr lhs, Expr rhs) {
    return new TypeCheckFailure("You can only append Lists with matching element types");
  }

  static TypeCheckFailure makeInterpolationError(Expr interpolated, Expr interpolatedType) {
    return new TypeCheckFailure("You can only interpolate Text");
  }

  static TypeCheckFailure makeSomeApplicationError(Expr arg, Expr argType) {
    return new TypeCheckFailure("Some argument has the wrong type");
  }

  static TypeCheckFailure makeBuiltInApplicationError(String name, Expr arg, Expr argType) {
    return new TypeCheckFailure("Can't apply " + name);
  }

  static TypeCheckFailure makeApplicationTypeError(Expr expected, Expr received) {
    return new TypeCheckFailure("Wrong type of function argument");
  }

  static TypeCheckFailure makeApplicationError(Expr base, Expr arg) {
    return new TypeCheckFailure("Not a function");
  }

  static TypeCheckFailure makeUnresolvedImportError() {
    return new TypeCheckFailure("Can't type-check unresolved import");
  }

  static TypeCheckFailure makeIfPredicateError(Expr type) {
    return new TypeCheckFailure("Invalid predicate for if");
  }

  static TypeCheckFailure makeIfBranchTypeMismatchError(Expr thenType, Expr elseType) {
    return new TypeCheckFailure("if branches must have matching types");
  }

  static TypeCheckFailure makeIfBranchError(Expr type) {
    return new TypeCheckFailure("if branch is not a term");
  }

  static TypeCheckFailure makeLambdaInputError(Expr type) {
    return new TypeCheckFailure("Invalid function input");
  }

  static TypeCheckFailure makeAssertError(Expr type) {
    return new TypeCheckFailure("Not an equivalence");
  }

  static TypeCheckFailure makeFieldAccessError() {
    return new TypeCheckFailure("Not a record or union");
  }

  static TypeCheckFailure makeFieldAccessRecordMissingError(String fieldName) {
    return new TypeCheckFailure("Missing record field: " + fieldName);
  }

  static TypeCheckFailure makeFieldAccessUnionMissingError(String fieldName) {
    return new TypeCheckFailure("Missing constructor: " + fieldName);
  }

  static TypeCheckFailure makeProjectionError() {
    return new TypeCheckFailure("Not a record");
  }

  static TypeCheckFailure makeFieldTypeError(String fieldName) {
    return new TypeCheckFailure("Invalid field type");
  }

  static TypeCheckFailure makeFieldDuplicateError(String fieldName) {
    return new TypeCheckFailure("duplicate field: " + fieldName);
  }

  static TypeCheckFailure makeListTypeMismatchError(Expr type1, Expr type2) {
    return new TypeCheckFailure("List elements should all have the same type");
  }

  static TypeCheckFailure makeListTypeError(Expr type) {
    return new TypeCheckFailure("Invalid type for List");
  }

  static TypeCheckFailure makeAnnotationError(Expr expected, Expr received) {
    return new TypeCheckFailure("Expression doesn't match annotation");
  }

  static TypeCheckFailure makeAlternativeTypeMismatchError(Expr type) {
    return new TypeCheckFailure("Alternative annotation mismatch");
  }

  static TypeCheckFailure makeAlternativeTypeError(Expr type) {
    return new TypeCheckFailure("Invalid alternative type");
  }

  /** Not sure under what conditions this wouldn't be caught by the parser.s */
  static TypeCheckFailure makeAlternativeDuplicateError(String fieldName) {
    return new TypeCheckFailure("duplicate field: " + fieldName);
  }

  static TypeCheckFailure makeMergeHandlersTypeError(Expr type) {
    return new TypeCheckFailure("merge expects a record of handlers");
  }

  static TypeCheckFailure makeMergeUnionTypeError(Expr type) {
    return new TypeCheckFailure("toMap expects a union or an Optional");
  }

  static TypeCheckFailure makeMergeHandlerMissingError(String fieldName) {
    return new TypeCheckFailure("Missing handler: " + fieldName);
  }

  static TypeCheckFailure makeMergeHandlerUnusedError(String fieldName) {
    return new TypeCheckFailure("Unused handler: " + fieldName);
  }

  static TypeCheckFailure makeMergeHandlerTypeInvalidError(Expr expected, Expr type) {
    return new TypeCheckFailure("Wrong handler input type");
  }

  static TypeCheckFailure makeMergeHandlerTypeNotFunctionError(
      String fieldName, Expr expected, Expr type) {
    return new TypeCheckFailure("Handler for " + fieldName + " is not a function");
  }

  static TypeCheckFailure makeMergeHandlerTypeMismatchError(Expr type1, Expr type2) {
    return new TypeCheckFailure("Handlers should have the same output type");
  }

  static TypeCheckFailure makeMergeHandlerTypeDisallowedError(Expr type) {
    return new TypeCheckFailure("Disallowed handler type");
  }

  static TypeCheckFailure makeMergeInvalidAnnotationError(Expr expected, Expr inferred) {
    return new TypeCheckFailure("Expression doesn't match annotation");
  }

  static TypeCheckFailure makeToMapTypeError(Expr type) {
    return new TypeCheckFailure("toMap expects a record value");
  }

  static TypeCheckFailure makeToMapRecordKindError(Expr type) {
    return new TypeCheckFailure("toMap expects a record of kind Type");
  }

  static TypeCheckFailure makeToMapRecordTypeMismatchError(Expr type1, Expr type2) {
    return new TypeCheckFailure("toMap expects a homogenous record");
  }

  static TypeCheckFailure makeToMapResultTypeMismatchError(Expr expected, Expr inferred) {
    return new TypeCheckFailure("toMap result type doesn't match annotation");
  }

  static TypeCheckFailure makeToMapMissingAnnotationError() {
    return new TypeCheckFailure("An empty toMap requires a type annotation");
  }

  static TypeCheckFailure makeToMapInvalidAnnotationError(Expr type) {
    return new TypeCheckFailure("An empty toMap was annotated with an invalid type");
  }
}
