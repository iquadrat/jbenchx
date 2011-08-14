package org.jbenchx.util;

import org.jbenchx.*;
import org.jbenchx.annotations.*;

public class SystemBenchmark extends Benchmark {
  
  private volatile double fValue;
  
  public SystemBenchmark() {
    fValue = 2;
  }
  
  @Bench
  public int empty() {
    return 0;
  }
  
  @Bench
  public double sqrt1() {
    return Math.sqrt(fValue) + 1;
  }
  
  @Bench(divisor = 1000)
  public double sqrt() {
    double result = 0;
    for (int i = 0; i < 1000; i++) {
      result += Math.sqrt(fValue);
    }
    return result;
  }
  
}
