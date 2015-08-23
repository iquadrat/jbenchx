package org.jbenchx.test.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;

import org.jbenchx.util.StringUtil;
import org.junit.Test;

/**
 * Unit tests for {@link StringUtil}.
 */
public class StringUtilTest {
  
  @Test
  public void wildCardMatchEmpty() {
    Pattern pattern = StringUtil.wildCardToRegexpPattern("");
    assertTrue(pattern.matcher("").matches());
    assertFalse(pattern.matcher("a").matches());
    assertFalse(pattern.matcher("foobar").matches());
  }
  
  @Test
  public void wildCardMatchAll() {
    Pattern pattern = StringUtil.wildCardToRegexpPattern("*");
    assertTrue(pattern.matcher("").matches());
    assertTrue(pattern.matcher("a").matches());
    assertTrue(pattern.matcher("foobar").matches());
  }
  
  @Test
  public void wildCardMatchSingleChar() {
    Pattern pattern = StringUtil.wildCardToRegexpPattern("?");
    assertFalse(pattern.matcher("").matches());
    assertTrue(pattern.matcher("a").matches());
    assertFalse(pattern.matcher("foobar").matches());
  }
  
  @Test
  public void wildCardMatchBegin() {
    Pattern pattern = StringUtil.wildCardToRegexpPattern("*foo");
    assertTrue(pattern.matcher("foo").matches());
    assertTrue(pattern.matcher("barfoo").matches());
    assertFalse(pattern.matcher("bar").matches());
    assertFalse(pattern.matcher("foobar").matches());
  }
  
  @Test
  public void wildCardMatchEnd() {
    Pattern pattern = StringUtil.wildCardToRegexpPattern("foo*");
    assertTrue(pattern.matcher("foo").matches());
    assertFalse(pattern.matcher("barfoo").matches());
    assertFalse(pattern.matcher("bar").matches());
    assertTrue(pattern.matcher("foobar").matches());
  }
  
  @Test
  public void wildCardMatchComplex() {
    Pattern pattern = StringUtil.wildCardToRegexpPattern("?foo*a??fa*b*");
    assertTrue(pattern.matcher("afooaxxfab").matches());
    assertTrue(pattern.matcher("xfoonogasogaaxxfaaaaaboulous").matches());
    assertFalse(pattern.matcher("fooaxxfab").matches());
    assertFalse(pattern.matcher("afooaxxfa").matches());
    assertFalse(pattern.matcher("afooaxxfboobo").matches());
  }
  
}
