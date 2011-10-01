package org.jbenchx.remote;

import java.util.*;

import org.jbenchx.*;
import org.jbenchx.monitor.*;
import org.jbenchx.util.*;

public class RemoteRunner {

  public static void main(String[] args) {

    BenchmarkRunner runner = new BenchmarkRunner();

    List<String> bechmarks = getBenchmarkStrings(args);

    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    for (String benchmark: bechmarks) {

      try {
        Class<?> benchmarkClass = classloader.loadClass(benchmark);
        runner.add(benchmarkClass);
      } catch (ClassNotFoundException e) {
        // TODO handle
        e.printStackTrace();
      } catch (ClassCastException e) {
        // TODO handle
        e.printStackTrace();
      }

    }

    BenchmarkContext context = BenchmarkContext.create(new ConsoleProgressMonitor());
    runner.run(context);

  }

  private static List<String> getBenchmarkStrings(String[] args) {
    String argPrefix = "-benchmarks "; // TODO public constant
    for (String arg: args) {
      if (arg.startsWith(argPrefix)) {
        String substring = arg.substring(argPrefix.length());
        return StringUtil.split(substring, ",");
      }
    }
    return Collections.emptyList();
  }

}
