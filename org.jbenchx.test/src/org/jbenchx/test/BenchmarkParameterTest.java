package org.jbenchx.test;

import static org.junit.Assert.*;

import org.jbenchx.*;
import org.jbenchx.annotations.*;
import org.junit.*;

public class BenchmarkParameterTest extends BenchmarkTestCase {

  public abstract static class A {

    public void zero() {}

    @Bench(minRunCount = 10)
    public void one() {}

    @Bench(maxDeviation = 0.01, minRunCount = 9)
    public void two() {}

    @Bench(minSampleCount = 10, targetTimeNs = 1000)
    public void three() {}

    @Bench(minRunCount = 100)
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
    @Bench(maxDeviation = 0.1)
    public void four() {}

    @Override
    @Bench(maxRunCount = 2, targetTimeNs = 100)
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

  @Test
  public void read() throws Exception {
    assertNull(BenchmarkContext.getParamsFrom(C.class.getMethod("zero")));

    assertEquals(Benchmark.Parameters.newBuilder().setMinRunCount(10).build(), BenchmarkContext.getParamsFrom(C.class.getMethod("one")));
    assertEquals(Benchmark.Parameters.newBuilder().setMinRunCount(9).setMaxDeviation(0.01).build(), BenchmarkContext.getParamsFrom(C.class.getMethod("two")));
    assertEquals(Benchmark.Parameters.newBuilder().setTargetTimeNs(1000).setMinSampleCount(10).build(), BenchmarkContext.getParamsFrom(C.class.getMethod("three")));
    assertEquals(Benchmark.Parameters.newBuilder().setMinRunCount(100).setMaxDeviation(0.1).build(), BenchmarkContext.getParamsFrom(C.class.getMethod("four")));
    assertEquals(Benchmark.Parameters.newBuilder().setMaxRunCount(1).setTargetTimeNs(1000).setMinSampleCount(2).build(), BenchmarkContext.getParamsFrom(C.class.getMethod("five")));
    assertEquals(Benchmark.Parameters.getDefaultInstance(), BenchmarkContext.getParamsFrom(C.class.getMethod("six")));
    assertEquals(Benchmark.Parameters.newBuilder().setMinRunCount(20).build(), BenchmarkContext.getParamsFrom(C.class.getMethod("seven")));
  }

}
