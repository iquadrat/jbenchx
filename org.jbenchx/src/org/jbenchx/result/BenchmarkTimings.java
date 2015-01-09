package org.jbenchx.result;

import java.util.ArrayList;
import java.util.List;

import org.jbenchx.BenchmarkParameters;

public class BenchmarkTimings {
  
//  private final BenchmarkParameters fParams;
  private final List<Timing> fTimings;
  
  private long               fMinTime    = Long.MAX_VALUE;
  
  private transient boolean  fLastIsBest = true;
  
  public BenchmarkTimings(/*BenchmarkParameters params*/) {
//    fParams = params;
    fTimings = new ArrayList<Timing>();
  }
  
  public void add(Timing timing) {
    fTimings.add(timing);
    if (timing.getRunTime() < fMinTime) {
      fMinTime = timing.getRunTime();
      fLastIsBest = true;
    } else {
      fLastIsBest = false;
    }
  }
  
  public List<Timing> getTimings() {
    return fTimings;
  }
  
  public boolean needsMoreRuns(BenchmarkParameters fParams) {
    if (getRuns() >= fParams.getMaxRunCount()) {
      return false;
    }
    if (getRuns() < fParams.getMinRunCount()) {
      return true;
    }
    
    if (fLastIsBest) {
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
    
    long maxAllowedTime = Math.round(fMinTime * (1 + fParams.getMaxDeviation()));
    int validSampleCount = 0;
    for (int i = 0; i < getRuns(); ++i) {
      if (fTimings.get(i).getRunTime() <= maxAllowedTime) {
        validSampleCount++;
      }
    }
    
    return validSampleCount < fParams.getMinSampleCount();
  }
  
  /**
   * @return estimated runtime in nanoseconds
   */
  public long getEstimatedRunTime() {
    return fMinTime;
  }
  
  public void clear() {
    fMinTime = Long.MAX_VALUE;
    fLastIsBest = true;
    fTimings.clear();
  }
  
  private int getRuns() {
    return fTimings.size();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int)(fMinTime ^ (fMinTime >>> 32));
    result = prime * result + ((fTimings == null) ? 0 : fTimings.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    BenchmarkTimings other = (BenchmarkTimings)obj;
    if (fMinTime != other.fMinTime) return false;
    if (fTimings == null) {
      if (other.fTimings != null) return false;
    } else if (!fTimings.equals(other.fTimings)) return false;
    return true;
  }
  
}
