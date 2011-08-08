package org.jbenchx.test;

import java.lang.reflect.*;

import javax.annotation.CheckForNull;

import junit.framework.*;
import edu.umd.cs.findbugs.annotations.*;

public abstract class BenchmarkTestCase extends TestCase {
  
  @Override
  @OverrideMustInvoke
  protected void setUp() throws Exception {
    super.setUp();
  }
  
  @Override
  @OverrideMustInvoke
  protected void tearDown() throws Exception {
    clearAllFields();
    super.tearDown();
  }
  
  /**
   * Clears non-primitive-type non-final non-static fields of the this object
   * that come from {@link BenchmarkTestCase} or subclasses of it.
   */
  private void clearAllFields() throws Exception {
    clearAllFields(getClass());
  }
  
  /**
   * Helper class for {@link #clearAllFields()}.
   */
  private void clearAllFields(Class<?> klass) throws Exception {
    for (Field field: klass.getDeclaredFields()) {
      if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) continue;
      if (field.getType().isPrimitive()) continue;
      field.setAccessible(true);
      field.set(this, null);
    }
    if (BenchmarkTestCase.class.equals(klass)) return;
    clearAllFields(klass.getSuperclass());
  }
  
  public static void failExpectedThrowable(Class<? extends Throwable> throwable) {
    fail("expected " + throwable.getSimpleName());
  }
  
  public static AssertionFailedError fail(Throwable e) {
    throw fail(e.getMessage(), e);
  }
  
  public static AssertionFailedError fail(String message, @CheckForNull Throwable e) {
    AssertionFailedError assertionFailedError = new AssertionFailedError(message);
    if (e != null) assertionFailedError.initCause(e);
    throw assertionFailedError;
  }
  
}
