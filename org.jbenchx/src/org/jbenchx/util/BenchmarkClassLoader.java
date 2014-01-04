package org.jbenchx.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class BenchmarkClassLoader extends ClassLoader {
  
  private final Map<String, Class<?>> fMyClasses = new HashMap<String, Class<?>>();
  
  public BenchmarkClassLoader() {
    super(ClassLoader.getSystemClassLoader());
  }
  
  @Override
  public Class<?> loadClass(String name) throws ClassNotFoundException {
    // TODO good way to handle this?
    if (name.startsWith("java.") || name.startsWith("sun.") || name.startsWith("javax.") || name.startsWith("org.jbenchx.")) {
      return super.loadClass(name);
    }
    
    Class<?> cached = fMyClasses.get(name);
    if (cached != null) {
      return cached;
    }
    
    URL resources = getParent().getResource(name.replaceAll("\\.", "/") + ".class");
    
    if (resources == null) {
      return super.loadClass(name);
    }
    
    InputStream in;
    try {
      in = resources.openStream();
      byte[] data = readToEnd(in);
      in.close();
      Class<?> clazz = defineClass(name, data, 0, data.length);
      fMyClasses.put(name, clazz);
      return clazz;
    } catch (IOException e) {}
    
    throw new ClassNotFoundException(name);
  }
  
  public static byte[] readToEnd(InputStream input) throws IOException {
    ByteArrayOutputStream builder = new ByteArrayOutputStream();
    copyInputStreamToOutputStream(input, builder);
    return builder.toByteArray();
  }
  
  public static final void copyInputStreamToOutputStream(InputStream in, OutputStream out) throws IOException {
    byte[] buffer = new byte[1024 * 4];
    int len;
    while ((len = in.read(buffer)) >= 0) {
      out.write(buffer, 0, len);
    }
  }
  
}
