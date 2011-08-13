package org.jbenchx.test;

import java.security.*;

import org.jbenchx.*;
import org.jbenchx.annotations.*;


public class Example extends Benchmark {
  
  private SecureRandom fRandom;

  public Example() throws Exception {
    fRandom = SecureRandom.getInstance("SHA1PRNG");
  }
  
  @Bench
  public int random() {
    return fRandom.nextInt();
  }
  
}
