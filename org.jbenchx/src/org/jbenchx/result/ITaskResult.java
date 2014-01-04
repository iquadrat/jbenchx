/*
 * Created on 01.10.2011
 *
 */
package org.jbenchx.result;

import java.util.List;

public interface ITaskResult {
  
  public long getIterationCount();
  
  public List<BenchmarkFailure> getFailures();
  
  public List<BenchmarkWarning> getWarnings();
  
  public BenchmarkTimings getTimings();
  
  /**
   * @return estimated benchmark time in nanoseconds
   */
  public double getEstimatedBenchmark();
  
}