package org.jbenchx.result;

import org.jbenchx.run.IBenchmarkTask;

public class BenchmarkTaskFailure extends BenchmarkFailure {

  private static final long serialVersionUID = 1L;

  private final IBenchmarkTask fTask;

  private final Exception      fException;

  public BenchmarkTaskFailure(IBenchmarkTask task, Exception exception) {
    fTask = task;
    fException = exception;
  }

  @Override
  public String toString() {
    return fTask.getName() + ": " + fException;
  }
  
}
