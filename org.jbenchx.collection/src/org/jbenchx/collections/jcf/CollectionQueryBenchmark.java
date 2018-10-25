package org.jbenchx.collections.jcf;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.jbenchx.annotations.Bench;
import org.jbenchx.annotations.Tags;

/**
 * Benchmarks all queries (non-modifying) operation defined on the {@link Collection} interface.
 * @author micha
 */
@Tags({"collection", "query"})
public abstract class CollectionQueryBenchmark {
  
  private final Collection<String>   collection;
  
  private final String[]             fContained;
  
  private final String[]             fNotContained;
  
  CollectionQueryBenchmark(int size) {
    collection = createEmptyCollection();
    fContained = new String[size];
    fNotContained = new String[size];
    
    // fill the collection
    for (int i = 0; i < size; ++i) {
      int v = 2 * i + 1;
      String string = String.valueOf(v);
      collection.add(string);
      fContained[i] = string;
      fNotContained[i] = String.valueOf(v + 1);
    }
  }
  
  protected abstract Collection<String> createEmptyCollection();
  
  @Bench
  public int size() {
    return collection.size();
  }
  
  @Bench
  public boolean isEmpty() {
    return collection.isEmpty();
  }
  
  @Bench
  public boolean containsYes() {
    for (String s: fContained) {
      if (!collection.contains(s)) {
        return true;
      }
    }
    return false;
  }
  
  @Bench
  public boolean containsNo() {
    for (String s: fNotContained) {
      if (collection.contains(s)) {
        return true;
      }
    }
    return false;
  }
  
  @Bench
  public int iterate() {
    int result = 0;
    Iterator<String> iterator = collection.iterator();
    while (iterator.hasNext()) {
      result += iterator.next().length();
    }
    return result;
  }
  
  @Bench
  public Object toArray() {
    return collection.toArray();
  }
  
  @Bench
  public boolean containsAll() {
    return collection.containsAll(Arrays.asList(fContained));
  }
  
  @Bench
  public int benchHashCode() {
    return collection.hashCode();
  }
  
}
