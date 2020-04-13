package org.dhallj.yaml;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

interface YamlContext {
  void add(String key);

  void add(Object value);

  Object getResult();
}

final class RootContext implements YamlContext {
  private final Object result;

  RootContext(Object result) {
    this.result = result;
  }

  public void add(String key) {}

  public void add(Object value) {}

  public Object getResult() {
    return this.result;
  }
}

final class ObjectContext implements YamlContext {
  private String key = null;
  private final Map<String, Object> fields = new LinkedHashMap<>();
  private final boolean skipNulls;

  public ObjectContext(boolean skipNulls) {
    this.skipNulls = skipNulls;
  }

  public void add(String key) {
    this.key = key;
  }

  public void add(Object value) {
    if (!skipNulls || value != null) {
      this.fields.put(this.key, value);
    } else {
      this.key = null;
    }
  }

  public Object getResult() {
    return this.fields;
  }
}

final class ArrayContext implements YamlContext {
  private final List<Object> values = new ArrayList<>();

  public void add(String key) {}

  public void add(Object value) {
    this.values.add(value);
  }

  public Object getResult() {
    return this.values;
  }
}
