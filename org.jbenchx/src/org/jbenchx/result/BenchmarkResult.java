package org.jbenchx.result;

import java.util.*;

import javax.annotation.*;

import org.jbenchx.*;

public class BenchmarkResult {

  private final List<BenchmarkFailure>         fGeneralErrors = new ArrayList<BenchmarkFailure>();

  private final Map<BenchmarkTask, TaskResult> fResults       = new LinkedHashMap<BenchmarkTask, TaskResult>();

  public void addGeneralError(BenchmarkFailure error) {
    fGeneralErrors.add(error);
  }

  public void addResult(BenchmarkTask task, TaskResult result) {
    fResults.put(task, result);
  }

  public TaskResult getResult(BenchmarkTask task) {
    return fResults.get(task);
  }

  @CheckForNull
  public BenchmarkTask findTask(String name) {
    for (BenchmarkTask task: fResults.keySet()) {
      if (task.getName().equals(name)) {
        return task;
      }
    }
    return null;
  }

  public Set<BenchmarkTask> getTasks() {
    return fResults.keySet();
  }

}
