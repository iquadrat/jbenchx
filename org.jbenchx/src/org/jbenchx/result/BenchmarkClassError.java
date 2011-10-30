package org.jbenchx.result;

import java.io.*;

public class BenchmarkClassError extends BenchmarkFailure {

  private static final long serialVersionUID = 1L;

  private final Class<?>    fClass;

  private final String      fMessage;

  public BenchmarkClassError(Class<?> clazz, String message) {
    fClass = clazz;
    fMessage = message;
  }

  public Class<?> getAffectedClass() {
    return fClass;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + ": " + fClass.getName() + ": " + fMessage;
  }

  @Override
  public void print(PrintWriter out) {
    out.print(toString());
    out.flush();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fClass == null) ? 0 : fClass.hashCode());
    result = prime * result + ((fMessage == null) ? 0 : fMessage.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    BenchmarkClassError other = (BenchmarkClassError)obj;
    if (fClass == null) {
      if (other.fClass != null) return false;
    } else if (!fClass.equals(other.fClass)) return false;
    if (fMessage == null) {
      if (other.fMessage != null) return false;
    } else if (!fMessage.equals(other.fMessage)) return false;
    return true;
  }

}
