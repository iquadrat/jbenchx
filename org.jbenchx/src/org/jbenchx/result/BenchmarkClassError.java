package org.jbenchx.result;

public class BenchmarkClassError extends BenchmarkFailure {
  
  private static final long serialVersionUID = 1L;
  
  private final Class<?>    clazz;
  
  private final String      message;
  
  public BenchmarkClassError(Class<?> clazz, String message) {
    this.clazz = clazz;
    this.message = message;
  }
  
  public Class<?> getAffectedClass() {
    return clazz;
  }
  
  @Override
  public String getMessage() {
    return message;
  }
  
  @Override
  public String toString() {
    return getClass().getSimpleName() + ": " + clazz.getName() + ": " + message;
  }
  
}
