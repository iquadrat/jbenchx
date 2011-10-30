/*
 * Created on 01.10.2011
 *
 */
package org.jbenchx.test.result;

import static org.easymock.EasyMock.*;

import java.io.*;
import java.net.URL;
import java.util.*;

import org.easymock.*;
import org.jbenchx.BenchmarkParameters;
import org.jbenchx.result.*;
import org.jbenchx.run.*;
import org.jbenchx.util.*;
import org.jbenchx.vm.SystemInfo;
import org.junit.*;
import org.junit.Assert;

public class XmlResultSerializerTest {
  
  private final IMocksControl fControl;

  private static BenchmarkClassError createError() {
    BenchmarkClassError error = new BenchmarkClassError(XmlResultSerializerTest.class, "some failure warning here");
    StackTraceElement[] stackTrace = new StackTraceElement[2];
    stackTrace[0] = new StackTraceElement(XmlResultSerializerTest.class.getName(), "createError", "XmlResultSerializerTest.java", 12);
    stackTrace[1] = new StackTraceElement(XmlResultSerializerTest.class.getName(), "writeXmlResult_readXmlResult", "XmlResultSerializerTest.java", 33);
    error.setStackTrace(stackTrace);
    return error;
  }
  
  public XmlResultSerializerTest() {
    fControl = EasyMock.createStrictControl();
  }
  
  @Test
  public void writeXmlResult_readXmlResult() throws Exception {
    
    BenchmarkParameters params = BenchmarkParameters.getDefaults();
    BenchmarkTimings timings1 = new BenchmarkTimings();
    timings1.add(new Timing(1111, new GcStats(), new GcStats()));
    timings1.add(new Timing(1222, new GcStats(), new GcStats()));
    
    BenchmarkTimings timings2 = new BenchmarkTimings();
    timings2.add(new Timing(2111, new GcStats(), new GcStats()));
    
    ITimeProvider timeprovider = fControl.createMock("timeprovider", ITimeProvider.class);
    
    ParameterizationValues values = new ParameterizationValues(Arrays.<Object>asList(42, "foo"));
    
    IBenchmarkTask task1 = new BenchmarkTask("test.Task", "task1", Collections.<Class<?>>emptyList(), params, false, values,
        ParameterizationValues.EMPTY);
    IBenchmarkTask task2 = new BenchmarkTask("test.Task", "task2", Arrays.<Class<?>>asList(int.class, String.class), params, false,
        ParameterizationValues.EMPTY, values);
    
    BenchmarkWarning warning1 = new BenchmarkWarning("some warning reason here");
    BenchmarkClassError failure1 = createError();
    
    TaskResult result1 = new TaskResult(params, timings1, 11111L);
    result1.addWarning(warning1);
    TaskResult result2 = new TaskResult(params, timings1, 22222L, failure1);
    
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    XmlResultSerializer serializer = new XmlResultSerializer();
    
    GcStats postStats = new GcStats();
    postStats.addGc("foo", 2, 200);
    postStats.addGc("bar", 7, 142);
    timings2.add(new Timing(2222, new GcStats(), postStats));
    
    SystemInfo systemInfo = new SystemInfo(1, 2, 3, "linux", "2.3.4", 2, 10000000);
    
    // expectation
    expect(timeprovider.getCurrentTime()).andReturn(new Date(1317564808888L));
    expect(timeprovider.getCurrentTime()).andReturn(new Date(1317564809999L));
    expect(timeprovider.getCurrentTime()).andReturn(new Date(1317564809999L));
    
    
    fControl.replay();
    
    BenchmarkResult result = new BenchmarkResult(systemInfo, timeprovider);
    result.addResult(task1, result1);
    result.addResult(task2, result2);
    
    serializer.serialize(result, out);
    
    fControl.verify();
    
    URL resource = getClass().getResource(getClass().getSimpleName() + ".expected");
    InputStream in = resource.openStream();
    String expected = new String(IOUtil.readToEnd(in), "UTF-8");
    String actual = out.toString();
    
    Assert.assertEquals(expected, actual);

    IBenchmarkResult deserialized = serializer.deserialize(new ByteArrayInputStream(out.toByteArray()));
    
    Assert.assertEquals(result, deserialized);
    
  }
  
}
