package org.jbenchx.result;

import java.io.*;

public class BenchmarkClassError extends BenchmarkFailure {

  private final Class<?> fClass;

  private final String   fMessage;

  public BenchmarkClassError(Class<?> clazz, String message) {
    fClass = clazz;
    fMessage = message;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + ": " + fClass.getName() + ": " + fMessage;
  }

  @Override
  public void print(PrintWriter out) {
    out.print(toString());
    out.flush();
  }

}
