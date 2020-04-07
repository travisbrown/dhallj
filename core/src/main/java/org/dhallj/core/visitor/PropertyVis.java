package org.dhallj.core.visitor;

import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.Map.Entry;
import org.dhallj.core.Expr;
import org.dhallj.core.Import;
import org.dhallj.core.LetBinding;
import org.dhallj.core.Operator;
import org.dhallj.core.Vis;

public class PropertyVis extends ConstantVis<Boolean> {
  public PropertyVis() {
    super(true);
  }

  public Boolean onLambda(String name, Boolean type, Boolean result) {
    return type && result;
  }

  public Boolean onPi(String name, Boolean type, Boolean result) {
    return type && result;
  }

  public Boolean onLet(List<LetBinding<Boolean>> bindings, Boolean body) {
    for (LetBinding<Boolean> binding : bindings) {
      if (!binding.getValue() || (binding.hasType() && !binding.getType())) {
        return false;
      }
    }
    return true;
  }

  public Boolean onText(String[] parts, List<Boolean> interpolated) {
    for (Boolean value : interpolated) {
      if (!value) {
        return false;
      }
    }
    return true;
  }

  public Boolean onNonEmptyList(List<Boolean> values) {
    for (Boolean value : values) {
      if (!value) {
        return false;
      }
    }
    return true;
  }

  public Boolean onEmptyList(Expr typeExpr, Boolean type) {
    return type;
  }

  public Boolean onRecord(List<Entry<String, Boolean>> fields) {
    for (Entry<String, Boolean> entry : fields) {
      if (!entry.getValue()) {
        return false;
      }
    }
    return true;
  }

  public Boolean onRecordType(List<Entry<String, Boolean>> fields) {
    for (Entry<String, Boolean> entry : fields) {
      if (!entry.getValue()) {
        return false;
      }
    }
    return true;
  }

  public Boolean onUnionType(List<Entry<String, Boolean>> fields) {
    for (Entry<String, Boolean> entry : fields) {
      if (entry.getValue() != null && !entry.getValue()) {
        return false;
      }
    }
    return true;
  }

  public Boolean onFieldAccess(Boolean base, String fieldName) {
    return base;
  }

  public Boolean onProjection(Boolean base, String[] fieldNames) {
    return base;
  }

  public Boolean onProjectionByType(Boolean base, Boolean type) {
    return base && type;
  }

  public Boolean onApplication(Expr baseExpr, Boolean base, List<Boolean> args) {
    if (!base) {
      return false;
    }
    for (Boolean value : args) {
      if (!value) {
        return false;
      }
    }
    return true;
  }

  public Boolean onOperatorApplication(Operator operator, Boolean lhs, Boolean rhs) {
    return lhs && rhs;
  }

  public Boolean onIf(Boolean predicate, Boolean thenValue, Boolean elseValue) {
    return predicate && thenValue && elseValue;
  }

  public Boolean onAnnotated(Boolean base, Boolean type) {
    return base && type;
  }

  public Boolean onAssert(Boolean base) {
    return base;
  }

  public Boolean onMerge(Boolean handlers, Boolean union, Boolean type) {
    return handlers && union && (type == null || type);
  }

  public Boolean onToMap(Boolean base, Boolean type) {
    return base && (type == null || type);
  }

  public Boolean onLocalImport(Path path, Import.Mode mode, byte[] hash) {
    return true;
  }

  public Boolean onRemoteImport(URI url, Boolean using, Import.Mode mode, byte[] hash) {
    return true;
  }

  public Boolean onEnvImport(String value, Import.Mode mode, byte[] hash) {
    return true;
  }

  public Boolean onMissingImport(Import.Mode mode, byte[] hash) {
    return true;
  }
}
