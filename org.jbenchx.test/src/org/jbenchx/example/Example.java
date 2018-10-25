package org.jbenchx.example;

import org.jbenchx.BenchmarkContext;
import org.jbenchx.BenchmarkRunner;
import org.jbenchx.annotations.Bench;
import org.jbenchx.annotations.DivideBy;
import org.jbenchx.annotations.ForEachInt;
import org.jbenchx.monitor.ConsoleProgressMonitor;

public class Example {

  private volatile double fNumber1;

  private double          fNumber2;

  private double          fNumber3;

//  private final int       fN = 1000;

  public Example(@SuppressWarnings("unused") Void v) {}

  public Example() throws Exception {
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

  @Bench
  public double sqrt2(@DivideBy @ForEachInt(2) int calls) {
    return Math.sqrt(fNumber1) + Math.sqrt(fNumber2);
  }

  @Bench
  public double sqrt3(@DivideBy @ForEachInt(3) int calls) {
    return Math.sqrt(fNumber1) + Math.sqrt(fNumber2) + Math.sqrt(fNumber3);
  }

  @Bench
  public double sqrt2b(@DivideBy @ForEachInt(2) int calls) {
    return Math.sqrt(Math.sqrt(fNumber1));
  }

  @Bench
  public void sqrt3b(@DivideBy @ForEachInt(3) int calls) {
    fNumber3 = Math.sqrt(Math.sqrt(Math.sqrt(fNumber1)));
  }

  @Bench
  public double sqrt(@DivideBy @ForEachInt({1,10,100,1000}) int iterations) {
    double result = 0;
//    double f = 1;
    for (int i = 0; i < iterations; i++) {
      result += Math.sqrt(fNumber1);
//      f+= 0.00001;
    }
    return result;
  }

  @Bench
  public double sqrtR(@DivideBy @ForEachInt({1,10,100,1000}) int iterations) {
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
