/*
 * Created on 09.10.2011
 *
 */
package org.jbenchx.run;

import java.util.*;

import javax.annotation.*;

public class ParameterizationIterator implements Iterator<ParameterizationValues> {

  private final List<Parameterization> fParameterizations;

  private final Class<?>[]             fTypes;

  @CheckForNull
  private int[]                        fPositions;

  public ParameterizationIterator(List<Parameterization> parameterizations) {
    fParameterizations = new ArrayList<Parameterization>(parameterizations);
    fPositions = new int[parameterizations.size()];
    if (fPositions.length > 0) {
      fPositions[fPositions.length - 1] = -1;
    }
    fTypes = new Class<?>[parameterizations.size()];
    for (int i = 0; i < parameterizations.size(); ++i) {
      fTypes[i] = parameterizations.get(i).getType();
    }
  }

  @Override
  public boolean hasNext() {
    if (fPositions == null) {
      return false;
    }
    for (int i = 0; i < fPositions.length; ++i) {
      if (fPositions[i] != fParameterizations.get(i).size() - 1) {
        return true;
      }
    }
    return fParameterizations.isEmpty();
  }

  @Override
  public ParameterizationValues next() {
    if (!findNext()) {
      throw new NoSuchElementException();
    }
    if (fPositions == null) {
      return ParameterizationValues.EMPTY;
    }
    Object[] result = new Object[fPositions.length];
    for (int i = 0; i < result.length; ++i) {
      result[i] = fParameterizations.get(i).getValue(fPositions[i]);
    }
    return new ParameterizationValues(fTypes, result);
  }

  private boolean findNext() {
    if (fPositions == null) {
      return false;
    }
    for (int i = fPositions.length - 1; i >= 0; i--) {
      fPositions[i]++;
      if (fPositions[i] == fParameterizations.get(i).size()) {
        fPositions[i] = 0;
      } else {
        return true;
      }
    }
    // end of iteration reached
    fPositions = null;
    return fParameterizations.isEmpty();
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

}
