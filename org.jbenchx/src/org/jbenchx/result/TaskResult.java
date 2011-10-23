package org.jbenchx.result;

import java.util.*;

public class TaskResult implements ITaskResult {

  private final BenchmarkTimings       fTimings;

  private final List<BenchmarkFailure> fErrors;

  private final List<BenchmarkWarning> fWarnings;

  private final double                 fEstimatedBenchmark;

  private final long                   fIterationCount;

  public TaskResult(BenchmarkTimings timings, long iterationCount, BenchmarkFailure... errors) {
    fTimings = timings;
    fIterationCount = iterationCount;
    fErrors = Arrays.asList(errors);
    fWarnings = new ArrayList<BenchmarkWarning>();
    fEstimatedBenchmark = estimateBenchmark();
  }

  private double estimateBenchmark() {
    double perIterationTimeRaw = 1.0 * fTimings.getEstimatedRunTime() / fIterationCount;
    double benchmarkTime = perIterationTimeRaw / fTimings.getParams().getDivisor();
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
