package org.jbenchx.result;

import java.util.*;

import org.jbenchx.*;
import org.jbenchx.error.*;

public class BenchmarkResult {
  
  private final List<BenchmarkError>           fGeneralErrors = new ArrayList<BenchmarkError>();
  private final Map<BenchmarkTask, TaskResult> fResults       = new LinkedHashMap<BenchmarkTask, TaskResult>();
  
  public void addGeneralError(BenchmarkError error) {
    fGeneralErrors.add(error);
  }
  
  public void addResult(BenchmarkTask task, TaskResult result) {
    fResults.put(task, result);
  }
  
  public TaskResult getResult(BenchmarkTask task) {
    return fResults.get(task);
  }
  
  public Set<BenchmarkTask> getTasks() {
    return fResults.keySet();
  }
  
}
