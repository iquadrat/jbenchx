/*
 * Created on 01.10.2011
 *
 */
package org.jbenchx;

import javax.annotation.CheckForNull;

import org.jbenchx.monitor.IProgressMonitor;
import org.jbenchx.vm.SystemInfo;

public interface IBenchmarkContext {
  
  public BenchmarkParameters getDefaultParams();
  
  public IProgressMonitor getProgressMonitor();
  
  @CheckForNull 
  public SystemInfo getSystemInfo();
  
  public String getVersion();

  /**
   * @return the class loader to use for loading the benchmark
   */
  public ClassLoader getClassLoader();
  
}