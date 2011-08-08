package org.jbenchx.vm;

import java.lang.management.*;
import java.net.*;

public class VmInfo {
  
  private static final VmInfo INSTANCE = new VmInfo();
  
  private String fVmName;
  private String fOsName;
  private int    fProcessors;
  private String fJitCompiler;
  private String fHostName;
  
  public static VmInfo getVmInfo() {
    return INSTANCE;
  }
  
  private VmInfo() {
    fVmName = System.getProperty("java.runtime.name") + "-" + System.getProperty("java.runtime.version"); // ManagementFactory.getRuntimeMXBean().getVmName() + "-" + ManagementFactory.getRuntimeMXBean().getVmVersion();
    fOsName = System.getProperty("os.name") + "-" + System.getProperty("os.arch") + "-" + System.getProperty("os.version");
    fProcessors = ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
    fJitCompiler = ManagementFactory.getCompilationMXBean().getName();
    fHostName = getHostName();
  }
  
  private String getHostName() {
    try {
      InetAddress addr = InetAddress.getLocalHost();
      return addr.getHostName();
    } catch (UnknownHostException e) {
      return "?";
    }
  }
  
  @Override
  public String toString() {
    return fHostName + ":" + fOsName + ":" + fVmName + ":" + fJitCompiler + ":" + fProcessors;
  }
  
}
