/*
 * Created on 01.10.2011
 *
 */
package org.jbenchx;

import org.jbenchx.result.*;

public interface IBenchmarkTask {
  
  public abstract void run(BenchmarkResult result, IBenchmarkContext context);
  
  public abstract String getName();
  
}