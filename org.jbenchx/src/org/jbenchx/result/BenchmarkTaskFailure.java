package org.jbenchx.result;

import java.io.PrintWriter;

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

  @Override
  public void print(PrintWriter out) {
    out.println(toString());
    fException.printStackTrace(out);
    out.flush();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fException == null) ? 0 : fException.hashCode());
    result = prime * result + ((fTask == null) ? 0 : fTask.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    BenchmarkTaskFailure other = (BenchmarkTaskFailure)obj;
    if (fException == null) {
      if (other.fException != null) return false;
    } else if (!fException.getMessage().equals(other.fException.getMessage())) return false;
    if (fTask == null) {
      if (other.fTask != null) return false;
    } else if (!fTask.equals(other.fTask)) return false;
    return true;
  }
  
}
