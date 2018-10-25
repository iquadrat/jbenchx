/*
 * Created on 01.10.2011
 *
 */
package org.jbenchx.run;

import org.jbenchx.Benchmark;
import org.jbenchx.IBenchmarkContext;

public interface IBenchmarkTask {

  public Benchmark.TaskResult run(IBenchmarkContext context);

  public String getName();

}