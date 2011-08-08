package org.jbenchx;

import org.jbenchx.util.*;

public class Timing extends GcStats {
  
  private final long fRunTime;
  
  public Timing(long runtime, GcStats preStats, GcStats postStats) {
    fRunTime = runtime;
    
    for (String name: postStats.getGcNames()) {
      long preCount = preStats.getGcCount(name);
      long postCount = postStats.getGcCount(name);
      if (preCount == postCount) continue;
      addGc(name, postCount - preCount, postStats.getGcTime(name) - preStats.getGcTime(name));
    }
    
  }
  
  public long getRunTime() {
    return fRunTime;
  }
  
  @Override
  public String toString() {
    return TimeUtil.toString(fRunTime)  + ", GC={" + super.toString() + "}" ;
  }
  
}
