/*
 * Created on 01.10.2011
 *
 */
package org.jbenchx.test;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

import org.jbenchx.Benchmark.TaskResult;
import org.jbenchx.BenchmarkContext;
import org.jbenchx.Benchmarks;
import org.jbenchx.IBenchmarkContext;
import org.jbenchx.annotations.Bench;
import org.jbenchx.monitor.IProgressMonitor;
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
    IBenchmarkTask task = new BenchmarkTask(TestBench.class, emptyMethod, Benchmarks.getDefaultParameters(), false, NO_ARGS, NO_ARGS);
    IBenchmarkContext context = new BenchmarkContext(IProgressMonitor.DUMMY, SystemInfo.create(10, 100, 100), BenchmarkContext.RUN_ALL);
    TaskResult taskResult = task.run(context);
    if (!taskResult.getErrorList().isEmpty()) {
      Assert.fail(taskResult.getErrorList().toString());
    }
    Assert.assertEquals(1, taskResult.getWarningList().size());
    Pattern pattern = Pattern.compile("Runtime of single iteration too short: [0-9]*ns, increase work in single iteration to run at least 1000ns");
    String reason = taskResult.getWarningList().get(0).getMessage();
    if (!pattern.matcher(reason).matches()) {
      Assert.fail("Expected: "+pattern.toString()+" but was: "+reason);
    }
  }

}
