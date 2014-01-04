/*
 * Created on 09.10.2011
 *
 */
package org.jbenchx.test.run;

import java.lang.reflect.Method;

import org.jbenchx.annotations.Bench;
import org.jbenchx.annotations.DivideBy;
import org.jbenchx.annotations.ForEachBoolean;
import org.jbenchx.annotations.ForEachDouble;
import org.jbenchx.annotations.ForEachFloat;
import org.jbenchx.annotations.ForEachInt;
import org.jbenchx.annotations.ForEachLong;
import org.jbenchx.annotations.ForEachString;
import org.jbenchx.result.BenchmarkClassError;
import org.jbenchx.run.Parameterization;
import org.junit.Assert;
import org.junit.Test;

public class ParameterizationTest {
  
  @SuppressWarnings("unused")
  public static class ParamForEachBench {
    
    @Bench
    public void forEachInt(@DivideBy @ForEachInt({10, 100, 1000, 10000}) int size) {}
    
    @Bench
    public void forEachString(@ForEachString({"foo", "bar", "moo"}) String keys) {}
    
    @Bench
    public void forEachLong(@ForEachLong({1, 2, 3, 1L << 44}) long keys) {}
    
    @Bench
    public void forEachDouble(@ForEachDouble({1, 2.0, 3.5}) double keys) {}
    
    @Bench
    public void forEachFloat(@ForEachFloat({-1, 2.4f}) float keys) {}
    
    @Bench
    public void forEachBoolean(@ForEachBoolean({true, false}) boolean on) {}
    
    @Bench
    public void forEachMissing(int size) {}
    
    @Bench
    public void forEachWrong(@ForEachFloat({1}) int size) {}
    
  }
  
  private Parameterization createParameterization(Method method) {
    return Parameterization.create(method, method.getParameterTypes()[0], method.getParameterAnnotations()[0]);
  }
  
  @Test
  public void forEachInt() throws Exception {
    Method method = ParamForEachBench.class.getMethod("forEachInt", int.class);
    Assert.assertEquals(new Parameterization(new int[] {10, 100, 1000, 10000}, true), createParameterization(method));
  }
  
  @Test
  public void forEachString() throws Exception {
    Method method = ParamForEachBench.class.getMethod("forEachString", String.class);
    Assert.assertEquals(new Parameterization(new String[] {"foo", "bar", "moo"}), createParameterization(method));
  }
  
  @Test
  public void forEachLong() throws Exception {
    Method method = ParamForEachBench.class.getMethod("forEachLong", long.class);
    Assert.assertEquals(new Parameterization(new long[] {1, 2, 3, 1L << 44}, false), createParameterization(method));
  }
  
  @Test
  public void forEachDouble() throws Exception {
    Method method = ParamForEachBench.class.getMethod("forEachDouble", double.class);
    Assert.assertEquals(new Parameterization(new double[] {1, 2.0, 3.5}, false), createParameterization(method));
  }
  
  @Test
  public void forEachFloat() throws Exception {
    Method method = ParamForEachBench.class.getMethod("forEachFloat", float.class);
    Assert.assertEquals(new Parameterization(new float[] {-1, 2.4f}, false), createParameterization(method));
  }
  
  @Test
  public void forEachBoolean() throws Exception {
    Method method = ParamForEachBench.class.getMethod("forEachBoolean", boolean.class);
    Assert.assertEquals(new Parameterization(new boolean[] {true, false}), createParameterization(method));
  }
  
  @Test
  public void forEachMissing() throws Exception {
    Method method = ParamForEachBench.class.getMethod("forEachMissing", int.class);
    try {
      createParameterization(method);
    } catch (BenchmarkClassError e) {
      Assert.assertEquals(ParamForEachBench.class, e.getAffectedClass());
    }
  }
  
  @Test
  public void forEachWrong() throws Exception {
    Method method = ParamForEachBench.class.getMethod("forEachWrong", int.class);
    try {
      createParameterization(method);
    } catch (BenchmarkClassError e) {
      Assert.assertEquals(ParamForEachBench.class, e.getAffectedClass());
    }
  }
  
}
