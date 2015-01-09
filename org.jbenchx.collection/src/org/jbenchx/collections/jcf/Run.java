package org.jbenchx.collections.jcf;

import org.jbenchx.BenchmarkContext;
import org.jbenchx.BenchmarkRunner;
import org.jbenchx.monitor.ConsoleProgressMonitor;



public class Run {
  
  public static void main(String[] args) {
    BenchmarkRunner runner = new BenchmarkRunner();
    runner.add(ArrayListBenchmark.class);
    runner.add(HashSetBenchmark.class);
    runner.add(TreeSetBenchmark.class);
    runner.add(LinkedHashSetBenchmark.class);
    runner.add(ArrayDequeBenchmark.class);
    runner.add(LinkedListBenchmark.class);
    runner.run(BenchmarkContext.create(new ConsoleProgressMonitor()));
  }
  
}
