package org.jbench.collections.jcf;

import org.jbench.*;


public class Run {
  
  public static void main(String[] args) {
    BenchmarkRunner runner = new BenchmarkRunner();
    runner.add(ArrayListBenchmark.class);
    runner.add(HashSetBenchmark.class);
    runner.add(TreeSetBenchmark.class);
    runner.run(IProgressMonitor.DUMMY);
  }
  
}
