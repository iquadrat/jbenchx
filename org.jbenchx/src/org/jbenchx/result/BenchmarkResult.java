package org.jbenchx.result;

import java.util.*;

import javax.annotation.CheckForNull;

import org.jbenchx.BenchmarkContext;
import org.jbenchx.run.IBenchmarkTask;
import org.jbenchx.util.*;
import org.jbenchx.vm.SystemInfo;

import edu.umd.cs.findbugs.annotations.Nullable;

public class BenchmarkResult implements IBenchmarkResult {
  
  private final String                 fVersion       = BenchmarkContext.VERSION;
  
  private final List<BenchmarkFailure> fGeneralErrors = new ArrayList<BenchmarkFailure>();
  
  private final ResultMap              fResults       = new ResultMap();
  
  @Nullable 
  private final SystemInfo             fSystemInfo;
  
  private final Date                   fStartTime;
  
  private Date                         fEndTime;
  
  @CheckForNull
  private transient ITimeProvider      fTimeProvider  = null;
  
  public BenchmarkResult(@Nullable SystemInfo systemInfo) {
    this(systemInfo, TimeUtil.getDefaultTimeProvider());
  }
  
  public BenchmarkResult(@Nullable SystemInfo systemInfo, ITimeProvider timeProvider) {
    fSystemInfo = systemInfo;
    fTimeProvider = timeProvider;
    fStartTime = fTimeProvider.getCurrentTime();
    fEndTime = fStartTime;
  }
  
  public void addGeneralError(BenchmarkFailure error) {
    checkNotDeserialized();
    fGeneralErrors.add(error);
  }
  
  public void addResult(IBenchmarkTask task, ITaskResult result) {
    checkNotDeserialized();
    fResults.register(task, result);
    fEndTime = fTimeProvider.getCurrentTime();
  }
  
  private void checkNotDeserialized() {
    if (fTimeProvider != null) return;
    throw new IllegalStateException("Cannot add further items to deserialied " + BenchmarkResult.class.getSimpleName());
  }
  
  @Override
  public List<BenchmarkFailure> getGeneralErrors() {
    return fGeneralErrors;
  }
  
  @Override
  public ITaskResult getResult(IBenchmarkTask task) {
    return fResults.lookup(task);
  }
  
  @Override
  @CheckForNull
  public IBenchmarkTask findTask(String name) {
    for (IBenchmarkTask task: fResults.getAllTasks()) {
      if (task.getName().equals(name)) {
        return task;
      }
    }
    return null;
  }
  
  @Override
  public Set<IBenchmarkTask> getTasks() {
    return fResults.getAllTasks();
  }
  
  @Override
  public Date getStartTime() {
    return fStartTime;
  }
  
  @Override
  public Date getEndTime() {
    return fEndTime;
  }
  
  @Override
  public SystemInfo getSystemInfo() {
    return fSystemInfo;
  }
  
  @Override
  public String getVersion() {
    return fVersion;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fEndTime == null) ? 0 : fEndTime.hashCode());
    result = prime * result + ((fGeneralErrors == null) ? 0 : fGeneralErrors.hashCode());
    result = prime * result + ((fResults == null) ? 0 : fResults.hashCode());
    result = prime * result + ((fStartTime == null) ? 0 : fStartTime.hashCode());
    result = prime * result + ((fSystemInfo == null) ? 0 : fSystemInfo.hashCode());
    result = prime * result + ((fVersion == null) ? 0 : fVersion.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    BenchmarkResult other = (BenchmarkResult)obj;
    if (fEndTime == null) {
      if (other.fEndTime != null) return false;
    } else if (!fEndTime.equals(other.fEndTime)) return false;
    if (fGeneralErrors == null) {
      if (other.fGeneralErrors != null) return false;
    } else if (!fGeneralErrors.equals(other.fGeneralErrors)) return false;
    if (fResults == null) {
      if (other.fResults != null) return false;
    } else if (!fResults.equals(other.fResults)) return false;
    if (fStartTime == null) {
      if (other.fStartTime != null) return false;
    } else if (!fStartTime.equals(other.fStartTime)) return false;
    if (fSystemInfo == null) {
      if (other.fSystemInfo != null) return false;
    } else if (!fSystemInfo.equals(other.fSystemInfo)) return false;
    if (fVersion == null) {
      if (other.fVersion != null) return false;
    } else if (!fVersion.equals(other.fVersion)) return false;
    return true;
  }
  
}
