package org.jbenchx.result;

import java.io.PrintWriter;


public abstract class BenchmarkFailure extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public abstract void print(PrintWriter out);

}
