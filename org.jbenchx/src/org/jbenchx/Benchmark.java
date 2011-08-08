package org.jbenchx;


public abstract class Benchmark {
  
  public String getName() {
    return getClass().getSimpleName();
  }
  
}
