package org.jbenchx.monitor;

import java.io.PrintWriter;

import javax.annotation.CheckForNull;

import org.jbenchx.result.BenchmarkFailure;
import org.jbenchx.result.IBenchmarkResult;
import org.jbenchx.result.Timing;
import org.jbenchx.run.IBenchmarkTask;
import org.jbenchx.util.TimeUtil;
import org.jbenchx.vm.SystemInfo;
import org.jbenchx.vm.VmState;

public class ConsoleProgressMonitor implements IProgressMonitor {
  
  private int              fTasksTotal = 0;
  
  private int              fTasksDone  = 0;
  
  @CheckForNull
  private IBenchmarkResult fResult     = null;
  
  public ConsoleProgressMonitor() {
    System.out.println("Initializing Benchmarking Framework...");
  }
  
  @Override
  public void systemInfo(SystemInfo systemInfo) {
    System.out.println("Running on " + systemInfo.getOsInfo() + " " + systemInfo.getOsVersion());
    System.out.println("Max heap = " + systemInfo.getMaxHeapSize() + " System Benchmark = " + TimeUtil.toString(systemInfo.getSystemBenchmark()));
  }
  
  @Override
  public void init(int count, IBenchmarkResult result) {
    fTasksTotal = count;
    fTasksDone = 0;
    fResult = result;
    System.out.println("Performing " + fTasksTotal + " benchmarking tasks..");
  }
  
  @Override
  public void started(IBenchmarkTask task) {
    System.out.print("[" + fTasksDone + "]\t" + task.getName());
    System.out.flush();
  }
  
  @Override
  public void done(IBenchmarkTask task) {
    if (fResult == null) {
      throw new IllegalStateException();
    }
    System.out.println("\t" + TimeUtil.toString(fResult.getResult(task).getEstimatedBenchmark()));
    fTasksDone++;
  }
  
  @Override
  public void finished() {
    System.out.println("Success.");
  }
  
  @Override
  public void run(IBenchmarkTask task, Timing timing, VmState vmStateDiff) {
    if (VmState.EMPTY.equals(vmStateDiff)) {
      
      if (timing.getGcEvents().isEmpty()) {
        System.out.print(".");
      } else {
        System.out.print("*");
      }
      
//      System.out.print(TimeUtil.toString(timing.getRunTime()));
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
  public void failed(IBenchmarkTask task) {
    if (fResult == null) {
      throw new IllegalStateException();
    }
    System.out.println("\tfailed");
    System.out.flush();
    PrintWriter out = new PrintWriter(System.err);
    for (BenchmarkFailure error: fResult.getResult(task).getFailures()) {
      error.print(out);
    }
    out.flush();
  }
  
}
