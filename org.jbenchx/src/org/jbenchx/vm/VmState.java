package org.jbenchx.vm;

import java.lang.management.*;

public class VmState {
  
  private final long fClassesLoaded;
  private final long fClassesUnloaded;
  private final long fCompilationTime;
  
  public static VmState getCurrentState() {
    return new VmState();
  }
  
  protected VmState() {
    ClassLoadingMXBean loadBean = ManagementFactory.getClassLoadingMXBean();
    fClassesLoaded = loadBean.getTotalLoadedClassCount();
    fClassesUnloaded = loadBean.getUnloadedClassCount();
    fCompilationTime = getCompilationTime();
    
  }
  
  private long getCompilationTime() {
    CompilationMXBean compBean = ManagementFactory.getCompilationMXBean();
    if (compBean.isCompilationTimeMonitoringSupported()) {
      return compBean.getTotalCompilationTime();
    }
    return 0;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int)(fClassesLoaded ^ (fClassesLoaded >>> 32));
    result = prime * result + (int)(fClassesUnloaded ^ (fClassesUnloaded >>> 32));
    result = prime * result + (int)(fCompilationTime ^ (fCompilationTime >>> 32));
    return result;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    VmState other = (VmState)obj;
    if (fClassesLoaded != other.fClassesLoaded) return false;
    if (fClassesUnloaded != other.fClassesUnloaded) return false;
    if (fCompilationTime != other.fCompilationTime) return false;
    return true;
  }
  
  @Override
  public String toString() {
    return "Classes: " + fClassesLoaded + " loaded, " + fClassesUnloaded + " unloaded; used " + fCompilationTime + " to compile.";
  }
  
}
