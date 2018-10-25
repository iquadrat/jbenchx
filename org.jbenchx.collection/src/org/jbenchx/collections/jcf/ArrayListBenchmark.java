package org.jbenchx.collections.jcf;

import java.util.ArrayList;
import java.util.Collection;

import org.jbenchx.annotations.Bench;
import org.jbenchx.annotations.ForEachInt;

public class ArrayListBenchmark extends CollectionQueryBenchmark {
  
  public ArrayListBenchmark(@ForEachInt({10, 1000}) int size) {
    super(size);
  }

  @Bench
  public Collection<String> create() {
    return createEmptyCollection();
  }
  
  @Override
  protected Collection<String> createEmptyCollection() {
    return new ArrayList<String>();
  }
  
}
