package org.jbenchx.result;

import java.util.*;

import org.jbenchx.*;
import org.jbenchx.error.*;

public class TaskResult {
  
  private static final boolean SUBTRACT_INVOKE_TIME = false;
  
  private final BenchmarkContext     fContext;
  private final BenchmarkTimings     fTimings;
  private final List<BenchmarkFailure> fErrors;
  private final double               fEstimatedBenchmark;
  private final long                 fIterationCount;
  
  public TaskResult(BenchmarkContext context, BenchmarkTimings timings, long iterationCount, BenchmarkFailure... errors) {
    fContext = context;
    fTimings = timings;
    fIterationCount = iterationCount;
    fErrors = Arrays.asList(errors);
    fEstimatedBenchmark = estimateBenchmark();
  }
  
  private double estimateBenchmark() {
    long methodInvokeTime =  SUBTRACT_INVOKE_TIME ? Math.max(0, fContext.getMethodInvokeTime()) : 0;
    double perIterationTimeRaw = 1.0 * fTimings.getEstimatedRunTime() / fIterationCount;
    double benchmarkTime = (perIterationTimeRaw - methodInvokeTime) / fTimings.getParams().getDivisor();
    return Math.max(0, benchmarkTime);
  }
  
  public long getIterationCount() {
    return fIterationCount;
  }
  
  public List<BenchmarkFailure> getErrors() {
    return fErrors;
  }
  
  public BenchmarkTimings getTimings() {
    return fTimings;
  }
  
  public double getEstimatedBenchmark() {
    return fEstimatedBenchmark;
  }
  
}
