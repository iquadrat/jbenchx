package org.jbenchx.util;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;

import org.jbenchx.Benchmark;
import org.jbenchx.Benchmark.GcEvent;

public class SystemUtil {
  
  private static final long MS = 1000 * 1000;
  
  public static void cleanMemory() {
    for (int i = 0; i < 5; ++i) {
      System.gc();
      System.runFinalization();
    }
  }
  
  public static Benchmark.GcStats getGcStats() {
    Benchmark.GcStats.Builder builder = Benchmark.GcStats.newBuilder();
    for (GarbageCollectorMXBean gc: ManagementFactory.getGarbageCollectorMXBeans()) {
      builder.putGcEvents(gc.getName(),
          GcEvent.newBuilder()
              .setCount(gc.getCollectionCount())
              .setTimeNs(gc.getCollectionTime() * MS)
              .build());
    }
    return builder.build();
  }
  
  public static long getUsedMemory() {
    Runtime runtime = Runtime.getRuntime();
    return runtime.totalMemory() - runtime.freeMemory();
  }
  
  public static Benchmark.SystemInfo getSystemInfo(long timerGranularityNs, long methodInvokeTimeNs, double systemBenchMark) {
    Benchmark.SystemInfo.Builder builder = Benchmark.SystemInfo.newBuilder();
    builder.setTimerGranularityNs(timerGranularityNs);
    builder.setMethodInvokeTimeNs(methodInvokeTimeNs);
    builder.setSystemBenchmark(systemBenchMark);
    builder.setOsInfo(System.getProperty("os.name"));
    builder.setOsVersion(System.getProperty("os.version"));
    builder.setCpuCount(ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors());
    MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
    builder.setMaxHeapSize(heapMemoryUsage.getMax());
    return builder.build();
  }
  
//  public static long estimateMethodInvokeTime() throws Exception {
//    Timer timer = new Timer();
//    Method method1 = InvokeMe.class.getMethod("methodOne");
//    Method method2 = InvokeMe.class.getMethod("methodTwo");
//    InvokeMe foo = new InvokeMe();
//    long min = Long.MAX_VALUE;
//    for (int j = 0; j < 10; ++j) {
//      int count = 1000000;
//      for (int i = 0; i < count; ++i) {
//        method1.invoke(foo);
//      }
//      timer.start();
//      for (int i = 0; i < count; ++i) {
//        method2.invoke(foo);
//      }
//      long time2 = timer.stopAndReset() / count;
//      min = Math.min(min, time2);
//      System.out.println(time2);
//    }
//    return min;
//  }
//  
//  private static class InvokeMe {
//    
//    @CheckForNull
//    public Object methodOne() {
//      return null;
//    }
//    
//    @CheckForNull
//    public Object methodTwo() {
//      return null;
//    }
//    
//  }
  
}
