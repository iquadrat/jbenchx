package org.jbenchx.collections.jcf;

import java.util.*;

import org.jbenchx.annotations.*;

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
