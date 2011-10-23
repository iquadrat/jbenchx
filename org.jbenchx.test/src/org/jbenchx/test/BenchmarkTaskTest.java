/*
 * Created on 01.10.2011
 *
 */
package org.jbenchx.test;

import java.util.regex.*;

import org.jbenchx.*;
import org.jbenchx.annotations.*;
import org.jbenchx.monitor.*;
import org.jbenchx.result.*;
import org.jbenchx.run.*;
import org.junit.*;


public class BenchmarkTaskTest extends BenchmarkTestCase {

  private static final ParameterizationValues NO_ARGS = ParameterizationValues.EMPTY;

  public static class TestBench {

    @Bench
    public Object empty() {
      return null;
    }

  }

  @Test
  public void warningWhenIterationToFast() {
    IBenchmarkTask task = new BenchmarkTask("empty", TestBench.class.getName(), "empty", BenchmarkParameters.getDefaults(), false, NO_ARGS);
    BenchmarkResult result = new BenchmarkResult();
    IBenchmarkContext context = new BenchmarkContext(IProgressMonitor.DUMMY, SystemInfo.create(10, 100));
    task.run(result, context);
    ITaskResult taskResult = result.getResult(task);
    Assert.assertTrue(taskResult.getFailures().isEmpty());
    Assert.assertEquals(1, taskResult.getWarnings().size());
    Pattern pattern = Pattern.compile("Runtime of single iteration too short: [0-9]*ns, increase work in single iteration to run at least 1000ns");
    String reason = taskResult.getWarnings().get(0).getReason();
    if (!pattern.matcher(reason).matches()) {
      Assert.fail("Expected: "+pattern.toString()+" but was: "+reason);
    }
  }

}
