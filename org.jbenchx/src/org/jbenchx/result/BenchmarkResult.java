package org.jbenchx.result;

import java.util.*;

import javax.annotation.*;

import org.jbenchx.*;

public class BenchmarkResult implements IBenchmarkResult {

  private final List<BenchmarkFailure>           fGeneralErrors = new ArrayList<BenchmarkFailure>();

  private final Map<IBenchmarkTask, ITaskResult> fResults       = new LinkedHashMap<IBenchmarkTask, ITaskResult>();

  public void addGeneralError(BenchmarkFailure error) {
    fGeneralErrors.add(error);
  }

  public void addResult(IBenchmarkTask task, ITaskResult result) {
    fResults.put(task, result);
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

}
