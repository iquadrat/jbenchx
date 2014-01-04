/*
 * Created on 09.10.2011
 *
 */
package org.jbenchx.run;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.annotation.CheckForNull;

import net.jcip.annotations.Immutable;

import org.jbenchx.util.ObjectUtil;

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
      double divisor = 1;
      for (int i = 0; i < length; ++i) {
        Parameterization parameterization = fParameterizations.get(i);
        Object value = parameterization.getValue(fPositions[i]);
        result.add(value);
        if (parameterization.getDivideBy()) {
          Number number = ObjectUtil.castOrNull(Number.class, value);
          if (number.doubleValue() != 0) {
            divisor *= number.doubleValue();
          }
        }
      }
      return new ParameterizationValues(result, divisor);
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