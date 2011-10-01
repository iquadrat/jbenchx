/*
 * Created on 01.10.2011
 *
 */
package org.jbenchx.test.monitor;

import org.easymock.*;
import org.jbenchx.*;
import org.jbenchx.monitor.*;
import org.jbenchx.result.*;
import org.junit.*;


public class XmlResultProgressMonitorTest {

  private final IMocksControl fControl;

  public XmlResultProgressMonitorTest() {
    fControl = EasyMock.createStrictControl();
  }

  @Test
  public void writeXmlResult() {
    IBenchmarkTask task1 = fControl.createMock("task1", IBenchmarkTask.class);
    IBenchmarkTask task2 = fControl.createMock("task2", IBenchmarkTask.class);
    ITaskResult result1= fControl.createMock("result1", ITaskResult.class);
    ITaskResult result2= fControl.createMock("result2", ITaskResult.class);

    XmlResultProgressMonitor monitor = new XmlResultProgressMonitor();
    BenchmarkResult result = new BenchmarkResult();
    monitor.init(2, result);
    result.addResult(task1, result1);
    result.addResult(task2, result2);

    monitor.started(task1);
    monitor.done(task1);
    monitor.started(task2);
    monitor.done(task2);

    monitor.finished();
  }

}
