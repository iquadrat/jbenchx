package org.jbenchx.bench.util;

import java.util.*;

import org.jbenchx.*;
import org.jbenchx.annotations.*;
import org.jbenchx.monitor.*;
import org.jbenchx.util.*;

public class StringUtilBench {

  private static final int   STRING_COUNT = 1000;

  private final List<String> fStringList;

  private final String[]     fStrings;

  public StringUtilBench() {

    fStrings = new String[STRING_COUNT];

    for (int i = 0; i < STRING_COUNT; ++i) {
      fStrings[i] = String.valueOf(i);
    }
    fStringList = Arrays.asList(fStrings);
  }

  /**
   * How long does it take to join 1000 strings?
   */
  @Bench(minSampleCount = 20)
  public String joinIterable() {
    return StringUtil.join(", ", (Iterable<String>)fStringList);
  }

  @Bench(minSampleCount = 20)
  public String joinList() {
    return StringUtil.join(", ", fStringList);
  }

  @Bench(minSampleCount = 20)
  public String joinArray() {
    return StringUtil.join(", ", fStrings);
  }

  public static void main(String[] args) {

    BenchmarkRunner runner = new BenchmarkRunner();
    runner.add(StringUtilBench.class);

    runner.run(BenchmarkContext.create(new ConsoleProgressMonitor()));

  }

}
