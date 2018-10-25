package org.jbenchx.test;

import java.util.ArrayList;
import java.util.List;

import org.jbenchx.BenchmarkContext;
import org.jbenchx.BenchmarkRunner;
import org.jbenchx.annotations.Bench;
import org.jbenchx.monitor.ConsoleProgressMonitor;
import org.junit.Test;

public class BenchmarkRunnerTest extends BenchmarkTestCase {

  public static class TestBench {

    @Bench
    public Object createList() {
      List<Integer> numbers = new ArrayList<Integer>();
      for (int i = 1; i < 1000; ++i) {
        numbers.add(i);
      }
      return null;
    }

  }

  @Test
  public void runTestBench() {
    BenchmarkRunner runner = new BenchmarkRunner();
    runner.add(TestBench.class);
    runner.run(BenchmarkContext.create(new ConsoleProgressMonitor()));
  }

}
