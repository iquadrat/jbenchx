package org.jbenchx.monitor;

import java.io.*;

import javax.annotation.*;

import org.jbenchx.*;
import org.jbenchx.error.*;
import org.jbenchx.result.*;
import org.jbenchx.util.*;
import org.jbenchx.vm.*;

public class ConsoleProgressMonitor implements IProgressMonitor {
  
  private int             fTasksTotal = 0;
  private int             fTasksDone  = 0;
  
  @CheckForNull
  private BenchmarkResult fResult     = null;
  
  public ConsoleProgressMonitor() {
    System.out.println("Initializing Benchmarking Framework...");
  }
  
  @Override
  public void init(int count, BenchmarkResult result) {
    fTasksTotal = count;
    fTasksDone = 0;
    fResult = result;
    System.out.println("Performing " + fTasksTotal + " benchmarking tasks..");
  }
  
  @Override
  public void started(BenchmarkTask task) {
    System.out.print("[" + fTasksDone + "]\t" + task.getName());
    System.out.flush();
  }
  
  @Override
  public void done(BenchmarkTask task) {
    if (fResult == null) throw new IllegalStateException();
    System.out.println("\t" + TimeUtil.toString(fResult.getResult(task).getEstimatedBenchmark()));
    fTasksDone++;
  }
  
  @Override
  public void finished() {
    System.out.println("Success.");
  }
  
  @Override
  public void run(BenchmarkTask task, Timing timing, VmState vmStateDiff) {
    if (VmState.EMPTY.equals(vmStateDiff)) {
      System.out.print(".");
//      System.out.print(TimeUtil.toString(timing.getRunTime()));
    } else {
      System.out.print("!");
    }
    System.out.flush();
  }
  
  @Override
  public void failed(BenchmarkTask task) {
    if (fResult == null) throw new IllegalStateException();
    System.out.println("\tfailed");
    System.out.flush();
    PrintWriter out = new PrintWriter(System.err);
    for(BenchmarkFailure error: fResult.getResult(task).getErrors()) {
      error.print(out);
    }
    out.flush();
  }
  
}
