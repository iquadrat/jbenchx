/*
 * Created on 01.10.2011
 *
 */
package org.jbenchx.result;

import java.util.*;

import org.jbenchx.*;

public interface ITaskResult {

  public long getIterationCount();

  public List<BenchmarkFailure> getErrors();

  public List<BenchmarkWarning> getWarnings();

  public BenchmarkTimings getTimings();

  public double getEstimatedBenchmark();

}