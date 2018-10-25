package org.jbenchx.monitor;

import java.io.PrintWriter;

import org.jbenchx.Benchmark;
import org.jbenchx.Benchmark.SystemInfo;
import org.jbenchx.Benchmark.TaskResult;
import org.jbenchx.run.IBenchmarkTask;
import org.jbenchx.util.TimeUtil;
import org.jbenchx.vm.VmState;

public class ConsoleProgressMonitor implements IProgressMonitor {
  
  private int              tasksTotal = 0;
  
  private int              tasksDone  = 0;
  
  public ConsoleProgressMonitor() {
    System.out.println("Initializing Benchmarking Framework...");
  }
  
  @Override
  public void systemInfo(SystemInfo systemInfo) {
    System.out.println("Running on " + systemInfo.getOsInfo() + " " + systemInfo.getOsVersion());
    System.out.println("Max heap = " + systemInfo.getMaxHeapSize() + " System Benchmark = " + TimeUtil.toString(systemInfo.getSystemBenchmark()));
  }
  
  @Override
  public void init(int count) {
    tasksTotal = count;
    tasksDone = 0;
    System.out.println("Performing " + tasksTotal + " benchmarking tasks..");
  }
  
  @Override
  public void started(IBenchmarkTask task) {
    System.out.print("[" + tasksDone + "]\t" + task.getName());
    System.out.flush();
  }
  
  @Override
  public void done(IBenchmarkTask task, Benchmark.TaskResult result) {
    System.out.println("\t" + TimeUtil.toString(result.getEstimatedBenchmark()));
    tasksDone++;
  }
  
  @Override
  public void finished() {
    System.out.println("Success.");
  }
  
  @Override
  public void run(IBenchmarkTask task, Benchmark.Timing timing, VmState vmStateDiff) {
    if (VmState.EMPTY.equals(vmStateDiff)) {
      
      if (timing.getGcStats().getGcEventsCount() == 0) {
        System.out.print(".");
      } else {
        System.out.print("*");
      }
      
    } else {
      System.out.print("!");
    }
    System.out.flush();
  }
  
  @Override
  public void skipped(IBenchmarkTask task) {
    System.out.println("\tskipped");
    System.out.flush();
  }
  
  @Override
  public void failed(IBenchmarkTask task, TaskResult result) {
    System.out.println("\tfailed");
    System.out.flush();
    PrintWriter out = new PrintWriter(System.err);
    for (Benchmark.Error error: result.getErrorList()) {
      out.println(error.getMessage());
      for(String stackTrace: error.getStackTraceList()) {
        out.println(stackTrace);
      }
    }
    out.flush();
  }
  
}
