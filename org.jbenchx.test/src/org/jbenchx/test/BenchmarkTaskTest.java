/*
 * Created on 01.10.2011
 *
 */
package org.jbenchx.test;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

import org.jbenchx.BenchmarkContext;
import org.jbenchx.BenchmarkParameters;
import org.jbenchx.IBenchmarkContext;
import org.jbenchx.annotations.Bench;
import org.jbenchx.monitor.IProgressMonitor;
import org.jbenchx.result.BenchmarkResult;
import org.jbenchx.result.ITaskResult;
import org.jbenchx.run.BenchmarkTask;
import org.jbenchx.run.IBenchmarkTask;
import org.jbenchx.run.ParameterizationValues;
import org.jbenchx.vm.SystemInfo;
import org.junit.Assert;
import org.junit.Test;


public class BenchmarkTaskTest extends BenchmarkTestCase {

  private static final ParameterizationValues NO_ARGS = ParameterizationValues.EMPTY;

  public static class TestBench {

    @Bench
    public Object empty() {
      return null;
    }

  }

  @Test
  public void warningWhenIterationToFast() throws Throwable {
    Method emptyMethod = TestBench.class.getMethod("empty");
    IBenchmarkTask task = new BenchmarkTask(TestBench.class, emptyMethod, BenchmarkParameters.getDefaults(), false, NO_ARGS, NO_ARGS);
    BenchmarkResult result = new BenchmarkResult(SystemInfo.create(1, 2, 3.0));
    IBenchmarkContext context = new BenchmarkContext(IProgressMonitor.DUMMY, SystemInfo.create(10, 100, 100), BenchmarkContext.RUN_ALL);
    task.run(result, context);
    ITaskResult taskResult = result.getResult(task);
    if (!taskResult.getFailures().isEmpty()) {
      Assert.fail(taskResult.getFailures().toString());
    }
    Assert.assertEquals(1, taskResult.getWarnings().size());
    Pattern pattern = Pattern.compile("Runtime of single iteration too short: [0-9]*ns, increase work in single iteration to run at least 1000ns");
    String reason = taskResult.getWarnings().get(0).getReason();
    if (!pattern.matcher(reason).matches()) {
      Assert.fail("Expected: "+pattern.toString()+" but was: "+reason);
    }
  }

}
