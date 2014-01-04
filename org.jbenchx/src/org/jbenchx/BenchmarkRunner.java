package org.jbenchx;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jbenchx.annotations.Ignore;
import org.jbenchx.annotations.SingleRun;
import org.jbenchx.monitor.IProgressMonitor;
import org.jbenchx.result.BenchmarkClassError;
import org.jbenchx.result.BenchmarkResult;
import org.jbenchx.result.IBenchmarkResult;
import org.jbenchx.run.BenchmarkTask;
import org.jbenchx.run.IBenchmarkTask;
import org.jbenchx.run.Parameterization;
import org.jbenchx.run.ParameterizationIterable;
import org.jbenchx.run.ParameterizationValues;
import org.jbenchx.util.ClassUtil;
import org.jbenchx.util.MethodByNameSorter;

public class BenchmarkRunner {
  
  private final List<Class<?>> fBenchmarks = new ArrayList<Class<?>>();
  
  public void add(Class<?> benchmark) {
    fBenchmarks.add(benchmark);
  }
  
  public IBenchmarkResult run(IBenchmarkContext context) {
    IProgressMonitor progressMonitor = context.getProgressMonitor();
    BenchmarkResult result = new BenchmarkResult(context.getSystemInfo());
    List<BenchmarkTask> benchmarkTasks = findAllBenchmarkTasks(context);
    progressMonitor.init(benchmarkTasks.size(), result);
    for (IBenchmarkTask task: benchmarkTasks) {
      task.run(result, context);
    }
    progressMonitor.finished();
    return result;
  }
  
  private List<BenchmarkTask> findAllBenchmarkTasks(IBenchmarkContext context) {
    List<BenchmarkTask> tasks = new ArrayList<BenchmarkTask>();
    for (Class<?> clazz: fBenchmarks) {
      addBenchmarkTasks(context, tasks, clazz);
    }
    return tasks;
  }
  
  private void addBenchmarkTasks(IBenchmarkContext context, List<BenchmarkTask> tasks, Class<?> clazz) {
    
    Constructor<?>[] constructors = clazz.getConstructors();
    if (constructors.length != 1) {
      throw new BenchmarkClassError(clazz, "Benchmark class must have exactly one constructor!");
    }
    
    Iterable<ParameterizationValues> constructorParameterizations = null;
    Constructor<?> constructor = constructors[0];
    constructorParameterizations = getConstructorArgumentsIterator(constructor);
    
//    if (!ClassUtil.hasDefaultConstructor(clazz)) {
//      result.addGeneralError(new BenchmarkClassError(clazz, "No default constructor found!"));
//    }
    
    Method[] methods = clazz.getMethods();
    Arrays.sort(methods, MethodByNameSorter.INSTANCE);
    for (Method method: methods) {
      if (hasIgnoreAnnoation(method)) {
        continue;
      }
      
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
      
      Iterable<ParameterizationValues> methodParameterizations = getMethodArgumentsIterator(method);
      for (ParameterizationValues constructorArguments: constructorParameterizations) {
        for (ParameterizationValues methodArguments: methodParameterizations) {
          tasks.add(new BenchmarkTask(clazz, method, params, singleRun, constructorArguments, methodArguments));
        }
      }
      
    }
    
  }
  
  private Iterable<ParameterizationValues> getConstructorArgumentsIterator(Constructor<?> constructor) {
    return getArgumentsIterator(constructor, constructor.getParameterTypes(), constructor.getParameterAnnotations(), constructor.getDeclaringClass());
  }
  
  private Iterable<ParameterizationValues> getMethodArgumentsIterator(Method method) {
    return getArgumentsIterator(method, method.getParameterTypes(), method.getParameterAnnotations(), method.getDeclaringClass());
  }
  
  private Iterable<ParameterizationValues> getArgumentsIterator(AccessibleObject object, Class<?>[] paramTypes, Annotation[][] paramAnnotations,
      Class<?> declaringClass) {
    List<Parameterization> parameterizations = new ArrayList<Parameterization>(paramTypes.length);
    for (int i = 0; i < paramTypes.length; ++i) {
      parameterizations.add(Parameterization.create(declaringClass, object, paramTypes[i], paramAnnotations[i]));
    }
    return new ParameterizationIterable(parameterizations);
  }
  
  private boolean hasSingleRunAnnotation(Method method) {
    return !ClassUtil.findMethodAnnotations(method, SingleRun.class).isEmpty();
  }
  
  private boolean hasIgnoreAnnoation(Method method) {
    return !ClassUtil.findMethodAnnotations(method, Ignore.class).isEmpty();
  }
  
}
