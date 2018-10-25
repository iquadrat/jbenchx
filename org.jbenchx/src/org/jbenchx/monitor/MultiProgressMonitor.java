/*
 * Created on 02.10.2011
 *
 */
package org.jbenchx.monitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jbenchx.Benchmark;
import org.jbenchx.Benchmark.SystemInfo;
import org.jbenchx.Benchmark.TaskResult;
import org.jbenchx.run.IBenchmarkTask;
import org.jbenchx.vm.VmState;

public class MultiProgressMonitor implements IProgressMonitor {
  
  private final List<IProgressMonitor> monitors = new ArrayList<IProgressMonitor>();
  
  public MultiProgressMonitor(IProgressMonitor... monitors) {
    this.monitors.addAll(Arrays.asList(monitors));
  }
  
  public void add(IProgressMonitor monitor) {
    monitors.add(monitor);
  }
  
  @Override
  public void init(int count) {
    for (IProgressMonitor monitor: monitors) {
      monitor.init(count);
    }
  }
  
  @Override
  public void started(IBenchmarkTask task) {
    for (IProgressMonitor monitor: monitors) {
      monitor.started(task);
    }
  }
  
  @Override
  public void run(IBenchmarkTask task, Benchmark.Timing timing, VmState vmStateDiff) {
    for (IProgressMonitor monitor: monitors) {
      monitor.run(task, timing, vmStateDiff);
    }
  }
  
  @Override
  public void failed(IBenchmarkTask task, TaskResult result) {
    for (IProgressMonitor monitor: monitors) {
      monitor.failed(task, result);
    }
  }
  
  @Override
  public void done(IBenchmarkTask task, TaskResult result) {
    for (IProgressMonitor monitor: monitors) {
      monitor.done(task, result);
    }
  }
  
  @Override
  public void skipped(IBenchmarkTask task) {
    for (IProgressMonitor monitor: monitors) {
      monitor.skipped(task);
    }
  }
  
  @Override
  public void finished() {
    for (IProgressMonitor monitor: monitors) {
      monitor.finished();
    }
  }
  
  @Override
  public void systemInfo(SystemInfo systemInfo) {
    for (IProgressMonitor monitor: monitors) {
      monitor.systemInfo(systemInfo);
    }
  }
  
}
