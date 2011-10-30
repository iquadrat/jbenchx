package org.jbenchx.result;

import java.util.*;
import java.util.Map.Entry;

import org.jbenchx.run.IBenchmarkTask;

public class ResultMap extends AbstractCollection<BenchmarkResultEntry> {
  
  private final Map<IBenchmarkTask, ITaskResult> fResults = new LinkedHashMap<IBenchmarkTask, ITaskResult>();
  
  public void register(IBenchmarkTask task, ITaskResult result) {
    fResults.put(task, result);
  }
  
  public ITaskResult lookup(IBenchmarkTask task) {
    if (!fResults.containsKey(task)) throw new NoSuchElementException(task.getName());
    return fResults.get(task);
  }
  
  public Set<IBenchmarkTask> getAllTasks() {
    return Collections.unmodifiableSet(fResults.keySet());
  }

  @Override
  public Iterator<BenchmarkResultEntry> iterator() {
    List<BenchmarkResultEntry> entries = new ArrayList<BenchmarkResultEntry>(fResults.size());
    for(Entry<IBenchmarkTask, ITaskResult> mapEntry: fResults.entrySet()) {
      entries.add(new BenchmarkResultEntry(mapEntry.getKey(), mapEntry.getValue()));
    }
    return entries.iterator();
  }

  @Override
  public int size() {
    return fResults.size();
  }
  
  @Override
  public boolean add(BenchmarkResultEntry e) {
    fResults.put(e.getTask(), e.getResult());
    return true;
  }

  @Override
  public int hashCode() {
    return getClass().hashCode() + fResults.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    ResultMap other = (ResultMap)obj;
    if (fResults == null) {
      if (other.fResults != null) return false;
    } else if (!fResults.equals(other.fResults)) return false;
    return true;
  }
  
}
