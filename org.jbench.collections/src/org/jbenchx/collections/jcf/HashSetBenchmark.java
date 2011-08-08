package org.jbenchx.collections.jcf;

import java.util.*;

public class HashSetBenchmark extends CollectionQueryBenchmark {

  public Collection<String> create() {
    return new HashSet<String>();
  }

  @Override
  protected Collection<String> createEmptyCollection() {
    return new HashSet<String>();
  }
  
}
