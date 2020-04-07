package org.dhallj.core.typechecking;

import java.net.URI;
import java.nio.file.Path;
import org.dhallj.core.Import;
import org.dhallj.core.Vis;
import org.dhallj.core.visitor.PropertyVis;

public final class NonNegativeIndices extends PropertyVis {
  public static final Vis<Boolean> instance = new NonNegativeIndices();

  @Override
  public Boolean onIdentifier(String value, long index) {
    return index >= 0;
  }
}
