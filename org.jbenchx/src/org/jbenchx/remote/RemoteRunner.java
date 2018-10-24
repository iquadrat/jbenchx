package org.jbenchx.remote;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.jbenchx.BenchmarkContext;
import org.jbenchx.BenchmarkRunner;
import org.jbenchx.IBenchmarkContext;
import org.jbenchx.monitor.ConsoleProgressMonitor;
import org.jbenchx.monitor.MultiProgressMonitor;
import org.jbenchx.result.IBenchmarkResult;
import org.jbenchx.result.XmlResultSerializer;
import org.jbenchx.util.StringUtil;

public class RemoteRunner {
  
  private static final String FLAG_BENCHMARK = "-benchmarks";
  
  private static final String FLAG_TAG = "-tags";

  public static void main(String[] args) {

    BenchmarkRunner runner = new BenchmarkRunner();

    List<String> bechmarks = getFlag(FLAG_BENCHMARK, args);

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
      List<Pattern> patterns = BenchmarkContext.toPattern(getFlag(FLAG_TAG, args));
      IBenchmarkContext context = BenchmarkContext.create(progressMonitor, patterns.isEmpty() ? BenchmarkContext.RUN_ALL : patterns);
      IBenchmarkResult result = runner.run(context);
      FileOutputStream out = new FileOutputStream(fileName.toString() + "-"+result.getEndTime().getTime()+ ".bench");
      new XmlResultSerializer().serialize(result, out);

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  private static List<String> getFlag(String flag, String[] args) {
    for (String arg: args) {
      if (arg.startsWith(flag)) {
        String substring = arg.substring(flag.length()).trim();
        return StringUtil.split(substring, ",");
      }
    }
    return Collections.emptyList();
  }

}
