package org.jbenchx;

import java.util.*;

import org.jbenchx.error.*;

public class BenchmarkResult {
  
  private List<BenchmarkError> fErrors = new ArrayList<BenchmarkError>();
  
  public void addError(BenchmarkError error) {
    fErrors.add(error);
    System.out.println(error);
  }
  
}
