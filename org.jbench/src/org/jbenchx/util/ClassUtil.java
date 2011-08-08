package org.jbenchx.util;

import java.lang.reflect.*;
import java.security.*;

import org.jbenchx.*;

public class ClassUtil {
  
  public static boolean hasDefaultConstructor(Class<?> clazz) throws SecurityException {
    try {
      Constructor<?> constructor = clazz.getConstructor();
      if (!Modifier.isPublic(constructor.getModifiers())) {
        return false;
      }
      return true;
    } catch (NoSuchMethodException e) {
      return false;
    }
  }
  
  public static ClassLoader createClassLoader() {
    return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
      public ClassLoader run() {
        return new BenchmarkClassLoader();
      }
    });
  }
  
}
