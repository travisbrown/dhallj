package org.dhallj.core;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Map.Entry;

final class ExprState {
  final Expr expr;
  int state;
  int size;
  Entry<String, Expr>[] sortedFields;
  boolean skippedRecursion = false;

  ExprState(Expr expr, int state, int size) {
    this.expr = expr;
    this.state = state;
    this.size = size;
    this.sortedFields = null;
  }

  ExprState(Expr expr, int state, Entry<String, Expr>[] fields, boolean sortFields) {
    this.expr = expr;
    this.state = state;
    this.size = 0;
    if (sortFields) {
      this.sortedFields = new Entry[fields.length];
      System.arraycopy(fields, 0, sortedFields, 0, fields.length);
      Arrays.sort(sortedFields, ExprState.entryComparator);
    } else {
      this.sortedFields = fields;
    }
  }

  ExprState(Expr expr, int state) {
    this(expr, state, 0);
  }

  /** Java 8 introduces {@code comparingByKey}, but we can roll our own pretty easily. */
  private static final Comparator<Entry<String, Expr>> entryComparator =
      new Comparator<Entry<String, Expr>>() {
        public int compare(Entry<String, Expr> a, Entry<String, Expr> b) {
          return a.getKey().compareTo(b.getKey());
        }
      };
}

final class VisitState<A> {
  final Visitor<A> visitor;

  ExprState current;
  final Deque<ExprState> stack;
  final Deque<A> valueStack;

  final Deque<LinkedList<Expr>> applicationStack;
    // Note that we have to use a linked list here because we store null values on the stack.
  final Deque<List<LetBinding<Expr>>> letBindingsStack;
  final Deque<List<String>> letBindingNamesStack;

  public VisitState(Visitor<A> visitor, Expr expr) {
    this.visitor = visitor;
    this.current = new State(expr, 0);
    this.stack = new ArrayDeque<ExprState>();
    this applicationStack = new LinkedList<A>();

    this.applicationStack = new ArrayDeque<>();
    this.letBindingsStack = new ArrayDeque<>();
    this.letBindingNamesStack = new ArrayDeque<>();
  }
}
