package org.jbenchx;

public class Timer {
  
  private boolean fRunning   = false;
  
  private long    fStartTime = 0;
  
  public Timer() {
  }
  
  public boolean isRunning() {
    return fRunning;
  }
  
  public void start() {
    if (fRunning) throw new IllegalStateException("Already running!");
    fStartTime = System.nanoTime();
    fRunning = true;
  }
  
  public long stopAndReset() {
    if (!fRunning) throw new IllegalStateException("Not running!");
    long endTime = System.nanoTime();
    fRunning = false;
    return endTime - fStartTime;
  }
  
}
