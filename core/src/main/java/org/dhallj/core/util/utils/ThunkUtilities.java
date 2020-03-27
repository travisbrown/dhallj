package org.dhallj.core.util;

import java.util.ArrayList;
import java.util.List;
import org.dhallj.core.Expr;
import org.dhallj.core.Thunk;

/**
 * Utility methods related to thunks that are useful across packages.
 *
 * <p>This shouldn't exist.
 */
public final class ThunkUtilities {
  public static Expr[] exprsToArray(Iterable<Thunk<Expr>> thunks) {
    List<Expr> result = new ArrayList();

    for (Thunk<Expr> thunk : thunks) {
      result.add(thunk.apply());
    }

    return result.toArray(new Expr[result.size()]);
  }
}
