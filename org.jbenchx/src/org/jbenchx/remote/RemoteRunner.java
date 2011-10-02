package org.jbenchx.remote;

import java.io.*;
import java.util.*;

import org.jbenchx.*;
import org.jbenchx.monitor.*;
import org.jbenchx.result.*;
import org.jbenchx.util.*;

public class RemoteRunner {

  public static void main(String[] args) {

    BenchmarkRunner runner = new BenchmarkRunner();

    List<String> bechmarks = getBenchmarkStrings(args);

    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    StringBuilder fileName = new StringBuilder();
    Iterator<String> iter = bechmarks.iterator();
    while(iter.hasNext()) {
      String benchmark = iter.next();

      try {
        Class<?> benchmarkClass = classloader.loadClass(benchmark);
        runner.add(benchmarkClass);
        fileName.append(benchmarkClass.getSimpleName());
        if (iter.hasNext()) {
          fileName.append("_");
        }
      } catch (ClassNotFoundException e) {
        // TODO handle
        e.printStackTrace();
      } catch (ClassCastException e) {
        // TODO handle
        e.printStackTrace();
      }

    }

    try {

      MultiProgressMonitor progressMonitor = new MultiProgressMonitor();
      progressMonitor.add(new ConsoleProgressMonitor());
      IBenchmarkContext context = BenchmarkContext.create(progressMonitor);
      IBenchmarkResult result = runner.run(context);
      FileOutputStream out = new FileOutputStream(fileName.toString() + "-"+result.getEndTime().getTime()+ ".bench");
      new XmlResultSerializer().serialize(result, out);

    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

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
