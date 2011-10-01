/*
 * Created on 30.05.2010
 *
 */
package org.jbenchx.util;

import javax.annotation.CheckForNull;

public class ObjectUtil {

  private ObjectUtil() {
    // utility class
  }

  public static boolean equals(@CheckForNull Object object1, @CheckForNull Object object2) {
    if (object1 == null) {
      return (object2 == null);
    }
    return object1.equals(object2);
  }

  @CheckForNull
  public static <T> T castOrNull(Class<T> clazz, @CheckForNull Object object) {
    if (!clazz.isInstance(object)) {
      return null;
    }
    return clazz.cast(object);
  }

}
