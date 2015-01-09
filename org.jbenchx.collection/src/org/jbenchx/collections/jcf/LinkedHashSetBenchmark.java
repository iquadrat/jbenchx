package org.jbenchx.collections.jcf;

import java.util.Collection;
import java.util.LinkedHashSet;

import org.jbenchx.annotations.Bench;

public class LinkedHashSetBenchmark extends CollectionQueryBenchmark {
  
  @Bench
  public Collection<String> create() {
    return createEmptyCollection();
  }
  
  @Override
  protected Collection<String> createEmptyCollection() {
    return new LinkedHashSet<String>();
  }
  
}
