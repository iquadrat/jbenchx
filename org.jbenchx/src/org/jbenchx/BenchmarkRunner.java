package org.jbenchx;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import org.jbenchx.annotations.*;
import org.jbenchx.monitor.*;
import org.jbenchx.result.*;
import org.jbenchx.run.*;
import org.jbenchx.util.*;

public class BenchmarkRunner {

  private final List<Class<?>> fBenchmarks = new ArrayList<Class<?>>();

  public void add(Class<?> benchmark) {
    fBenchmarks.add(benchmark);
  }

  public IBenchmarkResult run(IBenchmarkContext context) {
    IProgressMonitor progressMonitor = context.getProgressMonitor();
    BenchmarkResult result = new BenchmarkResult();
    List<BenchmarkTask> benchmarkTasks = findAllBenchmarkTasks(context, result);
    progressMonitor.init(benchmarkTasks.size(), result);
    for (IBenchmarkTask task: benchmarkTasks) {
      task.run(result, context);
    }
    progressMonitor.finished();
    return result;
  }

  private List<BenchmarkTask> findAllBenchmarkTasks(IBenchmarkContext context, BenchmarkResult result) {
    List<BenchmarkTask> tasks = new ArrayList<BenchmarkTask>();
    for (Class<?> clazz: fBenchmarks) {
      addBenchmarkTasks(context, result, tasks, clazz);
    }
    return tasks;
  }

  private void addBenchmarkTasks(IBenchmarkContext context, BenchmarkResult result, List<BenchmarkTask> tasks, Class<?> clazz) {

    if (!ClassUtil.hasDefaultConstructor(clazz)) {
      result.addGeneralError(new BenchmarkClassError(clazz, "No default constructor found!"));
    }

    Method[] methods = clazz.getMethods();
    Arrays.sort(methods, MethodByNameSorter.INSTANCE);
    for (Method method: methods) {

      BenchmarkParameters params = BenchmarkParameters.read(method);
      if (params == null) {
        continue;
      }

//      if (method.getParameterTypes().length > 0) {
//        result.addGeneralError(new BenchmarkClassError(clazz, "Benchmark method " + method.getName() + " has parameters!"));
//        continue;
//      }

      params = BenchmarkParameters.merge(context.getDefaultParams(), params);
      boolean singleRun = hasSingleRunAnnotation(method);

      Iterator<ParameterizationValues> paramIterator = getMethodArgumentsIterator(clazz, method);
      while (paramIterator.hasNext()) {
        ParameterizationValues methodArguments = paramIterator.next();
        tasks.add(new BenchmarkTask(clazz.getSimpleName(), clazz.getName(), method.getName(), params, singleRun, methodArguments));
      }

    }

  }

  private Iterator<ParameterizationValues> getMethodArgumentsIterator(Class<?> clazz, Method method) {
    Class<?>[] paramTypes = method.getParameterTypes();
    Annotation[][] paramAnnotations = method.getParameterAnnotations();
    List<Parameterization> parameterizations = new ArrayList<Parameterization>(paramTypes.length);
    for (int i = 0; i < paramTypes.length; ++i) {
      parameterizations.add(Parameterization.create(method, paramTypes[i], paramAnnotations[i]));
    }
    return new ParameterizationIterator(parameterizations);
  }

  private boolean hasSingleRunAnnotation(Method method) {
    return !ClassUtil.findMethodAnnotations(method, SingleRun.class).isEmpty();
  }

}
