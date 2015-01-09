package org.jbenchx.collections.jcf;

import java.util.Collection;
import java.util.LinkedList;

import org.jbenchx.annotations.Bench;

public class LinkedListBenchmark extends CollectionQueryBenchmark {

  @Bench
  public Collection<String> create() {
    return createEmptyCollection();
  }

  @Override
  protected Collection<String> createEmptyCollection() {
    return new LinkedList<String>();
  }
  
}
