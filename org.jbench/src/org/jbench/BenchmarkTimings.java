package org.jbench;

import java.util.*;

public class BenchmarkTimings {
  
  private final double       fMaxDeviation;
  private final int          fMinRunCount;
  private final int          fMaxRunCount;
  private final int          fMinSampleCount;
  
  private final List<Timing> fTimings;
  
  private int                fRuns       = 0;
  private long               fMinTime    = Long.MAX_VALUE;
  private boolean            fLastIsBest = true;
  
  public BenchmarkTimings(int minRunCount, int maxRunCount, int minSamplesCount, double maxDeviation) {
    fMinRunCount = minRunCount;
    fMaxRunCount = maxRunCount;
    fMinSampleCount = minSamplesCount;
    fMaxDeviation = maxDeviation;
    fTimings = new ArrayList<Timing>(maxRunCount);
  }
  
  public void add(Timing timing) {
    fRuns++;
    fTimings.add(timing);
    if (timing.getRunTime() < fMinTime) {
      fMinTime = timing.getRunTime();
      fLastIsBest = true;
    } else {
      fLastIsBest = false;
    }
  }
  
  public boolean needsMoreRuns() {
    if (fRuns >= getMaxRunCount()) return false;
    if (fRuns < getMinRunCount()) return true;
    
    if (fLastIsBest) return true;
    
//    // find last local maxmimum
//    long timing = fTimings.get(fTimings.size() - 1);
//    int localMaxIdx = -1;
//    for (int i = fTimings.size() - 1; i >= 0; --i) {
//      if (fTimings.get(i) < timing) {
//        localMaxIdx = i + 1;
//        break;
//      }
//    }
    
    long maxAllowedTime = (long)Math.round(fMinTime * (1 + fMaxDeviation));
    int validSampleCount = 0;
    for (int i = 0; i < fRuns; ++i) {
      if (fTimings.get(i).getRunTime() <= maxAllowedTime) {
        validSampleCount++;
      }
    }
    
    return validSampleCount < getMinSampleCount();
  }
  
  private int getMinSampleCount() {
    return fMinSampleCount;
  }
  
  private int getMinRunCount() {
    return fMinRunCount;
  }
  
  private int getMaxRunCount() {
    return fMaxRunCount;
  }
  
  public double getEstimatedTime() {
    return fMinTime;
//    long sum = 0;
//    int validSampleCount = 0;
//    long maxAllowedTime = (long)Math.round(fMinTime * (1 + fMaxDeviation));
//    
//    for (int i = 0; i < fRuns; ++i) {
//      Long timings = fTimings.get(i);
//      if (timings <= maxAllowedTime) {
//        validSampleCount++;
//        sum += timings;
//      }
//    }
//    
//    return 1.0 * sum / validSampleCount;
  }
  
  public void clear() {
    fRuns = 0;
    fMinTime = Long.MAX_VALUE;
    fLastIsBest = true;
    fTimings.clear();
  }
  
}
