package org.dhallj.core.typechecking;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.dhallj.core.Expr;
import org.dhallj.core.Expr.Constants;

final class BuiltInTypes {
  static final Expr getType(String name) {
    return mappings.get(name);
  }

  private static final int SIZE = 34;
  private static final Map<String, Expr> mappings = new HashMap<>(SIZE);

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

    Expr textToText = Expr.makePi(Constants.TEXT, Constants.TEXT);

    mappings.put("Kind", Constants.SORT);
    mappings.put("Type", Constants.KIND);
    mappings.put("Bool", Constants.TYPE);
    mappings.put("True", Constants.BOOL);
    mappings.put("False", Constants.BOOL);
    mappings.put("Natural", Constants.TYPE);
    mappings.put("Integer", Constants.TYPE);
    mappings.put("Text", Constants.TYPE);
    mappings.put("Double", Constants.TYPE);
    mappings.put("List", typeToType);
    mappings.put("Optional", typeToType);
    mappings.put(
        "None",
        Expr.makePi(
            "A",
            Constants.TYPE,
            Expr.makeApplication(Constants.OPTIONAL, Expr.makeIdentifier("A"))));

    mappings.put(
        "Text/replace", Expr.makePi(Constants.TEXT, Expr.makePi(Constants.TEXT, textToText)));
    mappings.put("Text/show", textToText);

    mappings.put("Natural/build", Expr.makePi(naturalType, Constants.NATURAL));
    mappings.put("Natural/fold", Expr.makePi(Constants.NATURAL, naturalType));
    mappings.put("Natural/isZero", naturalToBool);
    mappings.put("Natural/even", naturalToBool);
    mappings.put("Natural/odd", naturalToBool);
    mappings.put("Natural/toInteger", Expr.makePi(Constants.NATURAL, Constants.INTEGER));
    mappings.put("Natural/show", Expr.makePi(Constants.NATURAL, Constants.TEXT));
    mappings.put("Natural/subtract", Expr.makePi(Constants.NATURAL, naturalToNatural));

    mappings.put("Integer/show", Expr.makePi(Constants.INTEGER, Constants.TEXT));
    mappings.put("Integer/toDouble", Expr.makePi(Constants.INTEGER, Constants.DOUBLE));
    mappings.put("Integer/negate", Expr.makePi(Constants.INTEGER, Constants.INTEGER));
    mappings.put("Integer/clamp", Expr.makePi(Constants.INTEGER, Constants.NATURAL));

    mappings.put("Double/show", Expr.makePi(Constants.DOUBLE, Constants.TEXT));

    mappings.put("List/build", Expr.makePi("a", Constants.TYPE, Expr.makePi(listType, listA)));
    mappings.put("List/fold", Expr.makePi("a", Constants.TYPE, Expr.makePi(listA, listType)));
    mappings.put(
        "List/length",
        Expr.makePi("a", Expr.Constants.TYPE, Expr.makePi(listA, Constants.NATURAL)));
    mappings.put("List/head", listAToOptionalA);
    mappings.put("List/last", listAToOptionalA);

    Entry[] indexedRecordFields = {
      new SimpleImmutableEntry<>("index", Constants.NATURAL),
      new SimpleImmutableEntry<>("value", _a)
    };

    mappings.put(
        "List/indexed",
        Expr.makePi(
            "a",
            Constants.TYPE,
            Expr.makePi(
                listA,
                Expr.makeApplication(Constants.LIST, Expr.makeRecordType(indexedRecordFields)))));
    mappings.put("List/reverse", Expr.makePi("a", Constants.TYPE, Expr.makePi(listA, listA)));

    mappings.put("Some", Constants.SOME);
  }
}
