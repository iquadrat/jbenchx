package org.jbenchx.vm;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.CompilationMXBean;
import java.lang.management.ManagementFactory;

public class VmState {
  
  public static final VmState EMPTY = new VmState(0, 0, 0);
  
  private final long          classesLoaded;
  
  private final long          classesUnloaded;
  
  private final long          compilationTimeMs;
  
  public static VmState getCurrentState() {
    ClassLoadingMXBean loadBean = ManagementFactory.getClassLoadingMXBean();
    long classesLoaded = loadBean.getTotalLoadedClassCount();
    long classesUnloaded = loadBean.getUnloadedClassCount();
    long compilationTimeMs = getCompilationTimeMs();
    return new VmState(classesLoaded, classesUnloaded, compilationTimeMs);
  }
  
  protected VmState(long classesLoaded, long classesUnloaded, long compilationTime) {
    this.classesLoaded = classesLoaded;
    this.classesUnloaded = classesUnloaded;
    this.compilationTimeMs = compilationTime;
  }
  
  public static VmState difference(VmState preState, VmState postState) {
    return new VmState(postState.classesUnloaded - preState.classesUnloaded, postState.classesLoaded - preState.classesLoaded,
        postState.compilationTimeMs - preState.compilationTimeMs);
  }
  
  private static long getCompilationTimeMs() {
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
    result = prime * result + (int)(classesLoaded ^ (classesLoaded >>> 32));
    result = prime * result + (int)(classesUnloaded ^ (classesUnloaded >>> 32));
    result = prime * result + (int)(compilationTimeMs ^ (compilationTimeMs >>> 32));
    return result;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    VmState other = (VmState)obj;
    if (classesLoaded != other.classesLoaded) return false;
    if (classesUnloaded != other.classesUnloaded) return false;
    if (compilationTimeMs != other.compilationTimeMs) return false;
    return true;
  }
  
  @Override
  public String toString() {
    return "Classes: " + classesLoaded + " loaded, " + classesUnloaded + " unloaded; used " + compilationTimeMs + " to compile.";
  }
  
}
