package org.jbenchx.test;

import org.jbenchx.*;
import org.jbenchx.annotations.Bench;
import org.jbenchx.monitor.ConsoleProgressMonitor;

public class Example2 extends Benchmark {
  
  private int   sval;
  private final int[] vals;
  
  public Example2() throws Exception {
    sval = 0;
    vals = new int[1000];
    for (int i = 0; i < 1000; ++i) {
      vals[i] = i;
    }
  }
  
  @Bench
  public void test_assert() {
    for (int i = 0; i < 1000; ++i) {
      int val = vals[i];
      assert (val >= 0): "should be positive";
      sval = val * 6;
      sval += 3; // Read/write global
      sval /= 2;
    }
  }
  
  @Bench
  public void test_assert2() {
    for (int i = 0; i < 1000; ++i) {
      int val = vals[i];
      if (val < 0) throw new IllegalArgumentException();
      sval = val * 6;
      sval += 3; // Read/write global
      sval /= 2;
    }
  }
  
  public static void main(String[] args) throws Exception {
    
    BenchmarkRunner runner = new BenchmarkRunner();
    runner.add(Example2.class);
    
    runner.run(BenchmarkContext.create(new ConsoleProgressMonitor()));
    
  }
  
}
