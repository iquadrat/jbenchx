package org.jbenchx.error;

import java.io.*;

import org.jbenchx.*;

public class BenchmarkTaskException extends BenchmarkError {
  
  private final BenchmarkTask fTask;
  private final Exception     fException;
  
  public BenchmarkTaskException(BenchmarkTask task, Exception exception) {
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
  }
  
  
}
