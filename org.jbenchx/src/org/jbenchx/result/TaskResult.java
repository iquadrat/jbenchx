package org.jbenchx.result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jbenchx.BenchmarkParameters;

public class TaskResult implements ITaskResult {
  
  private final BenchmarkTimings       fTimings;
  
  private final List<BenchmarkFailure> fErrors;
  
  private final List<BenchmarkWarning> fWarnings;
  
  private final double                 fEstimatedBenchmark;
  
  private final long                   fIterationCount;
  
  private final double                 fDivisor;
  
  public TaskResult(BenchmarkParameters params, BenchmarkTimings timings, long iterationCount, double divisor) {
    this(params, timings, iterationCount, divisor, new BenchmarkFailure[0]);
  }
  
  public TaskResult(BenchmarkParameters params, BenchmarkFailure... errors) {
    this(params, new BenchmarkTimings(), 0, 1.0, errors);
  }
  
  private TaskResult(BenchmarkParameters params, BenchmarkTimings timings, long iterationCount, double divisor, BenchmarkFailure... errors) {
    fTimings = timings;
    fIterationCount = iterationCount;
    fDivisor = divisor;
    fErrors = new ArrayList<BenchmarkFailure>(Arrays.asList(errors));
    fWarnings = new ArrayList<BenchmarkWarning>();
    fEstimatedBenchmark = estimateBenchmark(params);
  }
  
  private double estimateBenchmark(BenchmarkParameters params) {
    double perIterationTimeRaw = 1.0 * fTimings.getEstimatedRunTime() / fIterationCount;
    double benchmarkTime = perIterationTimeRaw / params.getDivisor() / fDivisor;
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
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fErrors == null) ? 0 : fErrors.hashCode());
    long temp;
    temp = Double.doubleToLongBits(fEstimatedBenchmark);
    result = prime * result + (int)(temp ^ (temp >>> 32));
    result = prime * result + (int)(fIterationCount ^ (fIterationCount >>> 32));
    result = prime * result + ((fTimings == null) ? 0 : fTimings.hashCode());
    result = prime * result + ((fWarnings == null) ? 0 : fWarnings.hashCode());
    return result;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    TaskResult other = (TaskResult)obj;
    if (fErrors == null) {
      if (other.fErrors != null) return false;
    } else if (!fErrors.equals(other.fErrors)) return false;
    if (Double.doubleToLongBits(fEstimatedBenchmark) != Double.doubleToLongBits(other.fEstimatedBenchmark)) return false;
    if (fIterationCount != other.fIterationCount) return false;
    if (fTimings == null) {
      if (other.fTimings != null) return false;
    } else if (!fTimings.equals(other.fTimings)) return false;
    if (fWarnings == null) {
      if (other.fWarnings != null) return false;
    } else if (!fWarnings.equals(other.fWarnings)) return false;
    return true;
  }
  
}
