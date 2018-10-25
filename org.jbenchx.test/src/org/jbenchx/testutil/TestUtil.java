/*
 * Created on 09.10.2011
 *
 */
package org.jbenchx.testutil;

import java.util.Iterator;
import java.util.List;

import org.junit.Assert;

public class TestUtil {

  private TestUtil() {}

  /**
   * Asserts that the <code>actual</code> iterator iterates the sequence of elements as
   * given by the <code>expectation</code> list.
   */
  public static <T> void assertIteratesSequence(Iterator<T> actual, List<T> expectation) {
    for (T currentExpected: expectation) {
      Assert.assertTrue("element missing in actual iterator: " + currentExpected, actual.hasNext());
      Assert.assertEquals(currentExpected, actual.next());
    }

    int elementsLeft = TestUtil.count(actual);
    if (elementsLeft != 0) {
      Assert.fail("Too many elements in actual iterator: " + elementsLeft);
    }
  }

  /**
   * @return the number of elements left in the iteration of given <code>iterator</code>
   */
  public static <T> int count(Iterator<T> iterator) {
    int counter = 0;
    while (iterator.hasNext()) {
      iterator.next();
      ++counter;
    }
    return counter;
  }

}
