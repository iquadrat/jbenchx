package org.jbenchx.test;

import javax.annotation.*;

import junit.framework.*;

import org.junit.Assert;

public abstract class BenchmarkTestCase {

  public static void failExpectedThrowable(Class<? extends Throwable> throwable) {
    Assert.fail("expected " + throwable.getSimpleName());
  }

  public static AssertionFailedError fail(Throwable e) {
    throw fail(e.getMessage(), e);
  }

  public static AssertionFailedError fail(String message, @CheckForNull Throwable e) {
    AssertionFailedError assertionFailedError = new AssertionFailedError(message);
    if (e != null) {
      assertionFailedError.initCause(e);
    }
    throw assertionFailedError;
  }

}
