package org.jbenchx.collections.jcf;

import java.util.Arrays;
import java.util.Random;
import java.util.TreeSet;

import org.jbenchx.annotations.Bench;
import org.jbenchx.annotations.ForEachInt;
import org.jbenchx.annotations.ForEachString;
import org.jbenchx.util.Assert;

public class JavaSetBench {
  
  private final int                   fSize;
  private final int                   fType;
  
  private final Object[]              fStringsContained;
  private final Object[]              fStringsMissing;
  
  private final TreeSet<Object> fFullSet;
  
  public JavaSetBench(@ForEachInt({/*1, 10, 100, 1000, 10000, */100000, 1000000}) int size,
      @ForEachString({/*"Object",*/ "String", "Integer"}) String type) {
    fSize = size;
    fType = getType(type);
    
    fStringsContained = new Object[fSize];
    fStringsMissing = new Object[fSize];
    
    // simulate random allocation order of elements
    int[] permutation = new int[2 * fSize];
    for (int i = 0; i < 2 * fSize; ++i) {
      permutation[i] = i;
    }
    
    Random random = new Random(0);
    shuffle(permutation, random);
    for (int i = 0; i < fSize; ++i) {
      fStringsContained[i] = createObject(permutation[2 * i]);
      fStringsMissing[i] = createObject(permutation[2 * i + 1]);
    }
    
    fFullSet = new TreeSet<Object>(Arrays.asList(fStringsContained));
  }
  
  private void shuffle(int[] permutation, Random random) {
    int size = permutation.length;
    for (int i = size; i > 1; i--) {
      swap(permutation, i - 1, random.nextInt(i));
    }
  }
  
  private void swap(int[] permutation, int i, int j) {
    int tmp = permutation[i];
    permutation[i] = permutation[j];
    permutation[j] = tmp;
  }

  private int getType(String type) {
    if ("Object".equals(type)) {
      return 0;
    }
    if ("String".equals(type)) {
      return 1;
    }
    if ("Integer".equals(type)) {
      return 2;
    }
    throw new IllegalArgumentException(type);
  }
  
  private Object createObject(int idx) {
    switch (fType) {
      case 0:
        return new Object();
      case 1:
        return String.valueOf("Foo" + idx);
      case 2:
        return Integer.valueOf(idx);
    }
    throw new IllegalArgumentException("Invalid index: " + idx);
  }
  
  @Bench
  public Object create() {
    return new TreeSet<Object>(Arrays.asList(fStringsContained));
  }
  
  @Bench
  public Object containsTrue() {
    int count = 0;
    for (Object string: fStringsContained) {
      if (fFullSet.contains(string)) {
        count++;
      }
    }
    return count;
  }
  
  @Bench
  public Object containsFalse() {
    int count = 0;
    for (Object string: fStringsMissing) {
      if (fFullSet.contains(string)) {
        count++;
      }
    }
    return count;
  }
  
  @Bench
  public Object removeAdd() {
    for (int i = 0; i < fSize; ++i) {
      fFullSet.remove(fStringsContained[i]);
      fFullSet.add(fStringsContained[i]);
    }
    return fFullSet;
  }
  
  @Bench
  public Object removeAdd2() {
    for (int i = 0; i < fSize; ++i) {
      fFullSet.remove(fStringsContained[i]);
      fFullSet.add(fStringsMissing[i]);
    }
    for (int i = 0; i < fSize; ++i) {
      fFullSet.remove(fStringsMissing[i]);
      fFullSet.add(fStringsContained[i]);
    }
    Assert.equals(fSize, fFullSet.size());
    return fFullSet;
  }
  
  @Bench
  public Object iterate() {
    int i = 0;
    for (@SuppressWarnings("unused")
    Object s: fFullSet) {
      i++;
    }
    Assert.equals(fSize, i);
    return i;
  }
  
}
