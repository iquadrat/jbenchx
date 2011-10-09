package org.jbenchx.test;

import java.util.*;

import org.jbenchx.*;
import org.jbenchx.annotations.*;
import org.jbenchx.monitor.*;
import org.junit.*;

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
