package org.jbench;

import java.util.*;

import org.jbench.error.*;

public class BenchmarkResult {
  
  private List<BenchmarkError> fErrors = new ArrayList<BenchmarkError>();
  
  public void addError(BenchmarkError error) {
    fErrors.add(error);
    System.out.println(error);
  }
  
}
