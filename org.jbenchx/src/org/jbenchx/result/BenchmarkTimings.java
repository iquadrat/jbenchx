package org.jbenchx.result;

import java.util.ArrayList;
import java.util.List;

import org.jbenchx.Benchmark;

public class BenchmarkTimings {
  
//  private final BenchmarkParameters fParams;
  private final List<Timing> timings;
  
  private long               minTime    = Long.MAX_VALUE;
  
  private transient boolean  lastIsBest = true;
  
  public BenchmarkTimings(/*BenchmarkParameters params*/) {
//    fParams = params;
    this.timings = new ArrayList<Timing>();
  }
  
  public void add(Timing timing) {
    this.timings.add(timing);
    if (timing.getRunTime() < minTime) {
      minTime = timing.getRunTime();
      this.lastIsBest = true;
    } else {
      this.lastIsBest = false;
    }
  }
  
  public List<Timing> getTimings() {
    return timings;
  }
  
  public boolean needsMoreRuns(Benchmark.Parameters params) {
    if (getRuns() >= params.getMaxRunCount()) {
      return false;
    }
    if (getRuns() < params.getMinRunCount()) {
      return true;
    }
    
    if (lastIsBest) {
      return true;
    }
    
//    // find last local maxmimum
//    long timing = fTimings.get(fTimings.size() - 1);
//    int localMaxIdx = -1;
//    for (int i = fTimings.size() - 1; i >= 0; --i) {
//      if (fTimings.get(i) < timing) {
//        localMaxIdx = i + 1;
//        break;
//      }
//    }
    
    long maxAllowedTime = Math.round(minTime * (1 + params.getMaxDeviation()));
    int validSampleCount = 0;
    for (int i = 0; i < getRuns(); ++i) {
      if (timings.get(i).getRunTime() <= maxAllowedTime) {
        validSampleCount++;
      }
    }
    
    return validSampleCount < params.getMinSampleCount();
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
