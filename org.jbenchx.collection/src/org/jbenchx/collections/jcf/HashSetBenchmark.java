package org.jbenchx.collections.jcf;

import java.util.Collection;
import java.util.HashSet;

import org.jbenchx.annotations.Bench;

public class HashSetBenchmark extends CollectionQueryBenchmark {

  @Bench
  public Collection<String> create() {
    return createEmptyCollection();
  }

  @Override
  protected Collection<String> createEmptyCollection() {
    return new HashSet<String>();
  }
  
}
