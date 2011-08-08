package org.jbenchx;

import java.util.*;

import org.jbenchx.util.*;

public class GcStats {
  
  private static class GcCountAndTime {
    
    private final long fCount;
    private final long fTime;
    
    public GcCountAndTime(long count, long time) {
      fCount = count;
      fTime = time;
    }
    
    public long getCount() {
      return fCount;
    }
    
    public long getTime() {
      return fTime;
    }
    
  }
  
  private static final GcCountAndTime ZERO_COUNT_AND_TIME = new GcCountAndTime(0, 0);
  
  private List<String>                fGcs                = new ArrayList<String>();
  
  private Map<String, GcCountAndTime> fCountAndTimeMap    = new HashMap<String, GcCountAndTime>();
  
  public void addGc(String name, long count, long time) {
    fGcs.add(name);
    fCountAndTimeMap.put(name, new GcCountAndTime(count, time));
  }
  
  public Iterable<String> getGcNames() {
    return fGcs;
  }
  
  public long getGcCount(String name) {
    return get(name).getCount();
  }
  
  public long getGcTime(String name) {
    return get(name).getTime();
  }
  
  private GcCountAndTime get(String name) {
    GcCountAndTime result = fCountAndTimeMap.get(name);
    if (result == null) return ZERO_COUNT_AND_TIME;
    return result;
  }
  
  public boolean isEmpty() {
    return fGcs.isEmpty();
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (String gc: fGcs) {
      sb.append(gc + "[" + getGcCount(gc) + "," + TimeUtil.toString(getGcTime(gc)) + "]");
      sb.append(",");
    }
    if (sb.length() > 0) {
      sb.setLength(sb.length() - 1);
    }
    return sb.toString();
  }
  
}
