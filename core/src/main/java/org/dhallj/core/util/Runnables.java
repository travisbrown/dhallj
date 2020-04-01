package org.dhallj.core.util;

import java.util.Iterator;
import java.util.LinkedList;

public final class Runnables implements Runnable {
  private final Iterable<Runnable> values;

  Runnables(Iterable<Runnable> values) {
    this.values = values;
  }

  public final void run() {
    LinkedList<Iterator<Runnable>> stack = new LinkedList<Iterator<Runnable>>();
    Iterator<Runnable> current = this.values.iterator();

    do {
      if (current.hasNext()) {
        Runnable next = current.next();

        if (next instanceof Runnables) {
          stack.push(current);
          current = ((Runnables) next).values.iterator();
        } else {
          next.run();
        }
      } else {
        current = stack.poll();
      }
    } while (current != null);
  }
}
