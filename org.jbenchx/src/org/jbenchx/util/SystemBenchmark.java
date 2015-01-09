package org.jbenchx.util;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.jbenchx.annotations.Bench;

public class SystemBenchmark {
  
  private static final int MEM_BENCH_SIZE = 8 * 1024 * 1024;
  
  private IntBuffer        fBuffer;
  
  public SystemBenchmark() {
    fBuffer = ByteBuffer.allocateDirect(MEM_BENCH_SIZE).asIntBuffer();
  }
  
  @Bench
  public Void empty() {
    return null;
  }
  
  /**
   * Do some floating point number crunching.
   */
  @Bench(divisor = 1000)
  public double calculate() {
    double result = 0;
    for (int i = 0; i < 1000; i++) {
      result += 0.5 / i;
    }
    return result;
  }
  
  /**
   * Memory throughput benchmark. If the memory throughput changes between
   * the same runs on a machine this is a strong indicator that other applications
   * are running concurrently.
   */
  @Bench(divisor = MEM_BENCH_SIZE)
  public Object memory() {
    fBuffer.clear();
    int size = fBuffer.capacity();
    for (int i = 0; i < size; ++i) {
      fBuffer.put(i);
    }
    
    // partition in two halfs
    fBuffer.position(size / 2);
    IntBuffer buf1 = fBuffer.slice();
    
    fBuffer.position(0);
    fBuffer.limit(size / 2);
    IntBuffer buf2 = fBuffer.slice();
    
    for (int i = 0; i < 10; ++i) {
      buf1.clear();
      buf2.rewind();
      buf1.put(buf2);
      
      buf2.clear();
      buf1.rewind();
      buf2.put(buf1);
    }
    
    return fBuffer;
  }
  
}
