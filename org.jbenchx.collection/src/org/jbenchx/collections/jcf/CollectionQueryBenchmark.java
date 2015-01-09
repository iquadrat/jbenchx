package org.jbenchx.collections.jcf;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.jbenchx.annotations.Bench;
import org.jbenchx.annotations.Group;

/**
 * Benchmarks all queries (non-modifying) operation defined on the {@link Collection} interface.
 * @author micha
 */
@Group("Collection")
public abstract class CollectionQueryBenchmark {

  private static final int     COUNT = 100;
  private final int            fSize = 1000;
  private final Collection<String>   fCollection;
  private final String[]             fContained;
  private final String[]             fNotContained;
  private final Collection<String>[] fCollectionArray;

  @SuppressWarnings("unchecked")
  CollectionQueryBenchmark() {
    fCollection = createEmptyCollection();
    fContained = new String[fSize];
    fNotContained = new String[fSize];

    // fill the collection
    for (int i = 0; i < fSize; ++i) {
      int v = 2 * i + 1;
      String string = String.valueOf(v);
      fCollection.add(string);
      fContained[i] = string;
      fNotContained[i] = String.valueOf(v + 1);
    }

    // create some collections for testing simple properties
    fCollectionArray = new Collection[COUNT];
    for (int i = 0; i < COUNT; ++i) {
      fCollectionArray[i] = createEmptyCollection();
      for (int j = 0; j < fSize; ++j) {
        fCollectionArray[i].add(String.valueOf(j));
      }
    }
  }

  protected abstract Collection<String> createEmptyCollection();

  @Bench(divisor = COUNT)
  public int size() {
    int result = 0;
    for (int i = 0; i < COUNT; ++i) {
      result += fCollectionArray[i].size();
    }
    return result;
  }

  @Bench(divisor = COUNT)
  public int isEmpty() {
    int result = 0;
    for (int i = 0; i < COUNT; ++i) {
      result += fCollectionArray[i].isEmpty() ? 0 : 1;
    }
    return result;
  }

  @Bench(divisor = 1000)
  public boolean containsYes() {
    for (String s: fContained) {
      if (!fCollection.contains(s)) {
        return true;
      }
    }
    return false;
  }

  @Bench(divisor = 1000)
  public boolean containsNo() {
    for (String s: fNotContained) {
      if (fCollection.contains(s)) {
        return true;
      }
    }
    return false;
  }

  @Bench(divisor = 1000)
  public int iterate() {
    int result = 0;
    Iterator<String> iterator = fCollection.iterator();
    while (iterator.hasNext()) {
      result += iterator.next().length();
    }
    return result;
  }

  @Bench(divisor = 1000)
  public Object toArray() {
    return fCollection.toArray();
  }

  @Bench(divisor = 1000)
  public boolean containsAll() {
    return fCollection.containsAll(Arrays.asList(fContained));
  }

  @Bench
  public int benchHashCode() {
    return fCollection.hashCode();
  }

}
