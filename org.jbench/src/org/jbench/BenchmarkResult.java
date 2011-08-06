package org.jbench;

import java.util.*;

import org.jbench.error.*;

public class BenchmarkResult {
  
  private List<BenchmarkError> fErrors;
  
  public void addError(BenchmarkError error) {
    fErrors.add(error);
  }
  
}
