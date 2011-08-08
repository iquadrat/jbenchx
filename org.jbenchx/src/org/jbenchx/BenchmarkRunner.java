package org.jbenchx;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import javax.annotation.*;

import org.jbenchx.annotations.*;
import org.jbenchx.error.*;
import org.jbenchx.util.*;

public class BenchmarkRunner {
  
  private List<Class<? extends Benchmark>> fBenchmarks = new ArrayList<Class<? extends Benchmark>>();
  
  public void add(Class<? extends Benchmark> benchmark) {
    fBenchmarks.add(benchmark);
  }
  
  public BenchmarkResult run(IProgressMonitor progressMontior) {
    BenchmarkContext context = new BenchmarkContext();
    BenchmarkResult result = new BenchmarkResult();
    List<BenchmarkTask> benchmarkTasks = findAllBenchmarkTasks(result);
    progressMontior.beginTasks(benchmarkTasks.size());
    for (BenchmarkTask task: benchmarkTasks) {
      task.run(result, context);
    }
    return result;
  }
  
  private List<BenchmarkTask> findAllBenchmarkTasks(BenchmarkResult result) {
    List<BenchmarkTask> tasks = new ArrayList<BenchmarkTask>();
    for (Class<? extends Benchmark> clazz: fBenchmarks) {
      addBenchmarkTasks(result, tasks, clazz);
    }
    return tasks;
  }
  
  private void addBenchmarkTasks(BenchmarkResult result, List<BenchmarkTask> tasks, Class<? extends Benchmark> clazz) {
    
    if (!ClassUtil.hasDefaultConstructor(clazz)) {
      result.addError(new BenchmarkClassError(clazz, "No default constructor found!"));
    }
    
    for (Method method: clazz.getMethods()) {
      Bench annotation = findAnnotation(method, Bench.class);
      if (annotation == null) continue;
      
      if (method.getParameterTypes().length > 0) {
        result.addError(new BenchmarkClassError(clazz, "Benchmark method " + method.getName() + " has parameters!"));
        continue;
      }
      
      tasks.add(new BenchmarkTask(clazz.getName(), method.getName(), annotation.divisor(), annotation.minRunCount(), annotation.maxRunCount(),
          annotation.minSampleCount(), annotation.maxDeviation()));
    }
    
  }
  
  @CheckForNull
  private static <A extends Annotation> A findAnnotation(Method m, Class<A> annotation) throws SecurityException {
    A result = m.getAnnotation(annotation);
    if (result != null) {
      return result;
    }
    Class<?> parent = m.getDeclaringClass().getSuperclass();
    if (parent != null) {
      try {
        Method superMethod = parent.getMethod(m.getName());
        return findAnnotation(superMethod, annotation);
      } catch (NoSuchMethodException e) {
      }
    }
    return null;
  }
  
}
