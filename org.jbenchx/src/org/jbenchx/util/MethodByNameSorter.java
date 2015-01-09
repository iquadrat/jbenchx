package org.jbenchx.util;

import java.lang.reflect.Method;
import java.util.Comparator;

public class MethodByNameSorter implements Comparator<Method> {
  
  public static final Comparator<? super Method> INSTANCE = new MethodByNameSorter();
  
  @Override
  public int compare(Method o1, Method o2) {
    return o1.getName().compareTo(o2.getName());
  }
  
}
