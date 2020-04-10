package org.dhallj.core.normalization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.dhallj.core.Expr;
import org.dhallj.core.LetBinding;
import org.dhallj.core.Visitor;

/**
 * Performs alpha normalization.
 *
 * <p>Morally this is equivalent to the following (on non-underscore bindings):
 *
 * <pre>
 * input.increment("_").substitute(name, 0, Expr.Constants.UNDERSCORE).decrement(name);
 * </pre>
 *
 * The implementation here is necessary to fit the visitor API.
 *
 * <p>Note that this visitor maintains internal state and instances should not be reused.
 */
public final class AlphaNormalize extends Visitor.Identity {
  // We interpret any underscores as implicitly having this index added to their own.
  private int newUnderscoreDepth = 0;

  // The total number of underscores.
  private int underscoreDepth = 0;

  // We change any other name to an underscore whose index we compute from the depth we track here.
  private final Map<String, LinkedList<Integer>> nameDepths =
      new HashMap<String, LinkedList<Integer>>();

  @Override
  public Expr onIdentifier(Expr self, String name, long index) {
    if (name.equals("_")) {
      return Expr.makeIdentifier(name, index + this.newUnderscoreDepth);
    } else {
      LinkedList<Integer> depths = this.nameDepths.get(name);

      if (depths == null) {
        return self;
      } else if (index < depths.size()) {
        return Expr.makeIdentifier("_", underscoreDepth - depths.get((int) index));
      } else {
        return Expr.makeIdentifier(name, index - depths.size());
      }
    }
  }

  @Override
  public void bind(String name, Expr type) {
    this.underscoreDepth += 1;
    if (!name.equals("_")) {
      this.newUnderscoreDepth += 1;

      LinkedList<Integer> nameDepth = this.nameDepths.get(name);
      if (nameDepth == null) {
        nameDepth = new LinkedList<Integer>();
        nameDepths.put(name, nameDepth);
      }
      nameDepth.push(this.underscoreDepth);
    }
  }

  @Override
  public Expr onLambda(String name, Expr type, Expr result) {
    this.underscoreDepth -= 1;
    if (!name.equals("_")) {
      this.newUnderscoreDepth -= 1;
      this.nameDepths.get(name).pop();
    }
    return Expr.makeLambda("_", type, result);
  }

  @Override
  public Expr onPi(String name, Expr type, Expr result) {
    this.underscoreDepth -= 1;
    if (!name.equals("_")) {
      this.newUnderscoreDepth -= 1;
      this.nameDepths.get(name).pop();
    }
    return Expr.makePi("_", type, result);
  }

  @Override
  public Expr onLet(List<LetBinding<Expr>> bindings, Expr body) {
    List<LetBinding<Expr>> newBindings = new ArrayList<>(bindings.size());

    for (LetBinding<Expr> binding : bindings) {
      String name = binding.getName();

      this.underscoreDepth -= 1;
      if (!name.equals("_")) {
        this.newUnderscoreDepth -= 1;
        this.nameDepths.get(name).pop();
      }

      newBindings.add(new LetBinding<>("_", binding.getType(), binding.getValue()));
    }

    return Expr.makeLet(newBindings, body);
  }
}
