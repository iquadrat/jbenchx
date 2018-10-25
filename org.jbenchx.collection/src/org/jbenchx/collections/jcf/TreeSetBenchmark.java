package org.jbenchx.collections.jcf;

import java.util.Collection;
import java.util.TreeSet;

import org.jbenchx.annotations.ForEachInt;

public class TreeSetBenchmark extends CollectionQueryBenchmark {
  
  public TreeSetBenchmark(@ForEachInt({10, 1000, 100000}) int size) {
    super(size);
  }

  public Collection<String> create() {
    return new TreeSet<String>();
  }

  @Override
  protected Collection<String> createEmptyCollection() {
    return new TreeSet<String>();
  }
  
}
