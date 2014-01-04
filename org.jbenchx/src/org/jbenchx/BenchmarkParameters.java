package org.jbenchx;

import java.lang.reflect.Method;
import java.util.List;
import java.util.ListIterator;

import javax.annotation.CheckForNull;

import org.jbenchx.annotations.Bench;
import org.jbenchx.annotations.MemoryBench;
import org.jbenchx.util.ClassUtil;
import org.jbenchx.util.TimeUtil;

// TODO use builder pattern
public class BenchmarkParameters {
  
  private long    fTargetTimeNs;
  
  // FIXME remove
  private int     fDivisor;
  
  private int     fMinRunCount;
  
  private int     fMaxRunCount;
  
  private int     fMinSampleCount;
  
  private double  fMaxDeviation;
  
  private Boolean fMeasureMemory;
  
  public BenchmarkParameters() {
    this(-1, -1, -1, -1, -1, -1);
  }
  
  public BenchmarkParameters(long targetTimeNs, int divisor, int minRunCount, int maxRunCount, int minSampleCount, double maxDeviation) {
    fTargetTimeNs = targetTimeNs;
    fDivisor = divisor;
    fMinRunCount = minRunCount;
    fMaxRunCount = maxRunCount;
    fMinSampleCount = minSampleCount;
    fMaxDeviation = maxDeviation;
  }
  
  public BenchmarkParameters(Bench annotation) {
    this(annotation.targetTimeNs(), annotation.divisor(), annotation.minRunCount(), annotation.maxRunCount(), annotation.minSampleCount(), annotation
        .maxDeviation());
  }
  
  public boolean getMeasureMemory() {
    return Boolean.TRUE.equals(fMeasureMemory);
  }
  
  public int getDivisor() {
    return fDivisor;
  }
  
  public int getMinRunCount() {
    return fMinRunCount;
  }
  
  public int getMaxRunCount() {
    return fMaxRunCount;
  }
  
  public int getMinSampleCount() {
    return fMinSampleCount;
  }
  
  public double getMaxDeviation() {
    return fMaxDeviation;
  }
  
  public long getTargetTimeNs() {
    return fTargetTimeNs;
  }
  
  public void setTargetTimeNs(long targetTimeNs) {
    fTargetTimeNs = targetTimeNs;
  }
  
  public void setDivisor(int divisor) {
    fDivisor = divisor;
  }
  
  public void setMinRunCount(int minRunCount) {
    fMinRunCount = minRunCount;
  }
  
  public void setMaxRunCount(int maxRunCount) {
    fMaxRunCount = maxRunCount;
  }
  
  public void setMinSampleCount(int minSampleCount) {
    fMinSampleCount = minSampleCount;
  }
  
  public void setMaxDeviation(double maxDeviation) {
    fMaxDeviation = maxDeviation;
  }
  
  public static BenchmarkParameters merge(BenchmarkParameters paramsBase, BenchmarkParameters params) {
    long targetTimeNs = (params.getTargetTimeNs() == -1) ? paramsBase.getTargetTimeNs() : params.getTargetTimeNs();
    int divisor = (params.getDivisor() == -1) ? paramsBase.getDivisor() : params.getDivisor();
    int minRunCount = (params.getMinRunCount() == -1) ? paramsBase.getMinRunCount() : params.getMinRunCount();
    int maxRunCount = (params.getMaxRunCount() == -1) ? paramsBase.getMaxRunCount() : params.getMaxRunCount();
    int minSampleCount = (params.getMinSampleCount() == -1) ? paramsBase.getMinSampleCount() : params.getMinSampleCount();
    double maxDeviation = (params.getMaxDeviation() == -1) ? paramsBase.getMaxDeviation() : params.getMaxDeviation();
    BenchmarkParameters result = new BenchmarkParameters(targetTimeNs, divisor, minRunCount, maxRunCount, minSampleCount, maxDeviation);
    if (params.fMeasureMemory != null) {
      result.setMeasureMemory(params.getMeasureMemory());
    }
    return result;
  }
  
  @CheckForNull
  public static BenchmarkParameters read(Method method) {
    List<Bench> annotations = ClassUtil.findMethodAnnotations(method, Bench.class);
    List<MemoryBench> memoryAnnotations = ClassUtil.findMethodAnnotations(method, MemoryBench.class);
    if (annotations.isEmpty() && memoryAnnotations.isEmpty()) {
      return null;
    }
    ListIterator<Bench> iterator = annotations.listIterator(annotations.size());
    BenchmarkParameters result = new BenchmarkParameters();
    while (iterator.hasPrevious()) {
      result = merge(result, new BenchmarkParameters(iterator.previous()));
    }
    if (!memoryAnnotations.isEmpty()) {
      result.setMeasureMemory(true);
    }
    return result;
  }
  
  private void setMeasureMemory(boolean b) {
    fMeasureMemory = true;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + fDivisor;
    long temp;
    temp = Double.doubleToLongBits(fMaxDeviation);
    result = prime * result + (int)(temp ^ (temp >>> 32));
    result = prime * result + fMaxRunCount;
    result = prime * result + fMinRunCount;
    result = prime * result + fMinSampleCount;
    result = prime * result + (int)(fTargetTimeNs ^ (fTargetTimeNs >>> 32));
    return result;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    BenchmarkParameters other = (BenchmarkParameters)obj;
    if (fDivisor != other.fDivisor) return false;
    if (Double.doubleToLongBits(fMaxDeviation) != Double.doubleToLongBits(other.fMaxDeviation)) return false;
    if (fMaxRunCount != other.fMaxRunCount) return false;
    if (fMinRunCount != other.fMinRunCount) return false;
    if (fMinSampleCount != other.fMinSampleCount) return false;
    if (fTargetTimeNs != other.fTargetTimeNs) return false;
    if (fMeasureMemory != other.fMeasureMemory) return false;
    return true;
  }
  
  @Override
  public String toString() {
    return "BenchmarkParameters [fTargetTimeNs=" + fTargetTimeNs + ", fDivisor=" + fDivisor + ", fMinRunCount=" + fMinRunCount + ", fMaxRunCount="
        + fMaxRunCount + ", fMinSampleCount=" + fMinSampleCount + ", fMaxDeviation=" + fMaxDeviation + "]";
  }
  
  public static BenchmarkParameters getDefaults() {
    return new BenchmarkParameters(250 * TimeUtil.MS, 1, 10, 100, 8, 0.05);
  }
  
}
