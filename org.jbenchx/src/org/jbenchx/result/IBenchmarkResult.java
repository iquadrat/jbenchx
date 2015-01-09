/*
 * Created on 01.10.2011
 *
 */
package org.jbenchx.result;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import org.jbenchx.run.IBenchmarkTask;
import org.jbenchx.vm.SystemInfo;

public interface IBenchmarkResult {

  public ITaskResult getResult(IBenchmarkTask task);
  
  public List<BenchmarkFailure> getGeneralErrors();

  @CheckForNull
  public IBenchmarkTask findTask(String name);

  public Set<IBenchmarkTask> getTasks();

  public Date getStartTime();

  public Date getEndTime();
  
  @Nullable
  public SystemInfo getSystemInfo();
  
  public String getVersion();

}