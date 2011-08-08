package org.jbenchx.collections.jcf;

import org.jbenchx.*;


public class Run {
  
  public static void main(String[] args) {
    BenchmarkRunner runner = new BenchmarkRunner();
    runner.add(ArrayListBenchmark.class);
    runner.add(HashSetBenchmark.class);
    runner.add(TreeSetBenchmark.class);
    runner.run(IProgressMonitor.DUMMY);
  }
  
}
