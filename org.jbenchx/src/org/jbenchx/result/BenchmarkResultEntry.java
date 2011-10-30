package org.jbenchx.result;

import org.jbenchx.run.IBenchmarkTask;

public class BenchmarkResultEntry {
  
  private final IBenchmarkTask fTask;
  
  private final ITaskResult    fResult;
  
  public BenchmarkResultEntry(IBenchmarkTask task, ITaskResult result) {
    fTask = task;
    fResult = result;
  }
  
  
  public IBenchmarkTask getTask() {
    return fTask;
  }
  
  
  public ITaskResult getResult() {
    return fResult;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fResult == null) ? 0 : fResult.hashCode());
    result = prime * result + ((fTask == null) ? 0 : fTask.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    BenchmarkResultEntry other = (BenchmarkResultEntry)obj;
    if (fResult == null) {
      if (other.fResult != null) return false;
    } else if (!fResult.equals(other.fResult)) return false;
    if (fTask == null) {
      if (other.fTask != null) return false;
    } else if (!fTask.equals(other.fTask)) return false;
    return true;
  }
  
}
