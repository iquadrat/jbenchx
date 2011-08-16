package org.jbenchx.ui.eclipse.launch;

import java.util.*;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.*;
import org.eclipse.debug.ui.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.jbenchx.remote.RemoteRunner;
import org.jbenchx.ui.eclipse.*;
import org.jbenchx.util.StringUtil;

public class RunBenchmarkShortcut implements ILaunchShortcut {
  
  @Override
  public void launch(ISelection selection, String mode) {
    launchWithJavaElements(getJavaElements(EclipseUtil.extractElements(selection)), mode);
  }
  
  @Override
  public void launch(IEditorPart editor, String mode) {
    IResource resource = (IResource)editor.getEditorInput().getAdapter(IResource.class);
    if (resource == null) return;
    launchWithJavaElements(getJavaElements(Arrays.asList(resource)), mode);
  }
  
  private List<IJavaElement> getJavaElements(Iterable<?> elements) {
    List<IJavaElement> result = new ArrayList<IJavaElement>();
    for (Object element: elements) {
      IJavaElement javaElement = EclipseUtil.adapt(element, IJavaElement.class);
      if (javaElement != null) {
        result.add(javaElement);
      }
    }
    return result;
  }
  
  private void launchWithJavaElements(List<IJavaElement> javaElements, String mode) {
    LinkedHashSet<String> benchmarks = new LinkedHashSet<String>();
    IJavaProject javaProject = null;
    
    for (IJavaElement javaElement: javaElements) {
      IJavaElement primaryElement = javaElement.getPrimaryElement();
      if (!primaryElement.exists()) continue;
      
      if (primaryElement instanceof ITypeRoot) {
        ITypeRoot typeRoot = (ITypeRoot)primaryElement;
        IType type = typeRoot.findPrimaryType();
        if (type != null) {
          javaProject = javaElement.getJavaProject();
          benchmarks.add(type.getFullyQualifiedName());
        }
      }
    }
    
    if (javaElements.isEmpty() || javaProject == null) return;
    
    launchWithBenchmarks(new ArrayList<String>(benchmarks), javaProject, mode);
  }
  
  private void launchWithBenchmarks(List<String> benchmarkClasses, IJavaProject javaProject, String mode) {
    
    try {
      ILaunchConfiguration config = getOrCreateLaunchConfiguration(benchmarkClasses, javaProject);
      
      //ILaunch launch = config.launch(mode, new NullProgressMonitor());
      DebugUITools.launch(config, mode);
      
    } catch (CoreException e) {
      Activator.getDefault().logError(e);
    }
    
  }
  
  private ILaunchConfiguration getOrCreateLaunchConfiguration(List<String> benchmarkClasses, IJavaProject javaProject) throws CoreException {
    // TODO make custom launch configuration
    ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
    ILaunchConfigurationType type = launchManager.getLaunchConfigurationType(JBenchXLaunchConfig.ID_JBENCHX_BENCHMARK);
    for (ILaunchConfiguration configuration: launchManager.getLaunchConfigurations(type)) {
      
      if (configuration.getName().equals(JBenchXLaunchConfig.LAUNCH_CONFIG_NAME)) {
        return configuration.getWorkingCopy();
      }
    }
    return createLaunchConfiguration(benchmarkClasses, javaProject);
  }
  
  private ILaunchConfiguration createLaunchConfiguration(List<String> benchmarkClasses, IJavaProject javaProject) throws CoreException {
    ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
    ILaunchConfigurationType type = launchManager.getLaunchConfigurationType(JBenchXLaunchConfig.ID_JBENCHX_BENCHMARK);
    ILaunchConfigurationWorkingCopy result = type.newInstance(null, JBenchXLaunchConfig.LAUNCH_CONFIG_NAME);
    
    // set attributes
    result.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, RemoteRunner.class.getName());
    result.setAttribute(IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, true);
    result.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, javaProject.getElementName());
    result.setAttribute(JBenchXLaunchConfig.ATTR_JBENCHX_BENCHMARKS, StringUtil.join(",", benchmarkClasses));
    
    return result.doSave();
  }
  
}
