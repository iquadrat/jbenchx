package org.jbenchx;

import java.lang.reflect.*;
import java.util.*;

import org.jbenchx.error.*;
import org.jbenchx.monitor.*;
import org.jbenchx.result.*;
import org.jbenchx.util.*;

public class BenchmarkRunner {
  
  private List<Class<? extends Benchmark>> fBenchmarks = new ArrayList<Class<? extends Benchmark>>();
  
  public void add(Class<? extends Benchmark> benchmark) {
    fBenchmarks.add(benchmark);
  }
  
  public BenchmarkResult run(IProgressMonitor progressMontior) {
    BenchmarkContext context = new BenchmarkContext(progressMontior);
    BenchmarkResult result = new BenchmarkResult();
    List<BenchmarkTask> benchmarkTasks = findAllBenchmarkTasks(context, result);
    progressMontior.init(benchmarkTasks.size(), result);
    for (BenchmarkTask task: benchmarkTasks) {
      task.run(result, context);
    }
    progressMontior.finished();
    return result;
  }
  
  private List<BenchmarkTask> findAllBenchmarkTasks(BenchmarkContext context, BenchmarkResult result) {
    List<BenchmarkTask> tasks = new ArrayList<BenchmarkTask>();
    for (Class<? extends Benchmark> clazz: fBenchmarks) {
      addBenchmarkTasks(context, result, tasks, clazz);
    }
    return tasks;
  }
  
  private void addBenchmarkTasks(BenchmarkContext context, BenchmarkResult result, List<BenchmarkTask> tasks, Class<? extends Benchmark> clazz) {
    
    if (!ClassUtil.hasDefaultConstructor(clazz)) {
      result.addGeneralError(new BenchmarkClassError(clazz, "No default constructor found!"));
    }
    
    for (Method method: clazz.getMethods()) {
      
      BenchmarkParameters params = BenchmarkParameters.read(method);
      if (params == null) continue;
      
      if (method.getParameterTypes().length > 0) {
        result.addGeneralError(new BenchmarkClassError(clazz, "Benchmark method " + method.getName() + " has parameters!"));
        continue;
      }
      
      try {
        
        Benchmark benchmark = clazz.newInstance();
        params = BenchmarkParameters.merge(context.getDefaultParams(), BenchmarkParameters.read(method));
        tasks.add(new BenchmarkTask(benchmark.getName(), clazz.getName(), method.getName(), params));
        
      } catch (InstantiationException e) {
        result.addGeneralError(new BenchmarkClassError(clazz, "Could not instantiate class " + clazz.getSimpleName() + ": " + e.getMessage()));
      } catch (IllegalAccessException e) {
        result.addGeneralError(new BenchmarkClassError(clazz, "Could not instantiate class " + clazz.getSimpleName() + ": " + e.getMessage()));
      }
      
    }
    
  }
  
}
