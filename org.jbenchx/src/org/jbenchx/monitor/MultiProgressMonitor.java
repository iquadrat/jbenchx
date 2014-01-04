/*
 * Created on 02.10.2011
 *
 */
package org.jbenchx.monitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jbenchx.result.IBenchmarkResult;
import org.jbenchx.result.Timing;
import org.jbenchx.run.IBenchmarkTask;
import org.jbenchx.vm.SystemInfo;
import org.jbenchx.vm.VmState;

public class MultiProgressMonitor implements IProgressMonitor {
  
  private final List<IProgressMonitor> fMonitors = new ArrayList<IProgressMonitor>();
  
  public MultiProgressMonitor(IProgressMonitor... monitors) {
    fMonitors.addAll(Arrays.asList(monitors));
  }
  
  public void add(IProgressMonitor monitor) {
    fMonitors.add(monitor);
  }
  
  @Override
  public void init(int count, IBenchmarkResult result) {
    for (IProgressMonitor monitor: fMonitors) {
      monitor.init(count, result);
    }
  }
  
  @Override
  public void started(IBenchmarkTask task) {
    for (IProgressMonitor monitor: fMonitors) {
      monitor.started(task);
    }
  }
  
  @Override
  public void run(IBenchmarkTask task, Timing timing, VmState vmStateDiff) {
    for (IProgressMonitor monitor: fMonitors) {
      monitor.run(task, timing, vmStateDiff);
    }
  }
  
  @Override
  public void failed(IBenchmarkTask task) {
    for (IProgressMonitor monitor: fMonitors) {
      monitor.failed(task);
    }
  }
  
  @Override
  public void done(IBenchmarkTask task) {
    for (IProgressMonitor monitor: fMonitors) {
      monitor.done(task);
    }
  }
  
  @Override
  public void skipped(IBenchmarkTask task) {
    for (IProgressMonitor monitor: fMonitors) {
      monitor.skipped(task);
    }
  }
  
  @Override
  public void finished() {
    for (IProgressMonitor monitor: fMonitors) {
      monitor.finished();
    }
  }
  
  @Override
  public void systemInfo(SystemInfo systemInfo) {
    for (IProgressMonitor monitor: fMonitors) {
      monitor.systemInfo(systemInfo);
    }
  }
  
}
