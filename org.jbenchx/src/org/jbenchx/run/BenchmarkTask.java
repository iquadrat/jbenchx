package org.jbenchx.run;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.CheckForNull;

import org.jbenchx.Benchmark;
import org.jbenchx.Benchmark.SystemInfo;
import org.jbenchx.IBenchmarkContext;
import org.jbenchx.SkipBenchmarkException;
import org.jbenchx.Timer;
import org.jbenchx.result.ResultUtil;
import org.jbenchx.util.SystemUtil;
import org.jbenchx.util.TimeUtil;
import org.jbenchx.vm.VmState;

// TODO extract proper base class
public class BenchmarkTask implements IBenchmarkTask {
  
  private static final double            SQRT2 = Math.sqrt(2);
  
  protected final String                 fClassName;
  
  protected final String                 fMethodName;
  
  protected final Benchmark.Parameters    params;
  
  protected final boolean                fSingleRun;
  
  protected final ParameterizationValues fConstructorArguments;
  
  protected final ParameterizationValues fMethodArguments;
  
  protected final ArrayList<Class<?>>    fMethodArgumentTypes;
  
  public BenchmarkTask(Class<?> benchmarkClass, Method method, Benchmark.Parameters params, boolean singleRun,
      ParameterizationValues constructorArguments,
      ParameterizationValues methodArguments) {
    this(benchmarkClass.getName(), method.getName(), new ArrayList<Class<?>>(Arrays.<Class<?>>asList(method.getParameterTypes())),
        params, singleRun, constructorArguments, methodArguments);
  }
  
  public BenchmarkTask(String className, String methodName, List<Class<?>> methodArgumentTypes, Benchmark.Parameters params, boolean singleRun,
      ParameterizationValues constructorArguments, ParameterizationValues methodArguments) {
    if (methodArgumentTypes.size() != methodArguments.getValues().size()) {
      throw new IllegalArgumentException("Method argument type count does not match the method arguments count: " + methodArgumentTypes.size()
          + " != " + methodArguments.getValues().size());
    }
    this.fClassName = className;
    this.fMethodName = methodName;
    this.fMethodArgumentTypes = new ArrayList<Class<?>>(methodArgumentTypes);
    this.params = params;
    this.fSingleRun = singleRun;
    this.fConstructorArguments = constructorArguments;
    this.fMethodArguments = methodArguments;
  }
  
  public Benchmark.TaskResult run(IBenchmarkContext context) {
    ClassLoader classLoader = context.createClassLoader();
    
    try {
      context.getProgressMonitor().started(this);
      
      Benchmark.TaskResult taskResult;
      if (params.getMeasureMemory()) {
        taskResult = measureMemory(context);
      } else {
        taskResult = internalRun(context, classLoader);
      }
      
      context.getProgressMonitor().done(this, taskResult);
      return taskResult;
      
    } catch (Exception e) {
      if (isException(SkipBenchmarkException.class, e)) {
        context.getProgressMonitor().skipped(this);
        return Benchmark.TaskResult.newBuilder()
                .setTaskName(getName())
                .addError(Benchmark.Error.newBuilder().setSkipped(true).build())
                .build();
      } else {
        Benchmark.TaskResult taskResult =
            Benchmark.TaskResult.newBuilder()
                .setTaskName(getName())
                .addError(ResultUtil.toFailure(e))
                .build();
        context.getProgressMonitor().failed(this, taskResult);
        return taskResult;
      }
    }
  }
  
  private boolean isException(Class<? extends Exception> class_, @CheckForNull Throwable e) {
    if (e == null) {
      return false;
    }
    return class_.isInstance(e) || isException(class_, e.getCause());
  }
  
  protected Benchmark.TaskResult internalRun(IBenchmarkContext context, ClassLoader classLoader) throws Exception {
    long timerGranularity = 10 * TimeUtil.MS;
    long methodInvokeTime = 0;
    SystemInfo systemInfo = context.getSystemInfo();
    if (systemInfo.getTimerGranularityNs() > 0 && systemInfo.getMethodInvokeTimeNs() > 0) {
      timerGranularity = systemInfo.getTimerGranularityNs();
      methodInvokeTime = systemInfo.getMethodInvokeTimeNs();
    }
    
    Object benchmark = createInstance(classLoader);
    Method method = getBenchmarkMethod(benchmark);
    long iterationCount = findIterationCount(benchmark, method, timerGranularity);
    
    List<Benchmark.Timing> timings = new ArrayList<>();
    SystemUtil.cleanMemory();
    VmState preState = VmState.getCurrentState();
    long runtimePerIteration = Long.MAX_VALUE;
    long minTime = Long.MAX_VALUE;
    int restarts = 0;
    do {
      
      if (fSingleRun) {
        benchmark = createInstance(classLoader);
      }
      
      Benchmark.GcStats preGcStats = SystemUtil.getGcStats();
      long time = singleRun(benchmark, method, iterationCount);
      Benchmark.GcStats postGcStats = SystemUtil.getGcStats();
      VmState postState = VmState.getCurrentState();
      runtimePerIteration = Math.min(runtimePerIteration, time / iterationCount);
      
      Benchmark.Timing timing = ResultUtil.createTiming(time, preGcStats, postGcStats);
      if (preState.equals(postState) || restarts > params.getMaxRestartCount()) {
        timings.add(timing);
      } else {
        // restart
        restarts++;
        timings.clear();
        minTime = Long.MAX_VALUE;
        iterationCount = findIterationCount(benchmark, method, timerGranularity);
        runtimePerIteration = Long.MAX_VALUE;
      }
      context.getProgressMonitor().run(this, timing, VmState.difference(preState, postState));
      preState = postState;
      
    } while (needsMoreRuns(params, timings, minTime));
    
//    long avgNs = Math.round(timings.getEstimatedTime() / iterationCount / fDivisor);
//    System.out.println();
//    System.out.println(this + ": " + TimeUtil.toString(avgNs));
    double divisor = fMethodArguments.getfDivisor() * fConstructorArguments.getfDivisor();
    
    
    Benchmark.TaskResult.Builder result = ResultUtil.createTaskResult(getName(), params, timings, iterationCount, divisor).toBuilder();
    long minSingleIterationTime = methodInvokeTime * 10;
    if (runtimePerIteration < minSingleIterationTime) {
      result.addWarning(
          Benchmark.Warning.newBuilder().setMessage("Runtime of single iteration too short: " + runtimePerIteration
          + "ns, increase work in single iteration to run at least " + minSingleIterationTime + "ns").build());
    }
    return result.build();
  }
  
  private boolean needsMoreRuns(Benchmark.Parameters params, List<Benchmark.Timing> timings, long minTimeNs) {
    final int runs = timings.size();
    if (runs >= params.getMaxRunCount()) {
      return false;
    }
    if (runs < params.getMinRunCount()) {
      return true;
    }
    
    if (timings.get(timings.size() - 1).getRunTimeNs() == minTimeNs) {
      return true;
    }
    
    long maxAllowedTimeNs = Math.round(minTimeNs * (1 + params.getMaxDeviation()));
    int validSampleCount = 0;
    for (int i = 0; i < runs; ++i) {
      if (timings.get(i).getRunTimeNs() <= maxAllowedTimeNs) {
        validSampleCount++;
      }
    }
    
    return validSampleCount < params.getMinSampleCount();
  }
  
  @SuppressWarnings("unused")
  private volatile Object resultObject;
  
  private Benchmark.TaskResult measureMemory(IBenchmarkContext context) throws Exception {
    Object benchmark = createInstance(context.createClassLoader());
    Method method = getBenchmarkMethod(benchmark);
    
    int iterations = 5;
    List<Long> memUsed = new ArrayList<Long>(iterations);
    List<Benchmark.Timing> timings = new ArrayList<>();
    for (int i = 0; i < iterations; ++i) {
      SystemUtil.cleanMemory();
      long memoryBefore = SystemUtil.getUsedMemory();
      VmState preState = VmState.getCurrentState();
      
      Object[] arguments = fMethodArguments.getValues().toArray();
      resultObject = method.invoke(benchmark, arguments);
      
      VmState postState = VmState.getCurrentState();
      SystemUtil.cleanMemory();
      long memoryAfter = SystemUtil.getUsedMemory();
      
      resultObject = null;
      
      memUsed.add(memoryAfter - memoryBefore);
      // FIXME we should not use runtime ns for reporting bytes!
      Benchmark.Timing timing =
          Benchmark.Timing.newBuilder()
            .setRunTimeNs((memoryAfter - memoryBefore) * 1000 * 1000 * 1000L)
            .build();
      timings.add(timing);
      context.getProgressMonitor().run(this, timing, VmState.difference(preState, postState));
    }
    double divisor = fMethodArguments.getfDivisor() * fConstructorArguments.getfDivisor();
    return ResultUtil.createTaskResult(getName(), params, timings, 1, divisor);
  }
  
  protected long singleRun(Object benchmark, Method method, long iterationCount) throws IllegalArgumentException, IllegalAccessException,
      InvocationTargetException {
    Timer timer = new Timer();
    timer.start();
    Object[] arguments = fMethodArguments.getValues().toArray();
    for (long i = 0; i < iterationCount; ++i) {
      method.invoke(benchmark, arguments);
    }
    return timer.stopAndReset();
  }
  
  private long findIterationCount(Object benchmark, Method method, long timerGranularity) throws IllegalAccessException, InvocationTargetException {
    if (fSingleRun) {
      return 1;
    }
    
    Timer timer = new Timer();
    long iterations = 1;
    long time;
    for (;;) {
      
      Object[] arguments = fMethodArguments.getValues().toArray();
      timer.start();
      for (int i = 0; i < iterations; ++i) {
        method.invoke(benchmark, arguments);
      }
      time = timer.stopAndReset();
      if (iterations == 1 && time > params.getTargetTimeNs()) {
        break;
      }
      if (time >= params.getTargetTimeNs() / SQRT2 && time < params.getTargetTimeNs() * SQRT2) {
        break;
      }
      time = Math.max(time, 2 * timerGranularity); // at least two times the timer granularity
      double factor = (1.0 * params.getTargetTimeNs()) / time;
      iterations = Math.round(iterations * factor);
      iterations = Math.max(1, iterations);
      
    }
    return iterations;
  }
  
  protected Method getBenchmarkMethod(Object benchmark) throws SecurityException, NoSuchMethodException {
    return benchmark.getClass().getMethod(fMethodName, fMethodArgumentTypes.toArray(new Class[fMethodArgumentTypes.size()]));
  }
  
  protected Object createInstance(ClassLoader classLoader) throws ClassNotFoundException, InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    Class<?> clazz = classLoader.loadClass(fClassName);
    Constructor<?> constructor = clazz.getConstructors()[0];
    return constructor.newInstance(fConstructorArguments.getValues().toArray());
  }
  
  @Override
  public String toString() {
    return getName();
  }
  
  @Override
  public String getName() {
    StringBuilder sb = new StringBuilder();
    int lastDot = fClassName.lastIndexOf('.');
    if (lastDot == -1) {
      sb.append(fClassName);
    } else {
      sb.append(fClassName.substring(lastDot + 1));
    }
    appendArguments(sb, fConstructorArguments);
    sb.append('.');
    sb.append(fMethodName);
    appendArguments(sb, fMethodArguments);
    return sb.toString();
  }
  
  private void appendArguments(StringBuilder sb, ParameterizationValues arguments) {
    if (!arguments.hasArguments()) return;
    sb.append('(');
    for (Object argument: arguments.getValues()) {
      sb.append(argument.toString());
      sb.append(',');
    }
    sb.setCharAt(sb.length() - 1, ')');
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fClassName == null) ? 0 : fClassName.hashCode());
    result = prime * result + ((fConstructorArguments == null) ? 0 : fConstructorArguments.hashCode());
    result = prime * result + ((fMethodArgumentTypes == null) ? 0 : fMethodArgumentTypes.hashCode());
    result = prime * result + ((fMethodArguments == null) ? 0 : fMethodArguments.hashCode());
    result = prime * result + ((fMethodName == null) ? 0 : fMethodName.hashCode());
    result = prime * result + ((params == null) ? 0 : params.hashCode());
    result = prime * result + (fSingleRun ? 1231 : 1237);
    return result;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    BenchmarkTask other = (BenchmarkTask)obj;
    if (fClassName == null) {
      if (other.fClassName != null) return false;
    } else if (!fClassName.equals(other.fClassName)) return false;
    if (fConstructorArguments == null) {
      if (other.fConstructorArguments != null) return false;
    } else if (!fConstructorArguments.equals(other.fConstructorArguments)) return false;
    if (fMethodArgumentTypes == null) {
      if (other.fMethodArgumentTypes != null) return false;
    } else if (!fMethodArgumentTypes.equals(other.fMethodArgumentTypes)) return false;
    if (fMethodArguments == null) {
      if (other.fMethodArguments != null) return false;
    } else if (!fMethodArguments.equals(other.fMethodArguments)) return false;
    if (fMethodName == null) {
      if (other.fMethodName != null) return false;
    } else if (!fMethodName.equals(other.fMethodName)) return false;
    if (params == null) {
      if (other.params != null) return false;
    } else if (!params.equals(other.params)) return false;
    if (fSingleRun != other.fSingleRun) return false;
    return true;
  }
  
}
