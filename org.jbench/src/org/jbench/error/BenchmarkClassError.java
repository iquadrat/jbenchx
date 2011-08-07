package org.jbench.error;

import org.jbench.*;

public class BenchmarkClassError extends BenchmarkError {
  
  private final Class<? extends Benchmark> fClass;
  private final String                     fMessage;
  
  public BenchmarkClassError(Class<? extends Benchmark> clazz, String message) {
    fClass = clazz;
    fMessage = message;
  }
  
  @Override
  public String toString() {
    return getClass().getSimpleName() + ": " + fClass.getName() + ": " + fMessage;
  }
  
}
