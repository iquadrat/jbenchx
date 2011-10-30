package org.jbenchx.result;

import net.jcip.annotations.Immutable;

@Immutable
public class GcEvent {
  
  private final String fName;
  
  private final long   fCount;
  
  private final long   fTime;
  
  public GcEvent(String name, long count, long time) {
    fName = name;
    fCount = count;
    fTime = time;
  }
  
  /**
   * @return the name identifying the garbage collector
   */
  public String getName() {
    return fName;
  }
  
  /**
   * @return the invocation count of this garbage collector
   */
  public long getCount() {
    return fCount;
  }
  
  /**
   * @return the total time used in this garbage collector event in nanoseconds
   */
  public long getTime() {
    return fTime;
  }
  
}