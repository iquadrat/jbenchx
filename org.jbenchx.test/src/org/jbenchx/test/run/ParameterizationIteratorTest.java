/*
 * Created on 09.10.2011
 *
 */
package org.jbenchx.test.run;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.jbenchx.run.Parameterization;
import org.jbenchx.run.ParameterizationIterable;
import org.jbenchx.run.ParameterizationValues;
import org.jbenchx.test.BenchmarkTestCase;
import org.jbenchx.testutil.TestUtil;
import org.junit.Assert;
import org.junit.Test;

public class ParameterizationIteratorTest extends BenchmarkTestCase {
  
  @Test
  public void noArgs() {
    Iterator<ParameterizationValues> iterator = new ParameterizationIterable(Collections.<Parameterization>emptyList()).iterator();
    ArrayList<ParameterizationValues> expected = new ArrayList<ParameterizationValues>();
    expected.add(new ParameterizationValues(Arrays.asList(), 1));
    TestUtil.assertIteratesSequence(iterator, expected);
    testEndOfIteration(iterator);
  }
  
  @Test
  public void arg1d() {
    Parameterization param1 = new Parameterization(new int[] {10, 100, 1000}, false);
    Iterator<ParameterizationValues> iterator = new ParameterizationIterable(Arrays.asList(param1)).iterator();
    
    ArrayList<ParameterizationValues> expected = new ArrayList<ParameterizationValues>();
    expected.add(new ParameterizationValues(Arrays.<Object>asList(10), 1));
    expected.add(new ParameterizationValues(Arrays.<Object>asList(100), 1));
    expected.add(new ParameterizationValues(Arrays.<Object>asList(1000), 1));
    
    TestUtil.assertIteratesSequence(iterator, expected);
    
    testEndOfIteration(iterator);
  }
  
  private void testEndOfIteration(Iterator<ParameterizationValues> iterator) {
    Assert.assertFalse(iterator.hasNext());
    try {
      iterator.next();
      failExpectedThrowable(NoSuchElementException.class);
    } catch (NoSuchElementException e) {
      // pass
    }
    Assert.assertFalse(iterator.hasNext());
  }
  
  @Test
  public void arg2d() {
    Parameterization param1 = new Parameterization(new int[] {10, 100, 1000}, true);
    Parameterization param2 = new Parameterization(new String[] {"foo", "bar"});
    Iterator<ParameterizationValues> iterator = new ParameterizationIterable(Arrays.asList(param1, param2)).iterator();
    
    ArrayList<ParameterizationValues> expected = new ArrayList<ParameterizationValues>();
    expected.add(new ParameterizationValues(Arrays.<Object>asList(10, "foo"), 10));
    expected.add(new ParameterizationValues(Arrays.<Object>asList(10, "bar"), 10));
    expected.add(new ParameterizationValues(Arrays.<Object>asList(100, "foo"), 100));
    expected.add(new ParameterizationValues(Arrays.<Object>asList(100, "bar"), 100));
    expected.add(new ParameterizationValues(Arrays.<Object>asList(1000, "foo"), 1000));
    expected.add(new ParameterizationValues(Arrays.<Object>asList(1000, "bar"), 1000));
    
    TestUtil.assertIteratesSequence(iterator, expected);
    
    testEndOfIteration(iterator);
  }
  
  @Test
  public void arg3d() {
    Parameterization param1 = new Parameterization(new int[] {10, 100, 1000}, false);
    Parameterization param2 = new Parameterization(new String[] {"foo", "bar"});
    Parameterization param3 = new Parameterization(new float[] {3.0f}, true);
    Iterator<ParameterizationValues> iterator = new ParameterizationIterable(Arrays.asList(param1, param2, param3)).iterator();
    
    ArrayList<ParameterizationValues> expected = new ArrayList<ParameterizationValues>();
    expected.add(new ParameterizationValues(Arrays.<Object>asList(10, "foo", 3.0f), 3.0));
    expected.add(new ParameterizationValues(Arrays.<Object>asList(10, "bar", 3.0f), 3.0));
    expected.add(new ParameterizationValues(Arrays.<Object>asList(100, "foo", 3.0f), 3.0));
    expected.add(new ParameterizationValues(Arrays.<Object>asList(100, "bar", 3.0f), 3.0));
    expected.add(new ParameterizationValues(Arrays.<Object>asList(1000, "foo", 3.0f), 3.0));
    expected.add(new ParameterizationValues(Arrays.<Object>asList(1000, "bar", 3.0f), 3.0));
    
    TestUtil.assertIteratesSequence(iterator, expected);
    
    testEndOfIteration(iterator);
  }
  
}
