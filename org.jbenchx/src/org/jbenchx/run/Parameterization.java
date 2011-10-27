/*
 * Created on 09.10.2011
 *
 */
package org.jbenchx.run;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import net.jcip.annotations.Immutable;

import org.jbenchx.annotations.*;
import org.jbenchx.result.*;
import org.jbenchx.util.*;

@Immutable
public class Parameterization {

  private final Class<?> fType;

  private final Object[] fValues;

  public Parameterization(int[] values) {
    fType = int.class;
    fValues = new Object[values.length];
    for (int i = 0; i < values.length; ++i) {
      fValues[i] = values[i];
    }
  }

  public Parameterization(String[] values) {
    fType = String.class;
    fValues = new Object[values.length];
    for (int i = 0; i < values.length; ++i) {
      fValues[i] = values[i];
    }
  }

  public Parameterization(float[] values) {
    fType = float.class;
    fValues = new Object[values.length];
    for (int i = 0; i < values.length; ++i) {
      fValues[i] = values[i];
    }
  }

  public Parameterization(double[] values) {
    fType = double.class;
    fValues = new Object[values.length];
    for (int i = 0; i < values.length; ++i) {
      fValues[i] = values[i];
    }
  }

  public Parameterization(boolean[] values) {
    fType = boolean.class;
    fValues = new Object[values.length];
    for (int i = 0; i < values.length; ++i) {
      fValues[i] = values[i];
    }
  }

  public Parameterization(long[] values) {
    fType = long.class;
    fValues = new Object[values.length];
    for (int i = 0; i < values.length; ++i) {
      fValues[i] = values[i];
    }
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
  
  public static Parameterization create(Method method, Class<?> parameterType, Annotation[] annotations) {
    return create(method.getDeclaringClass(), method, parameterType, annotations);
  }

  public static Parameterization create(Class<?> declaringClass, AccessibleObject object, Class<?> parameterType, Annotation[] annotations) {
    for (Annotation annotation: annotations) {

      ForEachInt forEachInt = ObjectUtil.castOrNull(ForEachInt.class, annotation);
      if (forEachInt != null) {
        checkType(declaringClass, object, parameterType, int.class);
        return new Parameterization(forEachInt.value());
      }

      ForEachString forEachString = ObjectUtil.castOrNull(ForEachString.class, annotation);
      if (forEachString != null) {
        checkType(declaringClass, object, parameterType, String.class);
        return new Parameterization(forEachString.value());
      }

      ForEachDouble forEachDouble = ObjectUtil.castOrNull(ForEachDouble.class, annotation);
      if (forEachDouble != null) {
        checkType(declaringClass, object, parameterType, double.class);
        return new Parameterization(forEachDouble.value());
      }

      ForEachFloat forEachFloat = ObjectUtil.castOrNull(ForEachFloat.class, annotation);
      if (forEachFloat != null) {
        checkType(declaringClass, object, parameterType, float.class);
        return new Parameterization(forEachFloat.value());
      }

      ForEachLong forEachLong = ObjectUtil.castOrNull(ForEachLong.class, annotation);
      if (forEachLong != null) {
        checkType(declaringClass, object, parameterType, long.class);
        return new Parameterization(forEachLong.value());
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
    return true;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + Arrays.toString(fValues);
  }

}
