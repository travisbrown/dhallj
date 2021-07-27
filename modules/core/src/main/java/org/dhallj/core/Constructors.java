package org.dhallj.core;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.nio.file.Path;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Constructors for the Dhall expression abstract syntax tree.
 *
 * <p>Note that nothing in this file is public, and that custom code shouldn't be added here, since
 * this is generated from the visitor definition.
 */
final class Constructors {
  static final class NaturalLiteral extends Expr {
    final BigInteger value;

    NaturalLiteral(BigInteger value) {
      super(Tags.NATURAL);
      this.value = value;
    }

    public final <A> A accept(ExternalVisitor<A> visitor) {
      return visitor.onNatural(this.value);
    }

    final <A> void advance(VisitState<A> state) {
      state.valueStack.push(state.visitor.onNatural(this, this.value));
    }
  }

  static final class IntegerLiteral extends Expr {
    final BigInteger value;

    IntegerLiteral(BigInteger value) {
      super(Tags.INTEGER);
      this.value = value;
    }

    public final <A> A accept(ExternalVisitor<A> visitor) {
      return visitor.onInteger(this.value);
    }

    final <A> void advance(VisitState<A> state) {
      state.valueStack.push(state.visitor.onInteger(this, this.value));
    }
  }

  static final class DoubleLiteral extends Expr {
    final double value;

    DoubleLiteral(double value) {
      super(Tags.DOUBLE);
      this.value = value;
    }

    public final <A> A accept(ExternalVisitor<A> visitor) {
      return visitor.onDouble(this.value);
    }

    final <A> void advance(VisitState<A> state) {
      state.valueStack.push(state.visitor.onDouble(this, this.value));
    }
  }

  static final class DateLiteral extends Expr {
    final int year;
    final int month;
    final int day;

    DateLiteral(int year, int month, int day) {
      super(Tags.DATE);
      this.year = year;
      this.month = month;
      this.day = day;
    }

    public final <A> A accept(ExternalVisitor<A> visitor) {
      return visitor.onDate(this.year, this.month, this.day);
    }

    final <A> void advance(VisitState<A> state) {
      state.valueStack.push(state.visitor.onDate(this, this.year, this.month, this.day));
    }
  }

  static final class TimeLiteral extends Expr {
    final int hour;
    final int minute;
    final int second;
    final BigDecimal fractional;

    TimeLiteral(int hour, int minute, int second, BigDecimal fractional) {
      super(Tags.TIME);
      this.hour = hour;
      this.minute = minute;
      this.second = second;
      this.fractional = fractional;
    }

    public final <A> A accept(ExternalVisitor<A> visitor) {
      return visitor.onTime(this.hour, this.minute, this.second, this.fractional);
    }

    final <A> void advance(VisitState<A> state) {
      state.valueStack.push(
          state.visitor.onTime(this, this.hour, this.minute, this.second, this.fractional));
    }
  }

  static final class TimeZoneLiteral extends Expr {
    final int minutes;

    TimeZoneLiteral(int minutes) {
      super(Tags.TIME_ZONE);
      this.minutes = minutes;
    }

    public final <A> A accept(ExternalVisitor<A> visitor) {
      return visitor.onTimeZone(this.minutes);
    }

    final <A> void advance(VisitState<A> state) {
      state.valueStack.push(state.visitor.onTimeZone(this, this.minutes));
    }
  }

  static final class TextLiteral extends Expr {
    final String[] parts;
    final Expr[] interpolated;

    TextLiteral(String[] parts, Expr[] interpolated) {
      super(Tags.TEXT);
      this.parts = parts;
      this.interpolated = interpolated;
    }

    public final <A> A accept(ExternalVisitor<A> visitor) {
      return visitor.onText(this.parts, new ArrayIterable<Expr>(interpolated));
    }

    final <A> void advance(VisitState<A> state) {
      if (state.current.state == 0) {
        state.visitor.prepareText(this.parts.length);
        state.visitor.prepareTextPart(this.parts[0]);

        if (this.interpolated.length == 0) {
          state.valueStack.push(state.visitor.onText(this.parts, new ArrayList<A>()));
        } else {
          state.current.state = 1;
          state.stack.push(state.current);
          state.stack.push(new ExprState(this.interpolated[state.current.state - 1], 0));
        }
      } else if (state.current.state == this.interpolated.length) {
        state.visitor.prepareTextPart(this.parts[this.parts.length - 1]);
        List<A> results = new ArrayList<A>();
        for (int i = 0; i < this.interpolated.length; i += 1) {
          results.add(state.valueStack.poll());
        }
        Collections.reverse(results);
        state.valueStack.push(state.visitor.onText(this.parts, results));
      } else {
        state.current.state += 1;
        state.visitor.prepareTextPart(this.parts[state.current.state - 1]);
        state.stack.push(state.current);
        state.stack.push(new ExprState(this.interpolated[state.current.state - 1], 0));
      }
    }
  }

  static final class Application extends Expr {
    final Expr base;
    final Expr arg;

    Application(Expr base, Expr arg) {
      super(Tags.APPLICATION);
      this.base = base;
      this.arg = arg;
    }

    public final <A> A accept(ExternalVisitor<A> visitor) {
      return visitor.onApplication(base, arg);
    }

    final <A> void advance(VisitState<A> state) {
      if (state.current.state == 0) {
        LinkedList<Expr> application = new LinkedList<>();
        application.push(this.arg);

        Expr base = Application.gatherApplicationArgs(this.base, application);

        state.current.state = 1;
        state.current.size = application.size();
        boolean processBase = state.visitor.prepareApplication(base, application.size());
        state.current.skippedRecursion = !processBase;

        state.stack.push(state.current);

        if (processBase) {
          state.stack.push(new ExprState(base, 0));
        }
        state.applicationStack.push(application);
      } else {
        LinkedList<Expr> application = state.applicationStack.poll();

        if (application.isEmpty()) {
          List<A> args = new ArrayList<>(state.current.size);
          for (int i = 0; i < state.current.size; i++) {
            args.add(state.valueStack.poll());
          }
          Collections.reverse(args);

          A base = null;
          if (!state.current.skippedRecursion) {
            base = state.valueStack.poll();
          }

          state.valueStack.push(state.visitor.onApplication(base, args));
        } else {
          state.stack.push(state.current);
          state.stack.push(new ExprState(application.poll(), 0));
          state.applicationStack.push(application);
        }
      }
    }

    private static final Expr gatherApplicationArgs(Expr candidate, Deque<Expr> args) {
      Expr current = candidate.getNonNote();

      while (current.tag == Tags.APPLICATION) {
        Constructors.Application currentApplication = (Constructors.Application) current;

        if (args != null) {
          args.push(currentApplication.arg);
        }
        current = currentApplication.base.getNonNote();
      }

      return current;
    }
  }

  static final class OperatorApplication extends Expr {
    final Operator operator;
    final Expr lhs;
    final Expr rhs;

    OperatorApplication(Operator operator, Expr lhs, Expr rhs) {
      super(Tags.OPERATOR_APPLICATION);
      this.operator = operator;
      this.lhs = lhs;
      this.rhs = rhs;
    }

    public final <A> A accept(ExternalVisitor<A> visitor) {
      return visitor.onOperatorApplication(this.operator, lhs, rhs);
    }

    final <A> void advance(VisitState<A> state) {
      if (state.current.state == 0) {
        state.visitor.prepareOperatorApplication(this.operator);
        state.current.state = 1;
        state.stack.push(state.current);
        state.stack.push(new ExprState(this.lhs, 0));
      } else if (state.current.state == 1) {
        state.current.state = 2;
        state.stack.push(state.current);
        state.stack.push(new ExprState(this.rhs, 0));
      } else {
        A v1 = state.valueStack.poll();
        A v0 = state.valueStack.poll();
        state.valueStack.push(state.visitor.onOperatorApplication(this.operator, v0, v1));
      }
    }
  }

  static final class If extends Expr {
    final Expr predicate;
    final Expr thenValue;
    final Expr elseValue;

    If(Expr predicate, Expr thenValue, Expr elseValue) {
      super(Tags.IF);
      this.predicate = predicate;
      this.thenValue = thenValue;
      this.elseValue = elseValue;
    }

    public final <A> A accept(ExternalVisitor<A> visitor) {
      return visitor.onIf(predicate, thenValue, elseValue);
    }

    final <A> void advance(VisitState<A> state) {
      if (state.current.state == 0) {
        state.visitor.prepareIf();
        state.current.state = 1;
        state.stack.push(state.current);
        state.stack.push(new ExprState(this.predicate, 0));
      } else if (state.current.state == 1) {
        state.current.state = 2;
        state.stack.push(state.current);
        state.stack.push(new ExprState(this.thenValue, 0));
      } else if (state.current.state == 2) {
        state.current.state = 3;
        state.stack.push(state.current);
        state.stack.push(new ExprState(this.elseValue, 0));
      } else {
        A v2 = state.valueStack.poll();
        A v1 = state.valueStack.poll();
        A v0 = state.valueStack.poll();
        state.valueStack.push(state.visitor.onIf(v0, v1, v2));
      }
    }
  }

  static final class Lambda extends Expr {
    final String name;
    final Expr type;
    final Expr result;

    Lambda(String name, Expr type, Expr result) {
      super(Tags.LAMBDA);
      this.name = name;
      this.type = type;
      this.result = result;
    }

    public final <A> A accept(ExternalVisitor<A> visitor) {
      return visitor.onLambda(this.name, type, result);
    }

    final <A> void advance(VisitState<A> state) {
      switch (state.current.state) {
        case 0:
          state.visitor.prepareLambda(this.name, this.type);
          state.current.state = 1;
          state.stack.push(state.current);
          state.stack.push(new ExprState(this.type, 0));
          break;
        case 1:
          state.visitor.bind(this.name, this.type);
          state.current.state = 2;
          state.stack.push(state.current);
          state.stack.push(new ExprState(this.result, 0));
          break;
        case 2:
          A v1 = state.valueStack.poll();
          A v0 = state.valueStack.poll();
          state.valueStack.push(state.visitor.onLambda(this.name, v0, v1));
          break;
      }
    }
  }

  static final class Pi extends Expr {
    final String name;
    final Expr type;
    final Expr result;

    Pi(String name, Expr type, Expr result) {
      super(Tags.PI);
      this.name = name;
      this.type = type;
      this.result = result;
    }

    public final <A> A accept(ExternalVisitor<A> visitor) {
      return visitor.onPi(this.name, type, result);
    }

    final <A> void advance(VisitState<A> state) {
      switch (state.current.state) {
        case 0:
          state.visitor.preparePi(this.name, this.type);
          state.current.state = 1;
          state.stack.push(state.current);
          state.stack.push(new ExprState(this.type, 0));
          break;
        case 1:
          state.visitor.bind(this.name, this.type);
          state.current.state = 2;
          state.stack.push(state.current);
          state.stack.push(new ExprState(this.result, 0));
          break;
        case 2:
          A v1 = state.valueStack.poll();
          A v0 = state.valueStack.poll();
          state.valueStack.push(state.visitor.onPi(this.name, v0, v1));
          break;
      }
    }
  }

  static final class Assert extends Expr {
    final Expr base;

    Assert(Expr base) {
      super(Tags.ASSERT);
      this.base = base;
    }

    public final <A> A accept(ExternalVisitor<A> visitor) {
      return visitor.onAssert(base);
    }

    final <A> void advance(VisitState<A> state) {
      if (state.current.state == 0) {
        state.visitor.prepareAssert();
        state.current.state = 1;
        state.stack.push(state.current);
        state.stack.push(new ExprState(this.base, 0));
      } else {
        state.valueStack.push(state.visitor.onAssert(state.valueStack.poll()));
      }
    }
  }

  static final class FieldAccess extends Expr {
    final Expr base;
    final String fieldName;

    FieldAccess(Expr base, String fieldName) {
      super(Tags.FIELD_ACCESS);
      this.base = base;
      this.fieldName = fieldName;
    }

    public final <A> A accept(ExternalVisitor<A> visitor) {
      return visitor.onFieldAccess(base, this.fieldName);
    }

    final <A> void advance(VisitState<A> state) {
      if (state.current.state == 0) {
        if (state.visitor.prepareFieldAccess(this.base, this.fieldName)) {
          state.current.state = 1;
          state.stack.push(state.current);
          state.stack.push(new ExprState(this.base, 0));
        } else {
          state.valueStack.push(state.visitor.onFieldAccess(null, this.fieldName));
        }
      } else {
        state.valueStack.push(state.visitor.onFieldAccess(state.valueStack.poll(), this.fieldName));
      }
    }
  }

  static final class Projection extends Expr {
    final Expr base;
    final String[] fieldNames;

    Projection(Expr base, String[] fieldNames) {
      super(Tags.PROJECTION);
      this.base = base;
      this.fieldNames = fieldNames;
    }

    public final <A> A accept(ExternalVisitor<A> visitor) {
      return visitor.onProjection(base, this.fieldNames);
    }

    final <A> void advance(VisitState<A> state) {
      if (state.current.state == 0) {
        state.visitor.prepareProjection(this.fieldNames.length);
        state.current.state = 1;
        state.stack.push(state.current);
        state.stack.push(new ExprState(this.base, 0));
      } else {
        state.valueStack.push(state.visitor.onProjection(state.valueStack.poll(), this.fieldNames));
      }
    }
  }

  static final class ProjectionByType extends Expr {
    final Expr base;
    final Expr type;

    ProjectionByType(Expr base, Expr type) {
      super(Tags.PROJECTION_BY_TYPE);
      this.base = base;
      this.type = type;
    }

    public final <A> A accept(ExternalVisitor<A> visitor) {
      return visitor.onProjectionByType(base, type);
    }

    final <A> void advance(VisitState<A> state) {
      if (state.current.state == 0) {
        state.visitor.prepareProjectionByType();
        state.current.state = 1;
        state.stack.push(state.current);
        state.stack.push(new ExprState(this.base, 0));
      } else if (state.current.state == 1) {
        state.visitor.prepareProjectionByType(this.type);
        state.current.state = 2;
        state.stack.push(state.current);
        state.stack.push(new ExprState(this.type, 0));
      } else {
        A v1 = state.valueStack.poll();
        A v0 = state.valueStack.poll();
        state.valueStack.push(state.visitor.onProjectionByType(v0, v1));
      }
    }
  }

  static final class BuiltIn extends Expr {
    final String name;

    BuiltIn(String name) {
      super(Tags.BUILT_IN);
      this.name = name;
    }

    public final <A> A accept(ExternalVisitor<A> visitor) {
      return visitor.onBuiltIn(this.name);
    }

    final <A> void advance(VisitState<A> state) {
      state.valueStack.push(state.visitor.onBuiltIn(this, this.name));
    }
  }

  static final class Identifier extends Expr {
    final String name;
    final long index;

    Identifier(String name, long index) {
      super(Tags.IDENTIFIER);
      this.name = name;
      this.index = index;
    }

    public final <A> A accept(ExternalVisitor<A> visitor) {
      return visitor.onIdentifier(this.name, this.index);
    }

    final <A> void advance(VisitState<A> state) {
      state.valueStack.push(state.visitor.onIdentifier(this, this.name, this.index));
    }
  }

  static final class RecordLiteral extends Expr {
    final Entry<String, Expr>[] fields;

    RecordLiteral(Entry<String, Expr>[] fields) {
      super(Tags.RECORD);
      this.fields = fields;
    }

    public final <A> A accept(ExternalVisitor<A> visitor) {
      return visitor.onRecord(new ArrayIterable<Entry<String, Expr>>(fields), fields.length);
    }

    final <A> void advance(VisitState<A> state) {
      if (state.current.state == 0) {
        state.visitor.prepareRecord(this.fields.length);
        if (this.fields.length == 0) {
          state.valueStack.push(state.visitor.onRecord(new ArrayList<Entry<String, A>>()));
        } else {
          state.current =
              new ExprState(state.current.expr, 1, this.fields, state.visitor.sortFields());
          state.stack.push(state.current);

          Entry<String, Expr> field = state.current.sortedFields[state.current.state - 1];
          state.visitor.prepareRecordField(
              field.getKey(), field.getValue(), state.current.state - 1);
          state.stack.push(new ExprState(field.getValue(), 0));
        }
      } else if (state.current.state == state.current.sortedFields.length) {
        List<Entry<String, A>> results = new ArrayList<Entry<String, A>>();
        for (int i = state.current.sortedFields.length - 1; i >= 0; i -= 1) {
          results.add(
              new SimpleImmutableEntry<>(
                  state.current.sortedFields[i].getKey(), state.valueStack.poll()));
        }
        Collections.reverse(results);
        state.valueStack.push(state.visitor.onRecord(results));
      } else {
        state.current.state += 1;
        Entry<String, Expr> field = state.current.sortedFields[state.current.state - 1];

        state.visitor.prepareRecordField(field.getKey(), field.getValue(), state.current.state - 1);

        state.stack.push(state.current);
        state.stack.push(new ExprState(field.getValue(), 0));
      }
    }
  }

  static final class RecordType extends Expr {
    final Entry<String, Expr>[] fields;

    RecordType(Entry<String, Expr>[] fields) {
      super(Tags.RECORD_TYPE);
      this.fields = fields;
    }

    public final <A> A accept(ExternalVisitor<A> visitor) {
      return visitor.onRecordType(new ArrayIterable<Entry<String, Expr>>(fields), fields.length);
    }

    final <A> void advance(VisitState<A> state) {
      if (state.current.state == 0) {
        state.visitor.prepareRecordType(this.fields.length);
        if (this.fields.length == 0) {
          state.valueStack.push(state.visitor.onRecordType(new ArrayList<Entry<String, A>>()));
        } else {
          state.current =
              new ExprState(state.current.expr, 1, this.fields, state.visitor.sortFields());
          state.stack.push(state.current);

          Entry<String, Expr> field = state.current.sortedFields[state.current.state - 1];
          state.visitor.prepareRecordTypeField(
              field.getKey(), field.getValue(), state.current.state - 1);
          state.stack.push(new ExprState(field.getValue(), 0));
        }
      } else if (state.current.state == state.current.sortedFields.length) {
        List<Entry<String, A>> results = new ArrayList<Entry<String, A>>();
        for (int i = state.current.sortedFields.length - 1; i >= 0; i -= 1) {
          results.add(
              new SimpleImmutableEntry<>(
                  state.current.sortedFields[i].getKey(), state.valueStack.poll()));
        }
        Collections.reverse(results);
        state.valueStack.push(state.visitor.onRecordType(results));
      } else {
        state.current.state += 1;
        Entry<String, Expr> field = state.current.sortedFields[state.current.state - 1];

        state.visitor.prepareRecordTypeField(
            field.getKey(), field.getValue(), state.current.state - 1);

        state.stack.push(state.current);
        state.stack.push(new ExprState(field.getValue(), 0));
      }
    }
  }

  static final class UnionType extends Expr {
    final Entry<String, Expr>[] fields;

    UnionType(Entry<String, Expr>[] fields) {
      super(Tags.UNION_TYPE);
      this.fields = fields;
    }

    public final <A> A accept(ExternalVisitor<A> visitor) {
      return visitor.onUnionType(new ArrayIterable<Entry<String, Expr>>(fields), fields.length);
    }

    final <A> void advance(VisitState<A> state) {
      if (state.current.state == 0) {
        state.visitor.prepareUnionType(this.fields.length);
        if (this.fields.length == 0) {
          state.valueStack.push(state.visitor.onUnionType(new ArrayList<Entry<String, A>>()));
        } else {
          state.current =
              new ExprState(state.current.expr, 1, this.fields, state.visitor.sortFields());
          state.stack.push(state.current);

          Entry<String, Expr> field = state.current.sortedFields[state.current.state - 1];
          state.visitor.prepareUnionTypeField(
              field.getKey(), field.getValue(), state.current.state - 1);

          Expr type = field.getValue();
          if (type == null) {
            state.valueStack.push(null);
          } else {
            state.stack.push(new ExprState(type, 0));
          }
        }
      } else if (state.current.state == this.fields.length) {
        List<Entry<String, A>> results = new ArrayList<Entry<String, A>>();
        for (int i = state.current.sortedFields.length - 1; i >= 0; i -= 1) {
          results.add(
              new SimpleImmutableEntry<>(
                  state.current.sortedFields[i].getKey(), state.valueStack.poll()));
        }
        Collections.reverse(results);
        state.valueStack.push(state.visitor.onUnionType(results));
      } else {
        state.current.state += 1;

        Entry<String, Expr> field = state.current.sortedFields[state.current.state - 1];
        Expr type = field.getValue();

        state.visitor.prepareUnionTypeField(field.getKey(), type, state.current.state - 1);

        state.stack.push(state.current);
        if (type == null) {
          state.valueStack.push(null);
        } else {
          state.stack.push(new ExprState(type, 0));
        }
      }
    }
  }

  static final class NonEmptyListLiteral extends Expr {
    final Expr[] values;

    NonEmptyListLiteral(Expr[] values) {
      super(Tags.NON_EMPTY_LIST);
      this.values = values;
    }

    public final <A> A accept(ExternalVisitor<A> visitor) {
      return visitor.onNonEmptyList(new ArrayIterable<Expr>(values), this.values.length);
    }

    final <A> void advance(VisitState<A> state) {
      if (state.current.state == 0) {
        boolean done = false;
        if (state.visitor.flattenToMapLists()) {
          Expr asRecord = NonEmptyListLiteral.flattenToMapList(this.values);

          if (asRecord != null) {
            state.stack.push(new ExprState(asRecord, 0));
            done = true;
          }
        }

        if (!done) {
          state.visitor.prepareNonEmptyList(this.values.length);
          state.visitor.prepareNonEmptyListElement(0);
          state.current.state = 1;
          state.stack.push(state.current);
          state.stack.push(new ExprState(this.values[state.current.state - 1], 0));
        }
      } else if (state.current.state == this.values.length) {
        List<A> results = new ArrayList<A>();
        for (int i = 0; i < this.values.length; i += 1) {
          results.add(state.valueStack.poll());
        }
        Collections.reverse(results);
        state.valueStack.push(state.visitor.onNonEmptyList(results));
      } else {
        state.visitor.prepareNonEmptyListElement(state.current.state);
        state.current.state += 1;
        state.stack.push(state.current);
        state.stack.push(new ExprState(this.values[state.current.state - 1], 0));
      }
    }

    private static final Expr flattenToMapList(Expr[] values) {
      LinkedHashMap<String, Expr> fieldMap = new LinkedHashMap<>(values.length);

      for (Expr value : values) {
        List<Entry<String, Expr>> asRecord = Expr.Util.asRecordLiteral(value);

        if (asRecord == null) {
          return null;
        }

        Entry<Expr, Expr> entry = Expr.Util.flattenToMapRecord(asRecord);

        if (entry == null) {
          return null;
        }

        String asText = Expr.Util.asSimpleTextLiteral(entry.getKey());

        if (asText == null) {
          return null;
        }

        fieldMap.put(asText, entry.getValue());
      }

      Set<Entry<String, Expr>> fields = fieldMap.entrySet();

      return Expr.makeRecordLiteral(fields.toArray(new Entry[fields.size()]));
    }
  }

  static final class EmptyListLiteral extends Expr {
    final Expr type;

    EmptyListLiteral(Expr type) {
      super(Tags.EMPTY_LIST);
      this.type = type;
    }

    public final <A> A accept(ExternalVisitor<A> visitor) {
      return visitor.onEmptyList(type);
    }

    final <A> void advance(VisitState<A> state) {
      if (state.current.state == 0) {
        if (state.visitor.flattenToMapLists() && EmptyListLiteral.isToMapListType(this.type)) {
          state.stack.push(new ExprState(Expr.Constants.EMPTY_RECORD_LITERAL, 0));
        } else {
          if (state.visitor.prepareEmptyList(this.type)) {
            state.current.state = 1;
            state.stack.push(state.current);
            state.stack.push(new ExprState(this.type, 0));
          } else {
            state.valueStack.push(null);
          }
        }
      } else {
        state.valueStack.push(state.visitor.onEmptyList(state.valueStack.poll()));
      }
    }

    private static final boolean isToMapListType(Expr type) {
      Expr elementType = Expr.Util.getListArg(type);

      if (elementType == null) {
        return false;
      } else {
        List<Entry<String, Expr>> asRecordType = Expr.Util.asRecordType(elementType);

        if (asRecordType == null) {
          return false;
        }

        Entry<Expr, Expr> entry = Expr.Util.flattenToMapRecord(asRecordType);

        if (entry == null) {
          return false;
        }

        String asBuiltIn = Expr.Util.asBuiltIn(entry.getKey());

        return asBuiltIn != null && asBuiltIn.equals("Text");
      }
    }
  }

  static final class Let extends Expr {
    final String name;
    final Expr type;
    final Expr value;
    final Expr body;

    Let(String name, Expr type, Expr value, Expr body) {
      super(Tags.LET);
      this.name = name;
      this.type = type;
      this.value = value;
      this.body = body;
    }

    public final <A> A accept(ExternalVisitor<A> visitor) {
      return visitor.onLet(name, type, value, body);
    }

    final <A> void advance(VisitState<A> state) {
      List<LetBinding<Expr>> letBindings;

      if (state.current.state == 0) {
        letBindings = new ArrayList<LetBinding<Expr>>();
        letBindings.add(new LetBinding(this.name, this.type, this.value));

        Let.gatherLetBindings(this.body, letBindings);

        state.current.state = 1;
        state.current.size = letBindings.size();

        List<String> letBindingNames = new ArrayList<>(state.current.size);

        for (LetBinding<Expr> letBinding : letBindings) {
          letBindingNames.add(letBinding.getName());
        }

        state.letBindingNamesStack.push(letBindingNames);

        state.visitor.prepareLet(letBindings.size());
      } else {
        letBindings = state.letBindingsStack.poll();
      }

      if (letBindings.isEmpty()) {
        if (state.current.state == 1) {
          state.current.state = 3;
          state.stack.push(state.current);
          state.stack.push(new ExprState(Let.gatherLetBindings(this.body, null), 0));
          state.letBindingsStack.push(letBindings);
        } else {
          List<String> letBindingNames = state.letBindingNamesStack.poll();
          LinkedList<LetBinding<A>> valueBindings = new LinkedList<LetBinding<A>>();

          A body = state.valueStack.poll();

          for (int i = 0; i < state.current.size; i++) {
            A v1 = state.valueStack.poll();
            A v0 = state.valueStack.poll();

            valueBindings.push(
                new LetBinding(letBindingNames.get(state.current.size - 1 - i), v0, v1));
          }

          state.valueStack.push(state.visitor.onLet(valueBindings, body));
        }
      } else {
        LetBinding<Expr> letBinding = letBindings.get(0);

        switch (state.current.state) {
          case 1:
            state.current.state = 2;
            state.visitor.prepareLetBinding(letBinding.getName(), letBinding.getType());
            if (letBinding.hasType()) {
              state.stack.push(state.current);
              state.stack.push(new ExprState(letBinding.getType(), 0));
              state.letBindingsStack.push(letBindings);
              break;
            } else {
              state.valueStack.push(null);
            }
          case 2:
            state.current.state = 1;
            state.visitor.bind(letBinding.getName(), letBinding.getType());
            state.stack.push(state.current);
            state.stack.push(new ExprState(letBinding.getValue(), 0));
            letBindings.remove(0);
            state.letBindingsStack.push(letBindings);
            break;
        }
      }
    }

    private static final Expr gatherLetBindings(Expr candidate, List<LetBinding<Expr>> args) {
      Expr current = candidate.getNonNote();

      while (current.tag == Tags.LET) {
        Constructors.Let currentLet = (Constructors.Let) current;

        if (args != null) {
          args.add(new LetBinding(currentLet.name, currentLet.type, currentLet.value));
        }
        current = currentLet.body.getNonNote();
      }

      return current;
    }
  }

  static final class Annotated extends Expr {
    final Expr base;
    final Expr type;

    Annotated(Expr base, Expr type) {
      super(Tags.ANNOTATED);
      this.base = base;
      this.type = type;
    }

    public final <A> A accept(ExternalVisitor<A> visitor) {
      return visitor.onAnnotated(base, type);
    }

    final <A> void advance(VisitState<A> state) {
      if (state.current.state == 0) {
        state.visitor.prepareAnnotated(this.type);
        state.current.state = 1;
        state.stack.push(state.current);
        state.stack.push(new ExprState(this.base, 0));
      } else if (state.current.state == 1) {
        state.current.state = 2;
        state.stack.push(state.current);
        state.stack.push(new ExprState(this.type, 0));
      } else {
        A v1 = state.valueStack.poll();
        A v0 = state.valueStack.poll();
        state.valueStack.push(state.visitor.onAnnotated(v0, v1));
      }
    }
  }

  static final class Merge extends Expr {
    final Expr handlers;
    final Expr union;
    final Expr type;

    Merge(Expr handlers, Expr union, Expr type) {
      super(Tags.MERGE);
      this.handlers = handlers;
      this.union = union;
      this.type = type;
    }

    public final <A> A accept(ExternalVisitor<A> visitor) {
      return visitor.onMerge(handlers, union, type);
    }

    final <A> void advance(VisitState<A> state) {
      switch (state.current.state) {
        case 0:
          state.visitor.prepareMerge(this.type);
          state.current.state = 1;
          state.stack.push(state.current);
          state.stack.push(new ExprState(this.handlers, 0));
          break;
        case 1:
          state.current.state = 2;
          state.stack.push(state.current);
          state.stack.push(new ExprState(this.union, 0));
          break;
        case 2:
          state.current.state = 3;

          if (this.type != null) {
            state.stack.push(state.current);
            state.stack.push(new ExprState(this.type, 0));
            break;
          } else {
            state.valueStack.push(null);
          }
        case 3:
          A v2 = state.valueStack.poll();
          A v1 = state.valueStack.poll();
          A v0 = state.valueStack.poll();
          state.valueStack.push(state.visitor.onMerge(v0, v1, v2));

          break;
      }
    }
  }

  static final class ToMap extends Expr {
    final Expr base;
    final Expr type;

    ToMap(Expr base, Expr type) {
      super(Tags.TO_MAP);
      this.base = base;
      this.type = type;
    }

    public final <A> A accept(ExternalVisitor<A> visitor) {
      return visitor.onToMap(base, type);
    }

    final <A> void advance(VisitState<A> state) {
      switch (state.current.state) {
        case 0:
          state.visitor.prepareToMap(this.type);
          state.current.state = 1;
          state.stack.push(state.current);
          state.stack.push(new ExprState(this.base, 0));
          break;
        case 1:
          state.current.state = 2;

          if (this.type != null) {
            state.stack.push(state.current);
            state.stack.push(new ExprState(this.type, 0));
            break;
          } else {
            state.valueStack.push(null);
          }
        case 2:
          A v1 = state.valueStack.poll();
          A v0 = state.valueStack.poll();
          state.valueStack.push(state.visitor.onToMap(v0, v1));
          break;
      }
    }
  }

  static final class With extends Expr {
    final Expr base;
    final String[] path;
    final Expr value;

    With(Expr base, String[] path, Expr value) {
      super(Tags.WITH);
      this.base = base;
      this.path = path;
      this.value = value;
    }

    public final <A> A accept(ExternalVisitor<A> visitor) {
      return visitor.onWith(this.base, this.path, this.value);
    }

    final <A> void advance(VisitState<A> state) {
      switch (state.current.state) {
        case 0:
          state.visitor.prepareWith(this.path);
          state.current.state = 1;
          state.stack.push(state.current);
          state.stack.push(new ExprState(this.base, 0));
          break;
        case 1:
          state.visitor.prepareWithValue(this.path);
          state.current.state = 2;
          state.stack.push(state.current);
          state.stack.push(new ExprState(this.value, 0));
          break;
        case 2:
          A v1 = state.valueStack.poll();
          A v0 = state.valueStack.poll();
          state.valueStack.push(state.visitor.onWith(v0, this.path, v1));
          break;
      }
    }
  }

  static final class MissingImport extends Expr {
    final Expr.ImportMode mode;
    final byte[] hash;

    MissingImport(Expr.ImportMode mode, byte[] hash) {
      super(Tags.MISSING_IMPORT);
      this.mode = mode;
      this.hash = hash;
    }

    public final <A> A accept(ExternalVisitor<A> visitor) {
      return visitor.onMissingImport(this.mode, this.hash);
    }

    final <A> void advance(VisitState<A> state) {
      state.valueStack.push(state.visitor.onMissingImport(this.mode, this.hash));
    }
  }

  static final class EnvImport extends Expr {
    final String name;
    final Expr.ImportMode mode;
    final byte[] hash;

    EnvImport(String name, Expr.ImportMode mode, byte[] hash) {
      super(Tags.ENV_IMPORT);
      this.name = name;
      this.mode = mode;
      this.hash = hash;
    }

    public final <A> A accept(ExternalVisitor<A> visitor) {
      return visitor.onEnvImport(this.name, this.mode, this.hash);
    }

    final <A> void advance(VisitState<A> state) {
      state.valueStack.push(state.visitor.onEnvImport(this.name, this.mode, this.hash));
    }
  }

  static final class LocalImport extends Expr {
    final Path path;
    final Expr.ImportMode mode;
    final byte[] hash;

    LocalImport(Path path, Expr.ImportMode mode, byte[] hash) {
      super(Tags.LOCAL_IMPORT);
      this.path = path;
      this.mode = mode;
      this.hash = hash;
    }

    public final <A> A accept(ExternalVisitor<A> visitor) {
      return visitor.onLocalImport(this.path, this.mode, this.hash);
    }

    final <A> void advance(VisitState<A> state) {
      state.valueStack.push(state.visitor.onLocalImport(this.path, this.mode, this.hash));
    }
  }

  static final class ClasspathImport extends Expr {
    final Path path;
    final Expr.ImportMode mode;
    final byte[] hash;

    ClasspathImport(Path path, Expr.ImportMode mode, byte[] hash) {
      super(Tags.CLASSPATH_IMPORT);
      this.path = path;
      this.mode = mode;
      this.hash = hash;
    }

    public final <A> A accept(ExternalVisitor<A> visitor) {
      return visitor.onClasspathImport(this.path, this.mode, this.hash);
    }

    final <A> void advance(VisitState<A> state) {
      state.valueStack.push(state.visitor.onClasspathImport(this.path, this.mode, this.hash));
    }
  }

  static final class RemoteImport extends Expr {
    final URI url;
    final Expr using;
    final Expr.ImportMode mode;
    final byte[] hash;

    RemoteImport(URI url, Expr using, Expr.ImportMode mode, byte[] hash) {
      super(Tags.REMOTE_IMPORT);
      this.url = url;
      this.using = using;
      this.mode = mode;
      this.hash = hash;
    }

    public final <A> A accept(ExternalVisitor<A> visitor) {
      return visitor.onRemoteImport(this.url, this.using, this.mode, this.hash);
    }

    final <A> void advance(VisitState<A> state) {
      switch (state.current.state) {
        case 0:
          state.visitor.prepareRemoteImport(this.url, this.using, this.mode, this.hash);
          state.current.state = 1;

          if (this.using != null) {
            state.stack.push(state.current);
            state.stack.push(new ExprState(this.using, 0));
            break;
          } else {
            state.valueStack.push(null);
          }
        case 1:
          state.valueStack.push(
              state.visitor.onRemoteImport(
                  this.url, state.valueStack.poll(), this.mode, this.hash));
      }
    }
  }
}
