/*
 * Created on 02.10.2011
 *
 */
package org.jbenchx.monitor;

import java.util.*;

import org.jbenchx.*;
import org.jbenchx.result.*;
import org.jbenchx.run.*;
import org.jbenchx.vm.*;


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
    for(IProgressMonitor monitor: fMonitors) {
      monitor.init(count, result);
    }
  }

  @Override
  public void started(IBenchmarkTask task) {
    for(IProgressMonitor monitor: fMonitors) {
      monitor.started(task);
    }
  }

  @Override
  public void run(IBenchmarkTask task, Timing timing, VmState vmStateDiff) {
    for(IProgressMonitor monitor: fMonitors) {
      monitor.run(task, timing, vmStateDiff);
    }
  }

  @Override
  public void failed(IBenchmarkTask task) {
    for(IProgressMonitor monitor: fMonitors) {
      monitor.failed(task);
    }
  }

  @Override
  public void done(IBenchmarkTask task) {
    for(IProgressMonitor monitor: fMonitors) {
      monitor.done(task);
    }
  }

  @Override
  public void finished() {
    for(IProgressMonitor monitor: fMonitors) {
      monitor.finished();
    }
  }

}
