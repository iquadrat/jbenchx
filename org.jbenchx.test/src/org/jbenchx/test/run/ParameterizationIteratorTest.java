/*
 * Created on 09.10.2011
 *
 */
package org.jbenchx.test.run;

import java.util.*;

import org.jbenchx.run.*;
import org.jbenchx.test.*;
import org.jbenchx.testutil.*;
import org.junit.*;

public class ParameterizationIteratorTest extends BenchmarkTestCase {

  @Test
  public void noArgs() {
    ParameterizationIterator iterator = new ParameterizationIterator(Collections.<Parameterization>emptyList());
    ArrayList<ParameterizationValues> expected = new ArrayList<ParameterizationValues>();
    expected.add(new ParameterizationValues(new Class<?>[0], new Object[] {}));
    TestUtil.assertIteratesSequence(iterator, expected);
    testEndOfIteration(iterator);
  }

  @Test
  public void arg1d() {
    Parameterization param1 = new Parameterization(new int[] {10, 100, 1000});
    ParameterizationIterator iterator = new ParameterizationIterator(Arrays.asList(param1));

    Class<?>[] types = new Class<?>[] {int.class};

    ArrayList<ParameterizationValues> expected = new ArrayList<ParameterizationValues>();
    expected.add(new ParameterizationValues(types, new Object[] {10}));
    expected.add(new ParameterizationValues(types, new Object[] {100}));
    expected.add(new ParameterizationValues(types, new Object[] {1000}));

    TestUtil.assertIteratesSequence(iterator, expected);

    testEndOfIteration(iterator);
  }

  private void testEndOfIteration(ParameterizationIterator iterator) {
    Assert.assertFalse(iterator.hasNext());
    try {
      iterator.next();
      failExpectedThrowable(NoSuchElementException.class);
    } catch(NoSuchElementException e) {
      // pass
    }
    Assert.assertFalse(iterator.hasNext());
  }

  @Test
  public void arg2d() {
    Parameterization param1 = new Parameterization(new int[] {10, 100, 1000});
    Parameterization param2 = new Parameterization(new String[] {"foo", "bar"});
    ParameterizationIterator iterator = new ParameterizationIterator(Arrays.asList(param1, param2));

    Class<?>[] types = new Class<?>[] {int.class, String.class};

    ArrayList<ParameterizationValues> expected = new ArrayList<ParameterizationValues>();
    expected.add(new ParameterizationValues(types, new Object[] {10, "foo"}));
    expected.add(new ParameterizationValues(types, new Object[] {10, "bar"}));
    expected.add(new ParameterizationValues(types, new Object[] {100, "foo"}));
    expected.add(new ParameterizationValues(types, new Object[] {100, "bar"}));
    expected.add(new ParameterizationValues(types, new Object[] {1000, "foo"}));
    expected.add(new ParameterizationValues(types, new Object[] {1000, "bar"}));

    TestUtil.assertIteratesSequence(iterator, expected);

    testEndOfIteration(iterator);
  }

  @Test
  public void arg3d() {
    Parameterization param1 = new Parameterization(new int[] {10, 100, 1000});
    Parameterization param2 = new Parameterization(new String[] {"foo", "bar"});
    Parameterization param3 = new Parameterization(new float[] {3.0f});
    ParameterizationIterator iterator = new ParameterizationIterator(Arrays.asList(param1, param2, param3));

    Class<?>[] types = new Class<?>[] {int.class, String.class, float.class};

    ArrayList<ParameterizationValues> expected = new ArrayList<ParameterizationValues>();
    expected.add(new ParameterizationValues(types, new Object[] {10, "foo", 3.0f}));
    expected.add(new ParameterizationValues(types, new Object[] {10, "bar", 3.0f}));
    expected.add(new ParameterizationValues(types, new Object[] {100, "foo", 3.0f}));
    expected.add(new ParameterizationValues(types, new Object[] {100, "bar", 3.0f}));
    expected.add(new ParameterizationValues(types, new Object[] {1000, "foo", 3.0f}));
    expected.add(new ParameterizationValues(types, new Object[] {1000, "bar", 3.0f}));

    TestUtil.assertIteratesSequence(iterator, expected);

    testEndOfIteration(iterator);
  }

}
