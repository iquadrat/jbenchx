package org.jbenchx.monitor;

import org.jbenchx.*;
import org.jbenchx.result.*;
import org.jbenchx.vm.*;

public interface IProgressMonitor {
  
  public static final IProgressMonitor DUMMY = new Stub();
  
  public void init(int count, BenchmarkResult result);
  
  public void started(BenchmarkTask task);
  
  public void run(BenchmarkTask task, Timing timing, VmState vmStateDiff);
  
  public void failed(BenchmarkTask task);
  
  public void done(BenchmarkTask task);
  
  public void finished();
  
  public class Stub implements IProgressMonitor {
    
    @Override
    public void init(int count, BenchmarkResult result) {}
    
    @Override
    public void finished() {}
    
    @Override
    public void started(BenchmarkTask task) {}
    
    @Override
    public void failed(BenchmarkTask task) {}
    
    @Override
    public void done(BenchmarkTask task) {}
    
    @Override
    public void run(BenchmarkTask task, Timing timing, VmState vmStateDiff) {}
    
  }
  
}
