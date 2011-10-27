/*
 * Created on 09.10.2011
 *
 */
package org.jbenchx.run;

import java.util.*;

public class ParameterizationValues {

  public static final ParameterizationValues EMPTY = new ParameterizationValues(new Class<?>[0], new Object[0]);

  private final Class<?>[]                   fTypes;

  private final Object[]                     fValues;

  public ParameterizationValues(Class<?>[] types, Object[] values) {
    fTypes = types;
    fValues = values;
  }
  
  public boolean hasArguments() {
    return fTypes.length != 0;
  }

  public Object[] getValues() {
    return fValues;
  }

  public Class<?>[] getTypes() {
    return fTypes;
  }

  @Override
  public int hashCode() {
    return getClass().hashCode() + 31 * Arrays.hashCode(fValues);
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
    if (!Arrays.equals(fValues, other.fValues)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + Arrays.toString(fValues);
  }

}
