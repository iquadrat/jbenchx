package org.jbenchx.test;

import org.jbenchx.*;
import org.jbenchx.annotations.*;

public class BenchmarkParameterTest extends BenchmarkTestCase {
  
  public abstract static class A extends Benchmark {
    
    public void zero() {}
    
    @Bench(divisor = 7, minRunCount = 10)
    public void one() {}
    
    @Bench(maxDeviation = 0.01, minRunCount = 9)
    public void two() {}
    
    @Bench(minSampleCount = 10, targetTimeNs = 1000)
    public void three() {}
    
    @Bench(minRunCount = 100, divisor = 20)
    public void four() {}
    
    @Bench(maxRunCount = 10, minSampleCount = 2)
    public void five() {}
    
    @Bench
    public void six() {}
    
    @Bench(minRunCount = 20)
    public abstract void seven();
    
  }
  
  public static class B extends A {
    
    @Override
    public void two() {}
    
    @Override
    @Bench(divisor = 10, maxDeviation = 0.1)
    public void four() {}
    
    @Override
    @Bench(maxRunCount = 2, divisor = 100, targetTimeNs = 100)
    public void five() {}
    
    @Override
    public void seven() {}
    
  }
  
  public static class C extends B {
    
    @Override
    public void three() {}
    
    @Override
    @Bench(maxRunCount = 1, targetTimeNs = 1000)
    public void five() {}
    
  }
  
  public void testRead() throws Exception {
    assertNull(BenchmarkParameters.read(C.class.getMethod("zero")));
    
    // long targetTimeNs, int divisor, int minRunCount, int maxRunCount, int minSampleCount, double maxDeviation
    assertEquals(new BenchmarkParameters(-1, 7, 10, -1, -1, -1), BenchmarkParameters.read(C.class.getMethod("one")));
    assertEquals(new BenchmarkParameters(-1, -1, 9, -1, -1, 0.01), BenchmarkParameters.read(C.class.getMethod("two")));
    assertEquals(new BenchmarkParameters(1000, -1, -1, -1, 10, -1), BenchmarkParameters.read(C.class.getMethod("three")));
    assertEquals(new BenchmarkParameters(-1, 10, 100, -1, -1, 0.1), BenchmarkParameters.read(C.class.getMethod("four")));
    assertEquals(new BenchmarkParameters(1000, 100, -1, 1, 2, -1), BenchmarkParameters.read(C.class.getMethod("five")));
    assertEquals(new BenchmarkParameters(-1, -1, -1, -1, -1, -1), BenchmarkParameters.read(C.class.getMethod("six")));
    assertEquals(new BenchmarkParameters(-1, -1, 20, -1, -1, -1), BenchmarkParameters.read(C.class.getMethod("seven")));
  }
  
}
