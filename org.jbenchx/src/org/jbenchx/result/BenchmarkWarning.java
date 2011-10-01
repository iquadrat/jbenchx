/*
 * Created on 01.10.2011
 *
 */
package org.jbenchx.result;

public class BenchmarkWarning {

  private final String fReason;

  public BenchmarkWarning(String reason) {
    fReason = reason;
  }

  public String getReason() {
    return fReason;
  }

  @Override
  public String toString() {
    return getClass().getName() + "[" + fReason + "]";
  }

}
