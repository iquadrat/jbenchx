package org.jbenchx.example;

import java.util.*;

import org.jbenchx.*;
import org.jbenchx.annotations.*;
import org.jbenchx.monitor.ConsoleProgressMonitor;

public class ArrayListBench {

  private final ArrayList<Integer> fIntegers;
  private final int fSize;
  
  public ArrayListBench(@ForEachInt({10, 100, 1000, 10000}) int size) {
    fSize= size;
    fIntegers = new ArrayList<Integer>();
    for (int i = 0; i < fSize; ++i) {
      fIntegers.add(i);
    }
  }

  @SuppressWarnings("unused")
  @Bench
  public Object create() {
    for (int i = 0; i < 1000; ++i) {
      new ArrayList<Object>(fSize);
    }
    return null;
  }

  @Bench
  public Object fill(@ForEachBoolean({true, false}) boolean initSize) {
    ArrayList<Object> list = initSize ? new ArrayList<Object>(fSize) : new ArrayList<Object>();
    for (int i = 0; i < fSize; ++i) {
      list.add(new Object());
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
      if (fIntegers.contains(-1)) {
        return true;
      }
    }
    return false;
  }
  

  public static void main(String[] args) throws Exception {

    BenchmarkRunner runner = new BenchmarkRunner();
    runner.add(ArrayListBench.class);
    runner.run(BenchmarkContext.create(new ConsoleProgressMonitor()));

  }

}
