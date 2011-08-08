package org.jbenchx;

import java.util.*;

public class BenchmarkTimings {
  
  private final BenchmarkParameters fParams;
  private final List<Timing>        fTimings;
  
  private int                       fRuns       = 0;
  private long                      fMinTime    = Long.MAX_VALUE;
  private boolean                   fLastIsBest = true;
  
  public BenchmarkTimings(BenchmarkParameters params) {
    fParams = params;
    fTimings = new ArrayList<Timing>(params.getMaxRunCount());
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
    if (fRuns >= fParams.getMaxRunCount()) return false;
    if (fRuns < fParams.getMinRunCount()) return true;
    
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
    
    long maxAllowedTime = (long)Math.round(fMinTime * (1 + fParams.getMaxDeviation()));
    int validSampleCount = 0;
    for (int i = 0; i < fRuns; ++i) {
      if (fTimings.get(i).getRunTime() <= maxAllowedTime) {
        validSampleCount++;
      }
    }
    
    return validSampleCount < fParams.getMinSampleCount();
  }
  
  public double getEstimatedRunTime() {
    return 1.0 * fMinTime / fParams.getDivisor();
  }
  
  public void clear() {
    fRuns = 0;
    fMinTime = Long.MAX_VALUE;
    fLastIsBest = true;
    fTimings.clear();
  }
  
}
