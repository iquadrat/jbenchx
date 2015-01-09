/*
 * Created on 01.10.2011
 *
 */
package org.jbenchx.run;

import org.jbenchx.IBenchmarkContext;
import org.jbenchx.result.BenchmarkResult;

public interface IBenchmarkTask {

  public void run(BenchmarkResult result, IBenchmarkContext context);

  public String getName();

}