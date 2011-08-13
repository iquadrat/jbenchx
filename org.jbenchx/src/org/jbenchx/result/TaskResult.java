package org.jbenchx.result;

import java.util.*;

import org.jbenchx.*;
import org.jbenchx.error.*;

public class TaskResult {
  
  private BenchmarkTimings     fTimings;
  private List<BenchmarkError> fErrors;
  private double               fEstimatedBenchmark;
  private long                 fIterationCount;
  
  public TaskResult(BenchmarkTimings timings, long iterationCount, BenchmarkError... errors) {
    fTimings = timings;
    fIterationCount = iterationCount;
    fErrors = Arrays.asList(errors);
    fEstimatedBenchmark = fTimings.getEstimatedRunTime() /  fIterationCount;
  }
  
  public long getIterationCount() {
    return fIterationCount;
  }
  
  public List<BenchmarkError> getErrors() {
    return fErrors;
  }
  
  public BenchmarkTimings getTimings() {
    return fTimings;
  }
  
  public double getEstimatedBenchmark() {
    return fEstimatedBenchmark;
  }
  
}
