/*
 * Created on 09.10.2011
 *
 */
package org.jbenchx.run;

import java.lang.reflect.Field;
import java.util.*;

import net.jcip.annotations.*;

@Immutable
public class ParameterizationValues {
  
  public static final ParameterizationValues EMPTY = new ParameterizationValues(Collections.<Object>emptyList());
  
  private final List<Object>                 fValues;
  
  public ParameterizationValues(List<Object> values) {
    fValues = new ArrayList<Object>(values);
  }
  
  public boolean hasArguments() {
    return fValues.size() != 0;
  }
  
  public List<Object> getValues() {
    return fValues;
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
    return true;
  }
  
  @Override
  public String toString() {
    return getClass().getSimpleName() + fValues;
  }
  
}
