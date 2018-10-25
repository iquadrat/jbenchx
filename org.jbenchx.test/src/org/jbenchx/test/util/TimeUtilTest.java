package org.jbenchx.test.util;

import static org.junit.Assert.assertEquals;

import org.jbenchx.test.BenchmarkTestCase;
import org.jbenchx.util.TimeUtil;
import org.junit.Test;


public class TimeUtilTest extends BenchmarkTestCase {

  @Test
  public void doubleToString() {
    assertEquals("0.00ns", TimeUtil.toString(0.0));
    assertEquals("0.03ns", TimeUtil.toString(0.03));
    assertEquals("0.47ns", TimeUtil.toString(0.47));
    assertEquals("0.50ns", TimeUtil.toString(0.499));
    assertEquals("1.23ns", TimeUtil.toString(1.2345));
    assertEquals("9.95ns", TimeUtil.toString(9.949));
    assertEquals("9.95ns", TimeUtil.toString(9.95));
    assertEquals("9.99ns", TimeUtil.toString(9.99));
    assertEquals("10.0ns", TimeUtil.toString(9.995));
    assertEquals("11.1ns", TimeUtil.toString(11.05));
    assertEquals("49.5ns", TimeUtil.toString(49.49));
    assertEquals("99.2ns", TimeUtil.toString(99.15));
    assertEquals("99.6ns", TimeUtil.toString(99.55));
    assertEquals("99.9ns", TimeUtil.toString(99.949));
    assertEquals("100ns", TimeUtil.toString(99.95));
    assertEquals("1.23ms", TimeUtil.toString(1234567.89));
  }
  
  @Test
  public void longToString() {
    assertEquals("0ns", TimeUtil.toString(0));
    assertEquals("1ns", TimeUtil.toString(1));
    assertEquals("-1ns", TimeUtil.toString(-1));
    assertEquals("9ns", TimeUtil.toString(9));
    assertEquals("10ns", TimeUtil.toString(10));
    assertEquals("100ns", TimeUtil.toString(100));
    assertEquals("101ns", TimeUtil.toString(101));
    assertEquals("999ns", TimeUtil.toString(999));
    assertEquals("1.00us", TimeUtil.toString(1000));
    assertEquals("-1.00us", TimeUtil.toString(-1000));
    assertEquals("1.00us", TimeUtil.toString(1004));
    assertEquals("1.01us", TimeUtil.toString(1005));
    assertEquals("1.06us", TimeUtil.toString(1055));
    assertEquals("9.99us", TimeUtil.toString(9991));
    assertEquals("10.0us", TimeUtil.toString(9999));
    assertEquals("10.0us", TimeUtil.toString(10000));
    assertEquals("10.0us", TimeUtil.toString(10009));
    assertEquals("10.0us", TimeUtil.toString(10049));
    assertEquals("10.1us", TimeUtil.toString(10050));
    assertEquals("99.9us", TimeUtil.toString(99940));
    assertEquals("100us", TimeUtil.toString(99950));
    assertEquals("100us", TimeUtil.toString(100000));
    assertEquals("101us", TimeUtil.toString(100500));
    assertEquals("321us", TimeUtil.toString(321098));
    assertEquals("999us", TimeUtil.toString(999499));
    assertEquals("1.00ms", TimeUtil.toString(999505));
    assertEquals("1.00ms", TimeUtil.toString(1000000));
    assertEquals("1.00ms", TimeUtil.toString(1000500));
    assertEquals("1.01ms", TimeUtil.toString(1005000));
    assertEquals("5.16ms", TimeUtil.toString(5162323));
    assertEquals("10.0ms", TimeUtil.toString(9999999));
    assertEquals("10.0ms", TimeUtil.toString(10000000));
    assertEquals("10.1ms", TimeUtil.toString(10091234));
    assertEquals("99.9ms", TimeUtil.toString(99949999));
    assertEquals("100ms", TimeUtil.toString(99999999));
    assertEquals("100ms", TimeUtil.toString(100000000));
    assertEquals("615ms", TimeUtil.toString(614643434));
    assertEquals("1.00s", TimeUtil.toString(999999999L));
    assertEquals("1.00s", TimeUtil.toString(1000000000L));
    assertEquals("16.6s", TimeUtil.toString(16560000004L));
    assertEquals("100s", TimeUtil.toString(99999999999L));
    assertEquals("200s", TimeUtil.toString(199999999999L));
    assertEquals("1230s", TimeUtil.toString(1234567890123L));
    assertEquals("91200s", TimeUtil.toString(91234567890123L));
  }

}
