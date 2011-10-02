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

  private final List<String>                fGcs                = new ArrayList<String>();

  private final Map<String, GcCountAndTime> fCountAndTimeMap    = new HashMap<String, GcCountAndTime>();

  /**
   * Adds a garbage collection event.
   * @param name the name of the collector
   * @param count the number of garbage collections
   * @param time time in ns used for the garbage collections
   */
  public void addGc(String name, long count, long time) {
    fGcs.add(name);
    fCountAndTimeMap.put(name, new GcCountAndTime(count, time));
  }

  public List<String> getGcNames() {
    return Collections.unmodifiableList(fGcs);
  }

  public long getGcCount(String name) {
    return get(name).getCount();
  }

  /**
   * @return time in ns
   */
  public long getGcTime(String name) {
    return get(name).getTime();
  }

  private GcCountAndTime get(String name) {
    GcCountAndTime result = fCountAndTimeMap.get(name);
    if (result == null) {
      return ZERO_COUNT_AND_TIME;
    }
    return result;
  }

  public boolean noGc() {
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
