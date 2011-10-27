/*
 * Created on 01.10.2011
 *
 */
package org.jbenchx.result;

import java.util.*;

import javax.annotation.*;

import org.jbenchx.run.*;

public interface IBenchmarkResult {

  public ITaskResult getResult(IBenchmarkTask task);
  
  public List<BenchmarkFailure> getGeneralErrors();

  @CheckForNull
  public IBenchmarkTask findTask(String name);

  public Set<IBenchmarkTask> getTasks();

  public Date getStartTime();

  public Date getEndTime();

}