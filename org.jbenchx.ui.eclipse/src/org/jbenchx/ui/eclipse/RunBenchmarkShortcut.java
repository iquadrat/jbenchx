package org.jbenchx.ui.eclipse;

import java.util.*;

import org.eclipse.core.runtime.*;
import org.eclipse.debug.core.*;
import org.eclipse.debug.ui.*;
import org.eclipse.jdt.launching.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.ui.*;
import org.jbenchx.remote.*;

public class RunBenchmarkShortcut implements ILaunchShortcut {
  
  private static final String LAUNCH_CONFIG_NAME = "Run JBenchX Benchmark";
  
  @Override
  public void launch(ISelection selection, String mode) {
    System.out.println("launch in mode " + mode);
    launch(Collections.<String>emptyList(), mode);
  }
  
  @Override
  public void launch(IEditorPart editor, String mode) {
    System.out.println("launch in mode " + mode);
    launch(Collections.<String>emptyList(), mode);
  }
  
  private void launch(List<String> benchmarkClasses, String mode) {
    
    try {
      ILaunchConfiguration config = getOrCreateLaunchConfiguration(benchmarkClasses);
      
      //ILaunch launch = config.launch(mode, new NullProgressMonitor());
      DebugUITools.launch(config, mode);
      
    } catch(CoreException e) {
      Activator.getDefault().logError(e);
    }
    
  }
  
  private ILaunchConfiguration getOrCreateLaunchConfiguration(List<String> benchmarkClasses) throws CoreException {
    // TODO make custom launch configuration
    ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
    ILaunchConfigurationType type = launchManager.getLaunchConfigurationType(IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);
    for (ILaunchConfiguration configuration: launchManager.getLaunchConfigurations(type)) {
      
      if (configuration.getName().equals(LAUNCH_CONFIG_NAME)) {
        return configuration.getWorkingCopy();
      }
    }
    return createLaunchConfiguration();
  }
  
  private ILaunchConfiguration createLaunchConfiguration() throws CoreException {
    ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
    ILaunchConfigurationType type = launchManager.getLaunchConfigurationType(IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);
    ILaunchConfigurationWorkingCopy result = type.newInstance(null, LAUNCH_CONFIG_NAME);

    // set attributes
    result.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, RemoteRunner.class.getName());
    result.setAttribute(IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, true);
    
    return result.doSave();
  }
  
}
