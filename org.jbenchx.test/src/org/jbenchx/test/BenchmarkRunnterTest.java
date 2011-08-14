package org.jbenchx.test;

import org.jbenchx.*;
import org.jbenchx.monitor.*;
import org.junit.*;

public class BenchmarkRunnterTest extends BenchmarkTestCase {
  
  @Test
  public void testRun_ArrayListBenchmark() {
    BenchmarkRunner runner = new BenchmarkRunner();
    runner.add(ArrayListBenchmark.class);
    runner.run(BenchmarkContext.create(new ConsoleProgressMonitor()));
  }
  
  public static void main(String[] args) {
    new BenchmarkRunnterTest().testRun_ArrayListBenchmark();
  }
  
}
