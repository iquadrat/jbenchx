package org.jbenchx.util;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.jbenchx.annotations.Bench;
import org.jbenchx.annotations.DivideBy;
import org.jbenchx.annotations.ForEachInt;

public class SystemBenchmark {
  
  private static final int MEM_BENCH_SIZE = 8 * 1024 * 1024;
  
  private final IntBuffer  buffer;
  
  public SystemBenchmark() {
    buffer = ByteBuffer.allocateDirect(MEM_BENCH_SIZE).asIntBuffer();
  }
  
  @Bench
  public Void empty() {
    return null;
  }
  
  /**
   * Do some floating point number crunching.
   */
  @Bench
  public double calculate(@DivideBy @ForEachInt({1000}) int iterations) {
    double result = 0;
    for (int i = 0; i < iterations; i++) {
      result += 0.5 / i;
    }
    return result;
  }
  
  /**
   * Memory throughput benchmark. If the memory throughput changes between
   * the same runs on a machine this is a strong indicator that other applications
   * are running concurrently.
   */
  @Bench
  public Object memory(@DivideBy @ForEachInt({MEM_BENCH_SIZE}) int memSize) {
    buffer.clear();
    int size = buffer.capacity();
    for (int i = 0; i < size; ++i) {
      buffer.put(i);
    }
    
    // partition in two halfs
    buffer.position(size / 2);
    IntBuffer buf1 = buffer.slice();
    
    buffer.position(0);
    buffer.limit(size / 2);
    IntBuffer buf2 = buffer.slice();
    
    for (int i = 0; i < 10; ++i) {
      buf1.clear();
      buf2.rewind();
      buf1.put(buf2);
      
      buf2.clear();
      buf1.rewind();
      buf2.put(buf1);
    }
    
    return buffer;
  }
  
}
