/*
 * Created on 09.10.2011
 *
 */
package org.jbenchx.run;

import java.util.*;

import javax.annotation.*;

import net.jcip.annotations.Immutable;

@Immutable
public class ParameterizationIterable implements Iterable<ParameterizationValues> {
  
  private class ParameterizationIterator implements Iterator<ParameterizationValues> {
    
    @CheckForNull
    private int[] fPositions;
    
    public ParameterizationIterator() {
      fPositions = new int[fParameterizations.size()];
      if (fPositions.length > 0) {
        fPositions[fPositions.length - 1] = -1;
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
      int length = fPositions.length;
      List<Object> result = new ArrayList<Object>(length);
      for (int i = 0; i < length; ++i) {
        result.add(fParameterizations.get(i).getValue(fPositions[i]));
      }
      return new ParameterizationValues(result);
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
  
  private final List<Parameterization> fParameterizations;
  
//  private final List<Class<?>>         fTypes;
  
  public ParameterizationIterable(List<Parameterization> parameterizations) {
    fParameterizations = new ArrayList<Parameterization>(parameterizations);
//    fTypes = new ArrayList<Class<?>>(parameterizations.size());
//    for (int i = 0; i < parameterizations.size(); ++i) {
//      fTypes.add(parameterizations.get(i).getType());
//    }
  }
  
  @Override
  public Iterator<ParameterizationValues> iterator() {
    return new ParameterizationIterator();
  }
  
}