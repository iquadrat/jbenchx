/*
 * Created on 01.10.2011
 *
 */
package org.jbenchx.run;

import org.jbenchx.*;
import org.jbenchx.result.*;

public interface IBenchmarkTask {

  public void run(BenchmarkResult result, IBenchmarkContext context);

  public String getName();

}