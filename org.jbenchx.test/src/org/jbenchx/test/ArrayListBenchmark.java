package org.jbenchx.test;

import java.util.*;

import org.jbenchx.*;
import org.jbenchx.annotations.*;

public class ArrayListBenchmark extends Benchmark {
  
  private ArrayList<Integer> fInteger1000;
  
  public ArrayListBenchmark() {
    fInteger1000 = new ArrayList<Integer>();
    for (int i = 0; i < 1000; ++i) {
      fInteger1000.add(i);
    }
  }
  
  @Bench
  public Object create() {
    for(int i=0; i<1000; ++i) {
      new ArrayList<Object>(1000);
    }
    return null;
  }
  
//  @Bench
//  public Object createAdd1000() {
//    ArrayList<Integer> list = new ArrayList<Integer>(100);
//////    for (int i = 0; i < 1; ++i) {
//////      list.add(i);
//////    }
//    return list;
//  }
  
  @Bench
  public boolean contains() {
//    return fInteger1000.contains(-1);
    
    for (int i = 1; i <= 10; ++i) {
      if (fInteger1000.contains(-1)) {
        return true;
      }
    }
    return false;
  }
  
}
