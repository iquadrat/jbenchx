package org.jbenchx;

public class Timer {
  
  private boolean fRunning   = false;
  
  private long    fStartTime = 0;
  
  public Timer() {}
  
  public boolean isRunning() {
    return fRunning;
  }
  
  public void start() {
    if (fRunning) throw new IllegalStateException("Already running!");
    fStartTime = time();
    fRunning = true;
  }

  private long time() {
    return System.nanoTime();
//    return System.currentTimeMillis() * 1000*1000;
  }
  
  public long getTime() {
    return time() - fStartTime;
  }
  
  public long stopAndReset() {
    if (!fRunning) throw new IllegalStateException("Not running!");
    long endTime = time();
    fRunning = false;
    return endTime - fStartTime;
  }
  
}
