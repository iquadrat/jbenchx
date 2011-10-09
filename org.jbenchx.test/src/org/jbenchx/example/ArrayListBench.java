package org.jbenchx.example;

import java.util.*;

import org.jbenchx.annotations.*;

public class ArrayListBench {

  private final ArrayList<Integer> fInteger1000;

  public ArrayListBench() {
    fInteger1000 = new ArrayList<Integer>();
    for (int i = 0; i < 1000; ++i) {
      fInteger1000.add(i);
    }
  }

  @Bench
  public Object create(@ForEachInt({10, 100, 1000, 10000}) int size) {
    for (int i = 0; i < 1000; ++i) {
      new ArrayList<Object>(size);
    }
    return null;
  }

  @Bench
  public Object fill(@ForEachInt({10, 100, 1000, 10000}) int size, @ForEachBoolean({true, false}) boolean initSize) {
    ArrayList<Object> list = initSize ? new ArrayList<Object>(size) : new ArrayList<Object>();
    for (int i = 0; i < size; ++i) {
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
      if (fInteger1000.contains(-1)) {
        return true;
      }
    }
    return false;
  }

}
