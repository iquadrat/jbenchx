/*
 * Created on 01.10.2011
 *
 */
package org.jbenchx;

import org.jbenchx.monitor.*;
import org.jbenchx.vm.*;

public interface IBenchmarkContext {
  
  public abstract VmInfo getVmInfo();
  
  public abstract BenchmarkParameters getDefaultParams();
  
  public abstract IProgressMonitor getProgressMonitor();
  
  public abstract long getTimerGranularity();
  
  public abstract long getMethodInvokeTime();
  
}