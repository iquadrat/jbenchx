package org.jbenchx.util;

import java.lang.management.*;

import org.jbenchx.result.GcStats;

import edu.umd.cs.findbugs.annotations.SuppressWarnings;

public class SystemUtil {
  
  private static final long MS = 1000 * 1000;

  @SuppressWarnings("DM_GC")
  public static void cleanMemory() {
    for (int i = 0; i < 10; ++i) {
      System.gc();
      System.runFinalization();
    }
  }
  
  public static GcStats getGcStats() {
    GcStats gcStats = new GcStats();
    for (GarbageCollectorMXBean gc: ManagementFactory.getGarbageCollectorMXBeans()) {
      gcStats.addGc(gc.getName(), gc.getCollectionCount(), gc.getCollectionTime() * MS);
    }
    return gcStats;
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
