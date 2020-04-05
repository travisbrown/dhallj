package org.dhallj.core.properties;

import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.Map.Entry;
import org.dhallj.core.Expr;
import org.dhallj.core.Import;
import org.dhallj.core.LetBinding;
import org.dhallj.core.Operator;
import org.dhallj.core.Vis;
import org.dhallj.core.visitor.ConstantVis;

public final class IsResolved extends ConstantVis<Boolean> {
  public static final Vis<Boolean> instance = new IsResolved();

  IsResolved() {
    super(true);
  }

  @Override
  public Boolean onLambda(String name, Boolean type, Boolean result) {
    return type && result;
  }

  @Override
  public Boolean onPi(String name, Boolean type, Boolean result) {
    return type && result;
  }

  @Override
  public Boolean onLet(List<LetBinding<Boolean>> bindings, Boolean body) {
    for (LetBinding<Boolean> binding : bindings) {
      if (!binding.getValue() || (binding.hasType() && !binding.getType())) {
        return false;
      }
    }
    return true;
  }

  @Override
  public Boolean onText(String[] parts, List<Boolean> interpolated) {
    for (Boolean value : interpolated) {
      if (!value) {
        return false;
      }
    }
    return true;
  }

  @Override
  public Boolean onNonEmptyList(List<Boolean> values) {
    for (Boolean value : values) {
      if (!value) {
        return false;
      }
    }
    return true;
  }

  @Override
  public Boolean onEmptyList(Expr typeExpr, Boolean type) {
    return type;
  }

  @Override
  public Boolean onRecord(List<Entry<String, Boolean>> fields) {
    for (Entry<String, Boolean> entry : fields) {
      if (!entry.getValue()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public Boolean onRecordType(List<Entry<String, Boolean>> fields) {
    for (Entry<String, Boolean> entry : fields) {
      if (!entry.getValue()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public Boolean onUnionType(List<Entry<String, Boolean>> fields) {
    for (Entry<String, Boolean> entry : fields) {
      if (entry.getValue() != null && !entry.getValue()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public Boolean onFieldAccess(Boolean base, String fieldName) {
    return base;
  }

  @Override
  public Boolean onProjection(Boolean base, String[] fieldNames) {
    return base;
  }

  @Override
  public Boolean onProjectionByType(Boolean base, Boolean type) {
    return base && type;
  }

  @Override
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

  @Override
  public Boolean onOperatorApplication(Operator operator, Boolean lhs, Boolean rhs) {
    return lhs && rhs;
  }

  @Override
  public Boolean onIf(Boolean predicate, Boolean thenValue, Boolean elseValue) {
    return predicate && thenValue && elseValue;
  }

  @Override
  public Boolean onAnnotated(Boolean base, Boolean type) {
    return base && type;
  }

  @Override
  public Boolean onAssert(Boolean base) {
    return base;
  }

  @Override
  public Boolean onMerge(Boolean handlers, Boolean union, Boolean type) {
    return handlers && union && (type == null || type);
  }

  @Override
  public Boolean onToMap(Boolean base, Boolean type) {
    return base && (type == null || type);
  }

  @Override
  public Boolean onLocalImport(Path path, Import.Mode mode, byte[] hash) {
    return false;
  }

  @Override
  public Boolean onRemoteImport(URI url, Boolean using, Import.Mode mode, byte[] hash) {
    return false;
  }

  @Override
  public Boolean onEnvImport(String value, Import.Mode mode, byte[] hash) {
    return false;
  }

  @Override
  public Boolean onMissingImport(Import.Mode mode, byte[] hash) {
    return false;
  }
}
