package org.jbench;

import java.lang.reflect.*;
import java.util.*;

import org.jbench.annotations.*;
import org.jbench.error.*;


public class BenchmarkRunner {
  
  private List<Class<? extends Benchmark>> fBenchmarks = new ArrayList<Class<? extends Benchmark>>();
  
  public void add(Class<? extends Benchmark> benchmark)  {
    fBenchmarks.add(benchmark);
  }
  
  public BenchmarkResult run(IProgressMonitor progressMontior) {
    BenchmarkResult result = new BenchmarkResult();
    List<BenchmarkTask> benchmarkTasks = findAllBenchmarkTasks(result);
    progressMontior.beginTasks(benchmarkTasks.size());
    for (BenchmarkTask task: benchmarkTasks) {
      task.run(result, new BenchmarkContext());
    }
    return result;
  }

  private List<BenchmarkTask> findAllBenchmarkTasks(BenchmarkResult result) {
    List<BenchmarkTask> tasks = new ArrayList<BenchmarkTask>();
    for(Class<? extends Benchmark> clazz: fBenchmarks) {
      addBenchmarkTasks(result, tasks, clazz);
    }
    return tasks;
  }

  private void addBenchmarkTasks(BenchmarkResult result, List<BenchmarkTask> tasks, Class<? extends Benchmark> clazz) {

    if (!ClassUtil.hasDefaultConstructor(clazz)) {
      result.addError(new BenchmarkClassError(clazz, "No default constructor found!"));
    }
    
    for(Method method: clazz.getMethods()) {
      Bench annotation = method.getAnnotation(Bench.class);
      if (annotation == null) continue;
      
      if (method.getParameterTypes().length > 0) {
        result.addError(new BenchmarkClassError(clazz, "Benchmark method "+method.getName()+" has parameters!"));
        continue;
      }
      
      tasks.add(new BenchmarkTask(clazz.getName(), method.getName(), annotation.divisor(), annotation.minRunCount(), annotation.maxRunCount(), annotation.minSampleCount(), annotation.maxDeviation()));
    }
    
  }
  
}
