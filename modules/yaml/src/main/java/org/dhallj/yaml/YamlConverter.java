package org.dhallj.yaml;

import org.dhallj.core.Expr;
import org.dhallj.core.converters.JsonConverter;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions;

public class YamlConverter {
  private static final DumperOptions defaultOptions = new DumperOptions();

  static {
    defaultOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
  }

  public static final String toYamlString(Expr expr) {
    return toYamlString(expr, defaultOptions);
  }

  public static final String toYamlString(Expr expr, DumperOptions options) {
    YamlHandler handler = new YamlHandler();
    boolean wasConverted = expr.accept(new JsonConverter(handler));

    if (wasConverted) {
      Yaml yaml = new Yaml(options);
      return yaml.dump(handler.getResult());
    } else {
      return null;
    }
  }
}
