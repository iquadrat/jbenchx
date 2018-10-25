package org.jbenchx.result;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.CheckForNull;

import org.jbenchx.Benchmark;
import org.jbenchx.Benchmark.Result;
import org.jbenchx.Benchmark.TaskResult;
import org.jbenchx.Benchmark.Timing;

public class ResultUtil {
  
  public static Benchmark.Timing createTiming(long runtimeNs, Benchmark.GcStats preStats, Benchmark.GcStats postStats) {
    Benchmark.Timing.Builder builder = Benchmark.Timing.newBuilder();
    builder.setRunTimeNs(runtimeNs);
    
    for (Entry<String, Benchmark.GcEvent> postGcEvent: postStats.getGcEventsMap().entrySet()) {
      Benchmark.GcEvent preGcEvent = preStats.getGcEventsMap().get(postGcEvent.getKey());
      long preCount = preGcEvent.getCount();
      long postCount = postGcEvent.getValue().getCount();
      if (preCount == postCount) {
        continue;
      }
      builder.getGcStatsBuilder().putGcEvents(postGcEvent.getKey(),
          Benchmark.GcEvent.newBuilder()
              .setCount(postCount - preCount)
              .setTimeNs(postGcEvent.getValue().getTimeNs() - preGcEvent.getTimeNs())
              .build());
    }
    
    return builder.build();
  }
  
  public static Benchmark.TaskResult createTaskResult(
      String taskName,
      Benchmark.Parameters params,
      List<Benchmark.Timing> timings,
      long iterationCount,
      double divisor,
      Benchmark.Error... errors) {
    return Benchmark.TaskResult.newBuilder()
        .setTaskName(taskName)
        .setIterationCount(iterationCount)
        .setDivisor(divisor)
        .addAllError(Arrays.asList(errors))
        .addAllTiming(timings)
        .setEstimatedBenchmark(estimateBenchmark(params, timings, iterationCount, divisor))
        .build();
  }
  
  private static double estimateBenchmark(Benchmark.Parameters params, List<Benchmark.Timing> timings, long iterationCount, double divisor) {
    double perIterationTimeRaw = 1.0 * estimatedRunTimeNs(timings) / iterationCount;
    double benchmarkTime = perIterationTimeRaw / divisor;
    return Math.max(0, benchmarkTime);
  }
  
  private static double estimatedRunTimeNs(List<Timing> timings) {
    // TODO use some lower percentile instead of minimum?
    long min = Long.MAX_VALUE;
    for (Timing timing: timings) {
      min = Math.min(min, timing.getRunTimeNs());
    }
    return min;
  }

  public static Benchmark.Error toFailure(Exception e) {
    Benchmark.Error.Builder builder = Benchmark.Error.newBuilder();
    builder.setMessage(e.toString());
    for(StackTraceElement element: e.getStackTrace()) {
      builder.addStackTrace(element.toString());
    }
    return builder.build();
  }

  @CheckForNull
  public static Benchmark.TaskResult getTaskResult(Result result, String name) {
    for(TaskResult taskResult: result.getTaskResultList()) {
      if (taskResult.getTaskName().equals(name)) {
        return taskResult;
      }
    }
    return null;
  }
  
}
