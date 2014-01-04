package org.jbenchx.result;

import java.io.PrintWriter;

public class BenchmarkSkipped extends BenchmarkFailure {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public void print(PrintWriter out) {
    out.print("skipped");
  }
  
}
