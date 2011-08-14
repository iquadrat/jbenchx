package org.jbenchx;

import java.lang.reflect.*;
import java.util.*;

import org.jbenchx.annotations.*;
import org.jbenchx.error.*;
import org.jbenchx.monitor.*;
import org.jbenchx.result.*;
import org.jbenchx.util.*;

public class BenchmarkRunner {
  
  private List<Class<? extends Benchmark>> fBenchmarks = new ArrayList<Class<? extends Benchmark>>();
  
  public void add(Class<? extends Benchmark> benchmark) {
    fBenchmarks.add(benchmark);
  }
  
  public BenchmarkResult run(BenchmarkContext context) {
    IProgressMonitor prgoressMonitor = context.getProgressMonitor();
    BenchmarkResult result = new BenchmarkResult();
    List<BenchmarkTask> benchmarkTasks = findAllBenchmarkTasks(context, result);
    prgoressMonitor.init(benchmarkTasks.size(), result);
    for (BenchmarkTask task: benchmarkTasks) {
      task.run(result, context);
    }
    prgoressMonitor.finished();
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
    
    Method[] methods = clazz.getMethods();
    Arrays.sort(methods, MethodByNameSorter.INSTANCE);
    for (Method method: methods) {
      
      BenchmarkParameters params = BenchmarkParameters.read(method);
      if (params == null) continue;
      
      if (method.getParameterTypes().length > 0) {
        result.addGeneralError(new BenchmarkClassError(clazz, "Benchmark method " + method.getName() + " has parameters!"));
        continue;
      }
      
      params = BenchmarkParameters.merge(context.getDefaultParams(), BenchmarkParameters.read(method));
      boolean singleRun = hasSingleRunAnnotation(method); 
      
      tasks.add(new BenchmarkTask(clazz.getSimpleName(), clazz.getName(), method.getName(), params, singleRun));
      
    }
    
  }

  private boolean hasSingleRunAnnotation(Method method) {
    return !ClassUtil.findAnnotations(method, SingleRun.class).isEmpty();
  }
  
}
