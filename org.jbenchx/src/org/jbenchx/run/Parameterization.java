/*
 * Created on 09.10.2011
 *
 */
package org.jbenchx.run;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.Arrays;

import net.jcip.annotations.Immutable;

import org.jbenchx.annotations.DivideBy;
import org.jbenchx.annotations.ForEachBoolean;
import org.jbenchx.annotations.ForEachDouble;
import org.jbenchx.annotations.ForEachFloat;
import org.jbenchx.annotations.ForEachInt;
import org.jbenchx.annotations.ForEachLong;
import org.jbenchx.annotations.ForEachString;
import org.jbenchx.result.BenchmarkClassError;
import org.jbenchx.util.ObjectUtil;

@Immutable
public class Parameterization {
  
  private final Class<?> fType;
  
  private final Object[] fValues;
  
  private final boolean  fDivideBy;
  
  public Parameterization(int[] values, boolean divideBy) {
    fType = int.class;
    fValues = new Object[values.length];
    for (int i = 0; i < values.length; ++i) {
      fValues[i] = values[i];
    }
    fDivideBy = divideBy;
  }
  
  public Parameterization(String[] values) {
    fType = String.class;
    fValues = new Object[values.length];
    for (int i = 0; i < values.length; ++i) {
      fValues[i] = values[i];
    }
    fDivideBy = false;
  }
  
  public Parameterization(float[] values, boolean divideBy) {
    fType = float.class;
    fValues = new Object[values.length];
    for (int i = 0; i < values.length; ++i) {
      fValues[i] = values[i];
    }
    fDivideBy = divideBy;
  }
  
  public Parameterization(double[] values, boolean divideBy) {
    fType = double.class;
    fValues = new Object[values.length];
    for (int i = 0; i < values.length; ++i) {
      fValues[i] = values[i];
    }
    fDivideBy = divideBy;
  }
  
  public Parameterization(boolean[] values) {
    fType = boolean.class;
    fValues = new Object[values.length];
    for (int i = 0; i < values.length; ++i) {
      fValues[i] = values[i];
    }
    fDivideBy = false;
  }
  
  public Parameterization(long[] values, boolean divideBy) {
    fType = long.class;
    fValues = new Object[values.length];
    for (int i = 0; i < values.length; ++i) {
      fValues[i] = values[i];
    }
    fDivideBy = divideBy;
  }
  
  public Class<?> getType() {
    return fType;
  }
  
  public Object getValue(int index) {
    return fValues[index];
  }
  
  public int size() {
    return fValues.length;
  }
  
  public boolean getDivideBy() {
    return fDivideBy;
  }
  
  public static Parameterization create(Method method, Class<?> parameterType, Annotation[] annotations) {
    return create(method.getDeclaringClass(), method, parameterType, annotations);
  }
  
  public static Parameterization create(Class<?> declaringClass, AccessibleObject object, Class<?> parameterType, Annotation[] annotations) {
    boolean divideBy = false;
    for (Annotation annotation: annotations) {
      if (annotation instanceof DivideBy) {
        divideBy = true;
      }
    }
    
    for (Annotation annotation: annotations) {
      
      ForEachInt forEachInt = ObjectUtil.castOrNull(ForEachInt.class, annotation);
      if (forEachInt != null) {
        checkType(declaringClass, object, parameterType, int.class);
        return new Parameterization(forEachInt.value(), divideBy);
      }
      
      ForEachString forEachString = ObjectUtil.castOrNull(ForEachString.class, annotation);
      if (forEachString != null) {
        checkType(declaringClass, object, parameterType, String.class);
        return new Parameterization(forEachString.value());
      }
      
      ForEachDouble forEachDouble = ObjectUtil.castOrNull(ForEachDouble.class, annotation);
      if (forEachDouble != null) {
        checkType(declaringClass, object, parameterType, double.class);
        return new Parameterization(forEachDouble.value(), divideBy);
      }
      
      ForEachFloat forEachFloat = ObjectUtil.castOrNull(ForEachFloat.class, annotation);
      if (forEachFloat != null) {
        checkType(declaringClass, object, parameterType, float.class);
        return new Parameterization(forEachFloat.value(), divideBy);
      }
      
      ForEachLong forEachLong = ObjectUtil.castOrNull(ForEachLong.class, annotation);
      if (forEachLong != null) {
        checkType(declaringClass, object, parameterType, long.class);
        return new Parameterization(forEachLong.value(), divideBy);
      }
      
      ForEachBoolean forEachBoolean = ObjectUtil.castOrNull(ForEachBoolean.class, annotation);
      if (forEachBoolean != null) {
        checkType(declaringClass, object, parameterType, boolean.class);
        return new Parameterization(forEachBoolean.value());
      }
      
    }
    
    throw new BenchmarkClassError(declaringClass, "All arguments to " + object.toString()
        + " need annotations to define its values during benchmark.");
  }
  
  private static void checkType(Class<?> declaringClass, AccessibleObject object, Class<?> actual, Class<?> expected) {
    if (expected.isAssignableFrom(actual)) {
      return;
    }
    throw new BenchmarkClassError(declaringClass, "Parameter of " + object + " has type " + actual.getName()
        + " which does not match annotation type " + expected.getName());
  }
  
  @Override
  public int hashCode() {
    return getClass().hashCode() + 31 * Arrays.hashCode(fValues) + 31 * 31 * fType.hashCode();
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Parameterization other = (Parameterization)obj;
    if (!Arrays.equals(fValues, other.fValues)) {
      return false;
    }
    if (fType != other.fType) {
      return false;
    }
    if (fDivideBy != other.fDivideBy) {
      return false;
    }
    return true;
  }
  
  @Override
  public String toString() {
    return getClass().getSimpleName() + Arrays.toString(fValues);
  }
  
}
