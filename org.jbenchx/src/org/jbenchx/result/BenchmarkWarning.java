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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fReason == null) ? 0 : fReason.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    BenchmarkWarning other = (BenchmarkWarning)obj;
    if (fReason == null) {
      if (other.fReason != null) return false;
    } else if (!fReason.equals(other.fReason)) return false;
    return true;
  }
  
}
