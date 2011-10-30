package org.jbenchx.collections.jcf;

import java.util.*;

import org.jbenchx.annotations.*;
import org.jbenchx.util.Assert;

public class JavaSetBench {
  
  private final int         fSize;
  
  private final Object[]    fObjectsContained;
  private final Object[]    fObjectsMissing;
  
  private final Set<Object> fFullSet;
  
  public JavaSetBench(@ForEachInt({1, 10, 100, 1000, 10000, 100000}) int size) {
    fSize = size;
    fObjectsContained = new Object[fSize];
    fObjectsMissing = new Object[fSize];
    for (int i = 0; i < fSize; ++i) {
      fObjectsContained[i] = String.valueOf(2 * i);
      fObjectsMissing[i] = String.valueOf(2 * i + 1);
    }
    fFullSet = new HashSet<Object>(Arrays.asList(fObjectsContained));
  }
  
  @Bench
  public Object create() {
    return new HashSet<Object>(Arrays.asList(fObjectsContained));
  }
  
  @Bench
  public Object containsTrue() {
    int count = 0;
    for(Object string: fObjectsContained) {
      if (fFullSet.contains(string)) {
        count++;
      }
    }
    return count;
  }
  
  @Bench
  public Object containsFalse() {
    int count = 0;
    for(Object string: fObjectsMissing) {
      if (fFullSet.contains(string)) {
        count++;
      }
    }
    return count;
  }
  
  @Bench
  public Object removeAdd() {
    for (int i = 0; i < fSize; ++i) {
      fFullSet.remove(fObjectsContained[i]);
      fFullSet.add(fObjectsContained[i]);
    }
    return fFullSet;
  }
  
  @Bench
  public Object removeAdd2() {
    for (int i = 0; i < fSize; ++i) {
      fFullSet.remove(fObjectsContained[i]);
      fFullSet.add(fObjectsMissing[i]);
    }
    for (int i = 0; i < fSize; ++i) {
      fFullSet.remove(fObjectsMissing[i]);
      fFullSet.add(fObjectsContained[i]);
    }
    Assert.equals(fSize, fFullSet.size());
    return fFullSet;
  }
  
  @Bench
  public Object iterate() {
    int i = 0;
    for (@SuppressWarnings("unused")
    Object s: fFullSet) {
      i++;
    }
    return i;
  }
  
}
