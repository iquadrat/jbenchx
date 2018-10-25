package org.jbenchx.collections.jcf;

import java.util.Collection;
import java.util.LinkedHashSet;

import org.jbenchx.annotations.Bench;
import org.jbenchx.annotations.ForEachInt;

public class LinkedHashSetBenchmark extends CollectionQueryBenchmark {
  
  public LinkedHashSetBenchmark(@ForEachInt({10, 1000, 100000}) int size) {
    super(size);
  }

  @Bench
  public Collection<String> create() {
    return createEmptyCollection();
  }
  
  @Override
  protected Collection<String> createEmptyCollection() {
    return new LinkedHashSet<String>();
  }
  
}
