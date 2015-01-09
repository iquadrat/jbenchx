package org.jbenchx.collections.jcf;

import java.util.Collection;
import java.util.TreeSet;

public class TreeSetBenchmark extends CollectionQueryBenchmark {
  
  public Collection<String> create() {
    return new TreeSet<String>();
  }

  @Override
  protected Collection<String> createEmptyCollection() {
    return new TreeSet<String>();
  }
  
}
