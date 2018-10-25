package org.jbenchx.result;

import java.util.ArrayList;
import java.util.List;

import org.jbenchx.Benchmark;

public class BenchmarkTimings {
  
  private final List<Benchmark.Timing> timings;
  
  private long                         minTime    = Long.MAX_VALUE;
  
  private boolean            lastIsBest = true;
  
  public BenchmarkTimings() {
    this.timings = new ArrayList<>();
  }
  
  public void add(Benchmark.Timing timing) {
    this.timings.add(timing);
    if (timing.getRunTimeNs() < minTime) {
      minTime = timing.getRunTimeNs();
      this.lastIsBest = true;
    } else {
      this.lastIsBest = false;
    }
  }
  
  public List<Benchmark.Timing> getTimings() {
    return timings;
  }
  

  
  /**
   * @return estimated runtime in nanoseconds
   */
  public long getEstimatedRunTime() {
    return minTime;
  }
  
  public void clear() {
    minTime = Long.MAX_VALUE;
    lastIsBest = true;
    timings.clear();
  }
  
  private int getRuns() {
    return timings.size();
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int)(minTime ^ (minTime >>> 32));
    result = prime * result + ((timings == null) ? 0 : timings.hashCode());
    return result;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    BenchmarkTimings other = (BenchmarkTimings)obj;
    if (minTime != other.minTime) return false;
    if (timings == null) {
      if (other.timings != null) return false;
    } else if (!timings.equals(other.timings)) return false;
    return true;
  }
  
}
