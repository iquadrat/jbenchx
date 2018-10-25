package org.jbenchx;

import java.lang.reflect.Method;
import java.util.List;
import java.util.ListIterator;

import javax.annotation.CheckForNull;

import org.jbenchx.Benchmark.Parameters.Builder;
import org.jbenchx.annotations.Bench;
import org.jbenchx.annotations.MemoryBench;
import org.jbenchx.util.ClassUtil;
import org.jbenchx.util.TimeUtil;

public class Benchmarks {
  
  public static Benchmark.Parameters getDefaultParameters() {
    return Benchmark.Parameters.newBuilder()
        .setTargetTimeNs(250 * TimeUtil.MS)
        .setMinRunCount(10)
        .setMaxRunCount(100)
        .setMinSampleCount(8)
        .setMaxDeviation(0.05)
        .setMaxRestartCount(15)
        .build();
  }
  
  @CheckForNull
  public static Benchmark.Parameters getParamsFrom(Method method) {
    List<Bench> annotations = ClassUtil.findMethodAnnotations(method, Bench.class);
    List<MemoryBench> memoryAnnotations = ClassUtil.findMethodAnnotations(method, MemoryBench.class);
    if (annotations.isEmpty() && memoryAnnotations.isEmpty()) {
      return null;
    }
    ListIterator<Bench> iterator = annotations.listIterator(annotations.size());
    Benchmark.Parameters.Builder builder = Benchmark.Parameters.newBuilder();
    while (iterator.hasPrevious()) {
      builder.mergeFrom(getParamsFrom(iterator.previous()));
    }
    if (!memoryAnnotations.isEmpty()) {
      builder.setMeasureMemory(true);
    }
    return builder.build();
  }
  
  private static Benchmark.Parameters getParamsFrom(Bench annotation) {
    Builder builder = Benchmark.Parameters.newBuilder();
    if (annotation.targetTimeNs() != -1) {
      builder.setTargetTimeNs(annotation.targetTimeNs());
    }
    if (annotation.minRunCount() != -1) {
      builder.setMinRunCount(annotation.minRunCount());
    }
    if (annotation.maxRunCount() != -1) {
      builder.setMaxRunCount(annotation.maxRunCount());
    }
    if (annotation.minSampleCount() != -1) {
      builder.setMinSampleCount(annotation.minSampleCount());
    }
    if (annotation.maxDeviation() != -1) {
      builder.setMaxDeviation(annotation.maxDeviation());
    }
    return builder.build();
  }
  
}
