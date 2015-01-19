package org.jbenchx.ui.eclipse.classpath;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.CheckForNull;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

public class JBenchXContainerInitializer extends ClasspathContainerInitializer {
  
  public static class ActifsourceClasspathContainer implements IClasspathContainer {
    
    private final IPath       fPath;
    
    @CheckForNull
    private IClasspathEntry[] fClassPathEntries;
    
    public ActifsourceClasspathContainer(IPath path) {
      fPath = path;
    }
    
    @Override
    public IClasspathEntry[] getClasspathEntries() {
      if (fClassPathEntries != null) {
        return fClassPathEntries;
      }
      return calculateClassPathEntries();
    }
    
    private IClasspathEntry[] calculateClassPathEntries() {
      List<String> requiredBundles = Arrays.asList("org.jbenchx", "org.jbenchx.libs_external");
      
      Set<URI> classPath = new LinkedHashSet<URI>();
      for (String bundleName: requiredBundles) {
        try {
          
          String name = bundleName;
          Bundle bundle = Platform.getBundle(name);
          classPath.addAll(PluginUtil.getClassPath(bundle, false));
          
        } catch (BundleException e) {
        } catch (IOException e) {
        }
      }
      
      List<IClasspathEntry> classPathEntries = new ArrayList<IClasspathEntry>();
      for (URI url: classPath) {
        Path path = new Path(url.getPath());
        classPathEntries.add(JavaCore.newLibraryEntry(path, null, null));
      }
      
      fClassPathEntries = classPathEntries.toArray(new IClasspathEntry[classPathEntries.size()]);
      return fClassPathEntries;
    }
    
    @Override
    public String getDescription() {
      return "JBenchX";
    }
    
    @Override
    public int getKind() {
      return IClasspathContainer.K_APPLICATION;
    }
    
    @Override
    public IPath getPath() {
      return fPath;
    }
    
  }
  
  @Override
  public void initialize(IPath path, IJavaProject javaProject) throws CoreException {
    IClasspathContainer[] containers = {new ActifsourceClasspathContainer(path)};
    JavaCore.setClasspathContainer(path, new IJavaProject[] {javaProject}, containers, null);
  }
  
}
