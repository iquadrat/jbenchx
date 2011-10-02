package org.jbenchx.result;

import java.util.*;

import org.jbenchx.*;

public class TaskResult implements ITaskResult {

  private static final boolean         SUBTRACT_INVOKE_TIME = false;

  private final IBenchmarkContext       fContext;

  private final BenchmarkTimings       fTimings;

  private final List<BenchmarkFailure> fErrors;

  private final List<BenchmarkWarning> fWarnings;

  private final double                 fEstimatedBenchmark;

  private final long                   fIterationCount;

  public TaskResult(IBenchmarkContext context, BenchmarkTimings timings, long iterationCount, BenchmarkFailure... errors) {
    fContext = context;
    fTimings = timings;
    fIterationCount = iterationCount;
    fErrors = Arrays.asList(errors);
    fWarnings = new ArrayList<BenchmarkWarning>();
    fEstimatedBenchmark = estimateBenchmark();
  }

  private double estimateBenchmark() {
    long methodInvokeTime = SUBTRACT_INVOKE_TIME ? Math.max(0, fContext.getMethodInvokeTime()) : 0;
    double perIterationTimeRaw = 1.0 * fTimings.getEstimatedRunTime() / fIterationCount;
    double benchmarkTime = (perIterationTimeRaw - methodInvokeTime) / fTimings.getParams().getDivisor();
    return Math.max(0, benchmarkTime);
  }

  public void addWarning(BenchmarkWarning warning) {
    fWarnings.add(warning);
  }

  @Override
  public long getIterationCount() {
    return fIterationCount;
  }

  @Override
  public List<BenchmarkFailure> getFailures() {
    return fErrors;
  }

  @Override
  public List<BenchmarkWarning> getWarnings() {
    return fWarnings;
  }

  @Override
  public BenchmarkTimings getTimings() {
    return fTimings;
  }

  @Override
  public double getEstimatedBenchmark() {
    return fEstimatedBenchmark;
  }

}
