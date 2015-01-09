package org.jbenchx.collections.jcf;

import java.util.ArrayDeque;
import java.util.Collection;

import org.jbenchx.annotations.Bench;

public class ArrayDequeBenchmark extends CollectionQueryBenchmark {

  @Bench
  public Collection<String> create() {
    return createEmptyCollection();
  }

  @Override
  protected Collection<String> createEmptyCollection() {
    return new ArrayDeque<String>();
  }
  
}
