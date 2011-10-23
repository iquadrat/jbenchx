/*
 * Created on 04.07.2007
 *
 */
package org.jbenchx.util;

public class Assert {

  private Assert() {
    // utility class
  }

  public static void equals(short expected, short actual) {
    if (expected == actual) {
      return;
    }
    fail("Actual '", actual, "' is not equal to expected '", expected, "'");
  }

  public static void equals(int expected, int actual) {
    if (expected == actual) {
      return;
    }
    fail("Actual '", actual, "' is not equal to expected '", expected, "'");
  }

  public static void equals(long expected, long actual) {
    if (expected == actual) {
      return;
    }
    fail("Actual '", actual, "' is not equal to expected '", expected, "'");
  }

  public static void equals(Object expected, Object actual) {
    if (ObjectUtil.equals(expected, actual)) {
      return;
    }
    fail("Actual '", actual, "' is not equal to expected '", expected, "'");
  }

  public static AssertionFailedError fail(Object... message) {
    throw new AssertionFailedError(toString(message));
  }

  public static AssertionFailedError fail(Throwable t) {
    throw new AssertionFailedError(t);
  }

  public static void isFalse(boolean condition) {
    isFalse(condition, "Assertion failed!");
  }

  public static void isFalse(boolean condition, Object... message) {
    if (!condition) {
      return;
    }
    fail(message);
  }

  public static void isNotNull(Object o) {
    if (o != null) {
      return;
    }
    fail("Expected an object but was null!");
  }

  public static void isNotNull(Object o, Object... message) {
    if (o != null) {
      return;
    }
    fail(message);
  }

  public static void isNull(Object o) {
    if (o == null) {
      return;
    }
    fail("Expected null but was ", o, "!");
  }

  public static void isNull(Object o, Object... message) {
    if (o == null) {
      return;
    }
    fail(message);
  }

  public static void isTrue(boolean condition) {
    isTrue(condition, "Assertion failed!");
  }

  public static void isTrue(boolean condition, Object... message) {
    if (condition) {
      return;
    }
    fail(message);
  }

  private static String toString(Object[] message) {
    StringBuilder builder = new StringBuilder();
    for (Object o: message) {
      builder.append(o.toString());
    }
    return builder.toString();
  }

  public static void same(Object o1, Object o2) {
    if (o1 == o2) {
      return;
    }
    fail("Object "+o1+" is not same as "+o2);
  }

  public static void log(String string) {
    // TODO logger
    System.err.println(string);
  }

}
