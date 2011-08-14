package org.jbenchx.test;

import java.security.*;

import org.jbenchx.*;
import org.jbenchx.annotations.*;
import org.jbenchx.monitor.*;

import edu.umd.cs.findbugs.annotations.*;

public class Example extends Benchmark {
  
  private SecureRandom fRandom;
  
  private volatile double       fNumber1;
  private double       fNumber2;
  private double       fNumber3;
  private int          fN = 1000;
  
  public Example() throws Exception {
    fRandom = SecureRandom.getInstance("SHA1PRNG");
    fNumber1 = 2;
    fNumber2 = 2;
    fNumber3 = 2;
  }
  
//  @Bench(targetTimeNs = 100000000)
//  public void moep() {
//    double x = 1;
//    while (x < 12200) {
//      x += x * 0.0000755;
//      fN++;
//    }
//  }
  
//  @Bench
//  public int random() {
//    return fRandom.nextInt();
//  }
//  
//  @Bench
//  public Object one() {
//    return null;
//  }
//  
//  @Bench
//  public Object two() {
//    return null;
//  }
  
  @Bench
  public Object avoid() {
    return null;
  }
  
  @Bench
  public double asqrt1() {
//    return Math.sqrt(fNumber3) == 0 ? Boolean.TRUE : null;
    return Math.sqrt(fNumber1);
  } 
  
  @Bench(divisor = 2)
  public double sqrt2() {
    return Math.sqrt(fNumber1) + Math.sqrt(fNumber2);
  }
  
  @Bench(divisor = 3)
  public double sqrt3() {
    return Math.sqrt(fNumber1) + Math.sqrt(fNumber2)+ Math.sqrt(fNumber3);
  }
  
  @Bench(divisor = 2)
  public double sqrt2b() {
    return Math.sqrt(Math.sqrt(fNumber1)); 
  }
  
  @Bench(divisor = 3)
  public void sqrt3b() {
    fNumber3 = Math.sqrt(Math.sqrt(Math.sqrt(fNumber1))); 
  }
  
  @Bench(divisor = 1000)
  public double sqrt() {
    double result = 0;
//    double f = 1;
    for (int i = 0; i < 1000; i++) {
      result += Math.sqrt(fNumber1);
//      f+= 0.00001;
    }
    return result;
  }
  
  @Bench(divisor = 1000)
  public double sqrtR() {
    double result = fNumber1;
    for (int i = 0; i < 1000; i++) {
      result = Math.sqrt(result) + 1;
    }
//    System.out.println(result);
    return result;
  }
  
//  @Bench
//  public double afoo() {
//    return 2.8;
//  }
  
  public static void main(String[] args) throws Exception {
    
    BenchmarkRunner runner = new BenchmarkRunner();
    runner.add(Example.class);
    
    runner.run(BenchmarkContext.create(new ConsoleProgressMonitor()));
    
  }
  
}
