package org.dhallj.core.visitor;

import org.dhallj.core.Expr;
import org.dhallj.core.Visitor;

/**
 * Represents a function that "pattern matches" on a single AST layer.
 *
 * <p>Note that while an external visitor may be recursive, the recursion is driven by the visitor,
 * not the expression.
 *
 * @param A The final result type
 */
public interface ExternalVisitor<A> extends Visitor<Expr, A> {}
