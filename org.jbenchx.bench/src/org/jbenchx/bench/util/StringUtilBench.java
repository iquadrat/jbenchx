package org.jbenchx.bench.util;

import java.util.ArrayList;

import org.jbenchx.*;
import org.jbenchx.annotations.Bench;
import org.jbenchx.monitor.ConsoleProgressMonitor;
import org.jbenchx.util.StringUtil;

public class StringUtilBench extends Benchmark {
  
  private static final int        STRING_COUNT = 1000;
  
  private final ArrayList<String> fStrings;
  
  public StringUtilBench() {
    fStrings = new ArrayList<String>(STRING_COUNT);
    for (int i = 0; i < STRING_COUNT; ++i) {
      fStrings.add(String.valueOf(i));
    }
  }
  
  /**
   * How long does it take to join 1000 strings?
   */
  @Bench(minSampleCount = 20)
  public String joinIterable() {
    return StringUtil.join(", ", (Iterable<String>)fStrings);
  }
  
  @Bench(minSampleCount = 20)
  public String joinList() {
    return StringUtil.join(", ", fStrings);
  }
  
  public static void main(String[] args) {
    
    BenchmarkRunner runner = new BenchmarkRunner();
    runner.add(StringUtilBench.class);
    
    runner.run(BenchmarkContext.create(new ConsoleProgressMonitor()));
    
  }
  
}
