/*
 * Created on 09.10.2011
 *
 */
package org.jbenchx.run;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.jcip.annotations.Immutable;

@Immutable
public class ParameterizationValues {
  
  public static final ParameterizationValues EMPTY = new ParameterizationValues(Collections.<Object>emptyList(), 1);
  
  private final List<Object>                 fValues;
  
  private final double                       fDivisor;
  
  public ParameterizationValues(List<Object> values, double divisor) {
    fValues = new ArrayList<Object>(values);
    fDivisor = divisor;
  }
  
  public boolean hasArguments() {
    return fValues.size() != 0;
  }
  
  public List<Object> getValues() {
    return fValues;
  }
  
  public double getfDivisor() {
    return fDivisor;
  }
  
  private Object readResolve() throws Exception {
    if (fValues == null) {
      Field field = ParameterizationValues.class.getDeclaredField("fValues");
      field.setAccessible(true);
      field.set(this, Collections.emptyList());
    }
    return this;
  }
  
  @Override
  public int hashCode() {
    return getClass().hashCode() + fValues.hashCode() + 31;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    ParameterizationValues other = (ParameterizationValues)obj;
    if (!fValues.equals(other.fValues)) {
      return false;
    }
    if (fDivisor != other.fDivisor) {
      return false;
    }
    return true;
  }
  
  @Override
  public String toString() {
    return getClass().getSimpleName() + fValues;
  }
  
}
