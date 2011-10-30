package org.jbenchx.example;

import org.jbenchx.annotations.*;


public class Example3 {
 
  @Bench
  public Object createObjectArray(@ForEachInt({10,100,1000}) int size) {
    return new Object[size];
  }
  
}
