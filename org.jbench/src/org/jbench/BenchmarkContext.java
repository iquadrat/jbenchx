package org.jbench;

import org.jbench.vm.*;

public class BenchmarkContext {
  
  private static final long MS = 1000 * 1000L;
  
  private final VmInfo      fVmInfo;
  
  public BenchmarkContext() {
    fVmInfo = VmInfo.getVmInfo();
    System.out.println("Running on "+fVmInfo);
  }
  
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
