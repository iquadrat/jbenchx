package org.jbenchx;

import org.jbenchx.monitor.*;
import org.jbenchx.vm.*;

public class BenchmarkContext {
  
  private static final long         MS      = 1000 * 1000L;
  
  private final VmInfo              fVmInfo = VmInfo.getVmInfo();
  
  private final BenchmarkParameters fDefaultParams;
  
  private final IProgressMonitor    fProgressMonitor;
  
  public BenchmarkContext(IProgressMonitor progressMonitor) {
    this(progressMonitor, new BenchmarkParameters(250 * MS, 1, 4, 100, 8, 0.05));
  }
  
  public BenchmarkContext(IProgressMonitor progressMonitor, BenchmarkParameters defaultParams) {
    fProgressMonitor = progressMonitor;
    fDefaultParams = defaultParams;
  }
  
  public VmInfo getVmInfo() {
    return fVmInfo;
  }
  
  public BenchmarkParameters getDefaultParams() {
    return fDefaultParams;
  }
  
  public IProgressMonitor getProgressMonitor() {
    return fProgressMonitor;
  }
  
}
