package org.jbenchx.result;

import java.util.*;

import javax.annotation.*;

import org.jbenchx.*;
import org.jbenchx.util.*;

public class BenchmarkResult implements IBenchmarkResult {

  private final List<BenchmarkFailure>           fGeneralErrors = new ArrayList<BenchmarkFailure>();

  private final Map<IBenchmarkTask, ITaskResult> fResults       = new LinkedHashMap<IBenchmarkTask, ITaskResult>();

  private final ITimeProvider                    fTimeProvider;

  private final Date                             fStartTime;

  private Date                                   fEndTime;

  public BenchmarkResult() {
    this(TimeUtil.getDefaultTimeProvider());
  }

  public BenchmarkResult(ITimeProvider timeProvider) {
    fTimeProvider = timeProvider;
    fStartTime = fTimeProvider.getCurrentTime();
    fEndTime = fStartTime;
  }

  public void addGeneralError(BenchmarkFailure error) {
    fGeneralErrors.add(error);
  }

  public void addResult(IBenchmarkTask task, ITaskResult result) {
    fResults.put(task, result);
    fEndTime = fTimeProvider.getCurrentTime();
  }

  @Override
  public ITaskResult getResult(IBenchmarkTask task) {
    return fResults.get(task);
  }

  @Override
  @CheckForNull
  public IBenchmarkTask findTask(String name) {
    for (IBenchmarkTask task: fResults.keySet()) {
      if (task.getName().equals(name)) {
        return task;
      }
    }
    return null;
  }

  @Override
  public Set<IBenchmarkTask> getTasks() {
    return fResults.keySet();
  }

  @Override
  public Date getStartTime() {
    return fStartTime;
  }

  @Override
  public Date getEndTime() {
    return fEndTime;
  }

}
