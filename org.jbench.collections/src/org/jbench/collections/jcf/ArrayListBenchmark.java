package org.jbench.collections.jcf;

import java.util.*;

public class ArrayListBenchmark extends CollectionQueryBenchmark {
  
  public Collection<String> create() {
    return new ArrayList<String>();
  }
  
  @Override
  protected Collection<String> createEmptyCollection() {
    return new ArrayList<String>();
  }
  
}
