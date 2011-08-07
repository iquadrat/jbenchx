package org.jbench.util;

import java.lang.management.*;

import org.jbench.*;

public class SystemUtil {
  
  private static final long MS = 1000 * 1000;
  
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
  
}
