package org.jbenchx.monitor;

import org.jbenchx.Benchmark;
import org.jbenchx.Benchmark.TaskResult;
import org.jbenchx.run.IBenchmarkTask;
import org.jbenchx.vm.SystemInfo;
import org.jbenchx.vm.VmState;

public interface IProgressMonitor {
  
  public static final IProgressMonitor DUMMY = new Stub();
  
  /**
   * Notifies that the system info has been calculated.
   */
  public void systemInfo(SystemInfo systemInfo);
  
  /**
   * Notifies that a new set of benchmarks is going to be run.
   *
   * @param count the number of benchmarks in the set
   */
  public void init(int count);
  
  /**
   * Notifies that the given benchmark task is going to be run now.
   */
  public void started(IBenchmarkTask task);
  
  /**
   * Notifies that an iteration of the given benchmark task has been completed.
   * This will be invoked multiple times for the same benchmark task until enough samples
   * have been made for the task.
   *
   * @param timing the timing statistics of running the benchmark task
   * @param vmStateDiff the difference of the vm before and after running the benchmark
   */
  public void run(IBenchmarkTask task, Benchmark.Timing timing, VmState vmStateDiff);
  
  /**
   * Notifies that the given benchmark has been skipped.
   */
  public void skipped(IBenchmarkTask task);
  
  /**
   * Notifies that the given benchmark has failed to run.
   */
  public void failed(IBenchmarkTask task, TaskResult result);
  
  /**
   * Notifies that the given benchmark has completed.
   */
  public void done(IBenchmarkTask task, TaskResult result);
  
  /**
   * Notifies that all benchmark tasks in the set have been processed.
   */
  public void finished();
  
  public class Stub implements IProgressMonitor {
    
    @Override
    public void init(int count) {}
    
    @Override
    public void finished() {}
    
    @Override
    public void started(IBenchmarkTask task) {}
    
    @Override
    public void failed(IBenchmarkTask task, TaskResult result) {}
    
    @Override
    public void skipped(IBenchmarkTask task) {}
    
    @Override
    public void done(IBenchmarkTask task, TaskResult result) {}
    
    @Override
    public void run(IBenchmarkTask task, Benchmark.Timing timing, VmState vmStateDiff) {}
    
    @Override
    public void systemInfo(SystemInfo systemInfo) {}
    
  }
  
}
