package org.jbenchx;

import java.lang.management.*;

public class SystemInfo {
  
  private final long   fTimerGranularity;
  
  private final long   fMethodInvokeTime;
  
  private final double fSystemBenchmark;
  
  private final String fOsInfo;
  
  private final String fOsVersion;
  
  private final int    fCpuCount;
  
  private final long   fMaxHeapSize;
  
  public SystemInfo(long timerGranularity, long methodInvokeTime, double systemBenchMark, String osInfo, String osVersion, int cpuCount, 
      long maxHeapSize) {
    fTimerGranularity = timerGranularity;
    fMethodInvokeTime = methodInvokeTime;
    fSystemBenchmark = systemBenchMark;
    fOsInfo = osInfo;
    fOsVersion = osVersion;
    fCpuCount = cpuCount;
    fMaxHeapSize = maxHeapSize;
  }
  
  public static SystemInfo create(long timerGranularity, long methodInvokeTime, double systemBenchMark) {
    String osInfo = System.getProperty("os.name");
    String osVersion = System.getProperty("os.version");
    int cpuCount = ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
    MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
    long maxHeapSize = heapMemoryUsage.getMax();
    return new SystemInfo(timerGranularity, methodInvokeTime, systemBenchMark, osInfo, osVersion, cpuCount, maxHeapSize);
  }
  
  /**
   * @return timer granularity in nanoseconds
   */
  public long getTimerGranularity() {
    return fTimerGranularity;
  }
  
  /**
   * @return estimated time in nanoseconds to invoke a benchmark method
   */
  public long getMethodInvokeTime() {
    return fMethodInvokeTime;
  }
  
  public double getSystemBenchmark() {
    return fSystemBenchmark;
  }
  
  public String getOsVersion() {
    return fOsVersion;
  }
  
  public int getCpuCount() {
    return fCpuCount;
  }

  public String getOsInfo() {
    return fOsInfo;
  }
  
  public long getMaxHeapSize() {
    return fMaxHeapSize;
  }
  
  @Override
  public String toString() {
    return "SystemInfo [fTimerGranularity=" + fTimerGranularity + ", fMethodInvokeTime=" + fMethodInvokeTime + ", fOsInfo=" + fOsInfo
    + ", fOsVersion=" + fOsVersion + ", fCpuCount=" + fCpuCount + ", fMaxHeapSize=" + fMaxHeapSize + "]";
  }
  
}
