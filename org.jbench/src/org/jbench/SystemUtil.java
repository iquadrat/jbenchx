package org.jbench;


public class SystemUtil {

  public static void cleanMemory() {
    for(int i=0; i<5; ++i) {
      System.gc();
      System.runFinalization();
    }
  }
  
}
