/*
 * Created on 01.10.2011
 *
 */
package org.jbenchx.test.monitor;

import static org.easymock.EasyMock.*;

import java.io.*;
import java.net.*;
import java.util.*;

import org.easymock.*;
import org.jbenchx.*;
import org.jbenchx.monitor.*;
import org.jbenchx.result.*;
import org.jbenchx.run.*;
import org.jbenchx.util.*;
import org.junit.*;
import org.junit.Assert;

public class XmlResultProgressMonitorTest {

  private final IMocksControl fControl;

  public XmlResultProgressMonitorTest() {
    fControl = EasyMock.createStrictControl();
  }

  @Test
  public void writeXmlResult() throws Exception {
    IBenchmarkTask task1 = fControl.createMock("task1", IBenchmarkTask.class);
    IBenchmarkTask task2 = fControl.createMock("task2", IBenchmarkTask.class);
    ITaskResult result1 = fControl.createMock("result1", ITaskResult.class);
    ITaskResult result2 = fControl.createMock("result2", ITaskResult.class);
    ITimeProvider timeprovider = fControl.createMock("timeprovider", ITimeProvider.class);

    ByteArrayOutputStream out = new ByteArrayOutputStream();

    XmlResultSerializer serializer = new XmlResultSerializer();

    BenchmarkParameters params = BenchmarkParameters.getDefaults();
    BenchmarkTimings timings1 = new BenchmarkTimings(params);
    timings1.add(new Timing(1111, new GcStats(), new GcStats()));
    timings1.add(new Timing(1222, new GcStats(), new GcStats()));

    BenchmarkTimings timings2 = new BenchmarkTimings(params);
    timings2.add(new Timing(2111, new GcStats(), new GcStats()));

    GcStats postStats = new GcStats();
    postStats.addGc("foo", 2, 200);
    postStats.addGc("bar", 7, 142);
    timings2.add(new Timing(2222, new GcStats(), postStats));

    BenchmarkWarning warning1 = new BenchmarkWarning("some warning reason here");

    BenchmarkClassError failure1 = new BenchmarkClassError(getClass(), "some failure warning here");

    // expectation
    expect(timeprovider.getCurrentTime()).andReturn(new Date(1317564808888L));
    expect(timeprovider.getCurrentTime()).andReturn(new Date(1317564809999L));
    expect(timeprovider.getCurrentTime()).andReturn(new Date(1317564809999L));

    expect(task1.getName()).andReturn("task1");
    expect(result1.getIterationCount()).andReturn(11111L);
    expect(result1.getEstimatedBenchmark()).andReturn(11.1);
    expect(result1.getTimings()).andReturn(timings1).anyTimes();
    expect(result1.getFailures()).andReturn(Collections.<BenchmarkFailure>emptyList());
    expect(result1.getWarnings()).andReturn(Arrays.<BenchmarkWarning>asList(warning1)).anyTimes();

    expect(task2.getName()).andReturn("task2");
    expect(result2.getIterationCount()).andReturn(22222L);
    expect(result2.getEstimatedBenchmark()).andReturn(222.2);
    expect(result2.getTimings()).andReturn(timings2).anyTimes();
    expect(result2.getFailures()).andReturn(Arrays.<BenchmarkFailure>asList(failure1)).anyTimes();
    expect(result2.getWarnings()).andReturn(Collections.<BenchmarkWarning>emptyList());

    fControl.replay();

    BenchmarkResult result = new BenchmarkResult(timeprovider);
    result.addResult(task1, result1);
    result.addResult(task2, result2);

    serializer.serialize(result, out);

    fControl.verify();

    URL resource = getClass().getResource(getClass().getSimpleName() + ".xml");
    InputStream in = resource.openStream();
    if (!IOUtil.hasSameContents(in, new ByteArrayInputStream(out.toByteArray()))) {
      Assert.fail("expected: " + new String(IOUtil.readToEnd(resource.openStream())) + " but was: " + out.toString());
    }
    in.close();

  }

}
