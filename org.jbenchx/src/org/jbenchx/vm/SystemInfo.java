package org.jbenchx.vm;

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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + fCpuCount;
    result = prime * result + (int)(fMaxHeapSize ^ (fMaxHeapSize >>> 32));
    result = prime * result + (int)(fMethodInvokeTime ^ (fMethodInvokeTime >>> 32));
    result = prime * result + ((fOsInfo == null) ? 0 : fOsInfo.hashCode());
    result = prime * result + ((fOsVersion == null) ? 0 : fOsVersion.hashCode());
    long temp;
    temp = Double.doubleToLongBits(fSystemBenchmark);
    result = prime * result + (int)(temp ^ (temp >>> 32));
    result = prime * result + (int)(fTimerGranularity ^ (fTimerGranularity >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    SystemInfo other = (SystemInfo)obj;
    if (fCpuCount != other.fCpuCount) return false;
    if (fMaxHeapSize != other.fMaxHeapSize) return false;
    if (fMethodInvokeTime != other.fMethodInvokeTime) return false;
    if (fOsInfo == null) {
      if (other.fOsInfo != null) return false;
    } else if (!fOsInfo.equals(other.fOsInfo)) return false;
    if (fOsVersion == null) {
      if (other.fOsVersion != null) return false;
    } else if (!fOsVersion.equals(other.fOsVersion)) return false;
    if (Double.doubleToLongBits(fSystemBenchmark) != Double.doubleToLongBits(other.fSystemBenchmark)) return false;
    if (fTimerGranularity != other.fTimerGranularity) return false;
    return true;
  }
  
}
