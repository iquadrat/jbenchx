package org.jbenchx.collections.jcf;

import java.util.ArrayList;
import java.util.Collection;

import org.jbenchx.annotations.Bench;

public class ArrayListBenchmark extends CollectionQueryBenchmark {
  
  @Bench
  public Collection<String> create() {
    return createEmptyCollection();
  }
  
  @Override
  protected Collection<String> createEmptyCollection() {
    return new ArrayList<String>();
  }
  
}
