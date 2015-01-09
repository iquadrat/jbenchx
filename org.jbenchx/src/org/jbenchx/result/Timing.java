package org.jbenchx.result;

import javax.annotation.concurrent.NotThreadSafe;

import org.jbenchx.util.TimeUtil;

@NotThreadSafe
public class Timing extends GcStats {
  
  private final long fTime;
  
  public Timing(long runtime, GcStats preStats, GcStats postStats) {
    fTime = runtime;
    
    for (GcEvent postGcEvent: postStats.getGcEvents()) {
      GcEvent preGcEvent = preStats.getByName(postGcEvent.getName());
      long preCount = preGcEvent.getCount();
      long postCount = postGcEvent.getCount();
      if (preCount == postCount) continue;
      addGc(postGcEvent.getName(), postCount - preCount, postGcEvent.getTime() - preGcEvent.getTime());
    }
  }
  
  public long getRunTime() {
    return fTime;
  }
  
  @Override
  public String toString() {
    return TimeUtil.toString(fTime) + ", GC={" + super.toString() + "}";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + (int)(fTime ^ (fTime >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!super.equals(obj)) return false;
    if (getClass() != obj.getClass()) return false;
    Timing other = (Timing)obj;
    if (fTime != other.fTime) return false;
    return true;
  }
  
}
