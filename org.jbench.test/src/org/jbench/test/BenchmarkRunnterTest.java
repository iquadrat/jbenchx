package org.jbench.test;

import org.jbench.*;
import org.junit.*;



public class BenchmarkRunnterTest extends BenchmarkTestCase {
  
  @Test
  public void testRun_ArrayListBenchmark() {
    
    BenchmarkRunner runner = new BenchmarkRunner();
    runner.add(ArrayListBenchmark.class);
    runner.run(IProgressMonitor.DUMMY);
    
  }
  
  public static void main(String[] args) {
    new BenchmarkRunnterTest().testRun_ArrayListBenchmark();
  }
  
}
