package org.jbenchx;

import java.io.*;
import java.net.*;

public class BenchmarkClassLoader extends ClassLoader {
  
  public BenchmarkClassLoader() {
    super(ClassLoader.getSystemClassLoader());
  }
  
  @Override
  public Class<?> loadClass(String name) throws ClassNotFoundException {
    // TODO good way to handle this?
    if (name.startsWith("java.") || name.startsWith("sun.") || name.startsWith("javax.")) {
      return super.loadClass(name);
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
      return defineClass(name, data, 0, data.length);
    } catch (IOException e) {
    }
    
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
  
  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException {
    System.out.println("find " + name);
    return super.findClass(name);
  }
  
}
