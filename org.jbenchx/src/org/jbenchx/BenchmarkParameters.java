package org.jbenchx;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import javax.annotation.*;

import org.jbenchx.annotations.*;

public class BenchmarkParameters {
  
  private final long   fTargetTimeNs;
  private final int    fDivisor;
  private final int    fMinRunCount;
  private final int    fMaxRunCount;
  private final int    fMinSampleCount;
  private final double fMaxDeviation;
  
  public BenchmarkParameters(long targetTimeNs, int divisor, int minRunCount, int maxRunCount, int minSampleCount, double maxDeviation) {
    fTargetTimeNs = targetTimeNs;
    fDivisor = divisor;
    fMinRunCount = minRunCount;
    fMaxRunCount = maxRunCount;
    fMinSampleCount = minSampleCount;
    fMaxDeviation = maxDeviation;
  }
  
  protected BenchmarkParameters(Bench annotation) {
    this(annotation.targetTimeNs(), annotation.divisor(), annotation.minRunCount(), annotation.maxRunCount(), annotation.minSampleCount(), annotation
        .maxDeviation());
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
  
  public static BenchmarkParameters merge(BenchmarkParameters paramsBase, BenchmarkParameters params) {
    long targetTimeNs = (params.getTargetTimeNs() == -1) ? paramsBase.getTargetTimeNs() : params.getTargetTimeNs();
    int divisor = (params.getDivisor() == -1) ? paramsBase.getDivisor() : params.getDivisor();
    int minRunCount = (params.getMinRunCount() == -1) ? paramsBase.getMinRunCount() : params.getMinRunCount();
    int maxRunCount = (params.getMaxRunCount() == -1) ? paramsBase.getMaxRunCount() : params.getMaxRunCount();
    int minSampleCount = (params.getMinSampleCount() == -1) ? paramsBase.getMinSampleCount() : params.getMinSampleCount();
    double maxDeviation = (params.getMaxDeviation() == -1) ? paramsBase.getMaxDeviation() : params.getMaxDeviation();
    return new BenchmarkParameters(targetTimeNs, divisor, minRunCount, maxRunCount, minSampleCount, maxDeviation);
  }
  
  @CheckForNull
  public static BenchmarkParameters read(Method method) {
    ArrayList<Bench> annotations = new ArrayList<Bench>();
    findAnnotations(method, Bench.class, annotations);
    ListIterator<Bench> iterator = annotations.listIterator(annotations.size());
    if (!iterator.hasPrevious()) return null;
    BenchmarkParameters result = new BenchmarkParameters(iterator.previous());
    while (iterator.hasPrevious()) {
      result = merge(result, new BenchmarkParameters(iterator.previous()));
    }
    return result;
  }
  
  private static <A extends Annotation> void findAnnotations(Method m, Class<A> annotationClass, List<A> result) throws SecurityException {
    A annotation = m.getAnnotation(annotationClass);
    if (annotation != null) {
      result.add(annotation);
    }
    Class<?> parent = m.getDeclaringClass().getSuperclass();
    if (parent != null) {
      try {
        Method superMethod = parent.getMethod(m.getName());
        findAnnotations(superMethod, annotationClass, result);
      } catch (NoSuchMethodException e) {
      }
    }
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
    return true;
  }

  @Override
  public String toString() {
    return "BenchmarkParameters [fTargetTimeNs=" + fTargetTimeNs + ", fDivisor=" + fDivisor + ", fMinRunCount=" + fMinRunCount + ", fMaxRunCount="
        + fMaxRunCount + ", fMinSampleCount=" + fMinSampleCount + ", fMaxDeviation=" + fMaxDeviation + "]";
  }
  
}
