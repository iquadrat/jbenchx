package org.jbenchx.monitor;

import java.io.*;

import javax.annotation.*;

import org.jbenchx.*;
import org.jbenchx.result.*;
import org.jbenchx.run.*;
import org.jbenchx.util.*;
import org.jbenchx.vm.*;

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
    System.out.println("Running on " + systemInfo.getOsInfo() + " " + systemInfo.getOsInfo());
    System.out.println("Max heap = " + systemInfo.getMaxHeapSize() + " System Benchmark = " + systemInfo.getSystemBenchmark());
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
      
      if (timing.getGcNames().isEmpty()) {
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
