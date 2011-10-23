package org.jbenchx.monitor;

import org.jbenchx.*;
import org.jbenchx.result.*;
import org.jbenchx.run.*;
import org.jbenchx.vm.*;

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
   * @param result a reference to where the benchmark result will be stored
   */
  public void init(int count, IBenchmarkResult result);
  
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
  public void run(IBenchmarkTask task, Timing timing, VmState vmStateDiff);
  
  /**
   * Notifies that the given benchmark has failed to run.
   */
  public void failed(IBenchmarkTask task);
  
  /**
   * Notifies that the given benchmark has completed.
   */
  public void done(IBenchmarkTask task);
  
  /**
   * Notifies that all benchmark tasks in the set have been processed.
   */
  public void finished();
  
  public class Stub implements IProgressMonitor {
    
    @Override
    public void init(int count, IBenchmarkResult result) {}
    
    @Override
    public void finished() {}
    
    @Override
    public void started(IBenchmarkTask task) {}
    
    @Override
    public void failed(IBenchmarkTask task) {}
    
    @Override
    public void done(IBenchmarkTask task) {}
    
    @Override
    public void run(IBenchmarkTask task, Timing timing, VmState vmStateDiff) {}
    
    @Override
    public void systemInfo(SystemInfo systemInfo) {}
    
  }
  
}
