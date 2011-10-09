/*
 * Created on 01.10.2011
 *
 */
package org.jbenchx.result;

import java.util.*;

public interface ITaskResult {

  public long getIterationCount();

  public List<BenchmarkFailure> getFailures();

  public List<BenchmarkWarning> getWarnings();

  public BenchmarkTimings getTimings();

  public double getEstimatedBenchmark();

}