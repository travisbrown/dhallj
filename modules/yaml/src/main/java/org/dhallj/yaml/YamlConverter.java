package org.dhallj.yaml;

import org.dhallj.core.Expr;
import org.dhallj.core.converters.JsonConverter;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class YamlConverter {
  private static final DumperOptions defaultOptions = new DumperOptions();

  static {
    defaultOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
  }

  public static final String toYamlString(Expr expr) {
    return toYamlString(expr, defaultOptions, true);
  }

  public static final String toYamlString(Expr expr, DumperOptions options) {
    return toYamlString(expr, options, true);
  }

  public static final String toYamlString(Expr expr, boolean skipNulls) {
    return toYamlString(expr, defaultOptions, skipNulls);
  }

  public static final String toYamlString(Expr expr, DumperOptions options, boolean skipNulls) {
    YamlHandler handler = new YamlHandler(skipNulls);
    boolean wasConverted = expr.accept(new JsonConverter(handler));

    if (wasConverted) {
      Yaml yaml = new Yaml(options);

      return yaml.dump(handler.getResult());
    } else {
      return null;
    }
  }
}
