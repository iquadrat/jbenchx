package org.jbenchx.collections.jcf;

import java.util.Collection;
import java.util.LinkedList;

import org.jbenchx.annotations.Bench;
import org.jbenchx.annotations.ForEachInt;

public class LinkedListBenchmark extends CollectionQueryBenchmark {

  public LinkedListBenchmark(@ForEachInt({10, 1000}) int size) {
    super(size);
  }

  @Bench
  public Collection<String> create() {
    return createEmptyCollection();
  }

  @Override
  protected Collection<String> createEmptyCollection() {
    return new LinkedList<String>();
  }
  
}
