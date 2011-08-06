package org.jbench;

public class BenchmarkContext {
  
  private static final long MS = 1000 * 1000L;
  
  public long getTargetTimeNs() {
    return 250 * MS;
  }
  
  public int getMinRunCount() {
    return 4;
  }
  
  public int getMaxRunCount() {
    return 100;
  }
  
}
