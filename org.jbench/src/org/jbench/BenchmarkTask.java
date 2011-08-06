package org.jbench;

import java.lang.reflect.*;

import org.jbench.error.*;

public class BenchmarkTask {
  
  private static final double SQRT2 = Math.sqrt(2);
  
  private final String        fClassName;
  private final String        fMethodName;
  private final int           fDivisor;
  private final int           fMinRunCount;
  private final int           fMaxRunCount;
  private final int           fMinSampleCount;
  private final double        fMaxDeviation;
  
  public BenchmarkTask(String className, String methodName, int divisor, int minRunCount, int maxRunCount, int minSampleCount, double maxDeviation) {
    fClassName = className;
    fMethodName = methodName;
    fDivisor = divisor;
    fMinRunCount = minRunCount;
    fMaxRunCount = maxRunCount;
    fMinSampleCount = minSampleCount;
    fMaxDeviation = maxDeviation;
  }
  
  public void run(BenchmarkResult result, BenchmarkContext context) {
    try {
      internalRun(context, result);
    } catch (Exception e) {
      result.addError(new BenchmarkTaskException(this, e));
    }
  }
  
  private void internalRun(BenchmarkContext context, BenchmarkResult result) throws Exception {
    Object benchmark = createInstance(result);
    Method method = getBenchmarkMethod(benchmark);
    long iterationCount = findIterationCount(context, benchmark, method);
    System.out.println("using " + iterationCount + " iterations");
    
    BenchmarkTimings timings = new BenchmarkTimings(fMinRunCount, fMaxRunCount, fMinSampleCount, fMaxDeviation);
    do {
      long time = singleRun(benchmark, method, iterationCount);
      timings.add(time);
    } while (timings.needsMoreRuns());
    
    long avgNs = Math.round(timings.getEstimatedAverage() / iterationCount);
    System.out.println(this + ": " + avgNs + "ns");
    
  }
  
  private long singleRun(Object benchmark, Method method, long iterationCount) throws IllegalArgumentException, IllegalAccessException,
      InvocationTargetException {
    Timer timer = new Timer();
    timer.start();
    for (long i = 0; i < iterationCount; ++i) {
      method.invoke(benchmark);
    }
    return timer.stopAndReset();
  }
  
  private long findIterationCount(BenchmarkContext context, Object benchmark, Method method) throws IllegalAccessException, InvocationTargetException {
    Timer timer = new Timer();
    long iterations = 1;
    long time;
    for (;;) {
      
      timer.start();
      for (int i = 0; i < iterations; ++i) {
        method.invoke(benchmark);
      }
      time = timer.stopAndReset();
      System.out.println(iterations + " / " + time);
      time = Math.max(time, 10 * 1000 * 1000); // at least 10ms
      
      if (iterations == 1 && time > context.getTargetTimeNs()) {
        break;
      }
      if (time >= context.getTargetTimeNs() / SQRT2 && time < context.getTargetTimeNs() * SQRT2) {
        break;
      }
      
      iterations = (long)Math.ceil(1.0 * context.getTargetTimeNs() / time) * iterations;
      
    }
    return iterations;
  }
  
  private Method getBenchmarkMethod(Object benchmark) throws SecurityException, NoSuchMethodException {
    return benchmark.getClass().getMethod(fMethodName);
  }
  
  private Object createInstance(BenchmarkResult result) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    ClassLoader classLoader = ClassUtil.createClassLoader();
    Class<?> clazz = (Class<?>)classLoader.loadClass(fClassName);
    return clazz.newInstance();
  }
  
  @Override
  public String toString() {
    return fClassName + ":" + fMethodName;
  }
  
}
