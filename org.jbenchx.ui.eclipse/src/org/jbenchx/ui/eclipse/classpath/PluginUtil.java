package org.jbenchx.ui.eclipse.classpath;

import java.io.*;
import java.net.*;
import java.util.*;

import org.eclipse.core.runtime.*;
import org.eclipse.osgi.util.*;
import org.osgi.framework.*;

public class PluginUtil {
  
  private static Collection<ManifestElement> getManifestElements(Bundle bundle, String headerField) throws BundleException {
    ManifestElement[] prereqs = ManifestElement.parseHeader(headerField, bundle.getHeaders("").get(headerField));
    if (prereqs == null) return Collections.emptySet();
    return Arrays.asList(prereqs);
  }
  
  private static Collection<Bundle> getRequiredBundles(Bundle bundle, boolean recursive, Collection<Bundle> requiredBundles) throws BundleException {
    Collection<ManifestElement> requiredBundleElements = getManifestElements(bundle, Constants.REQUIRE_BUNDLE);
    for (ManifestElement prereq: requiredBundleElements) {
      if (requiredBundles.contains(prereq.getValue())) continue;
      Bundle requiredBundle = Platform.getBundle(prereq.getValue());
      requiredBundles.add(requiredBundle);
      if (recursive) {
        getRequiredBundles(requiredBundle, true, requiredBundles);
      }
    }
    return requiredBundles;
  }
  
  private static Collection<Bundle> getRequiredBundles(Bundle bundle, boolean recursive) throws BundleException {
    return getRequiredBundles(bundle, recursive, new HashSet<Bundle>());
  }
  
  private static Set<URL> getClassPath(Bundle bundle, boolean recursive, boolean includeBundlePath, Set<URL> classPath) throws BundleException,
      IOException {
    if (includeBundlePath) {
      URL url = bundle.getResource("/");
      if (url != null) {
        classPath.add(jarURLToJarFileURL(FileLocator.resolve(url)));
      }
    }
    Collection<ManifestElement> classPathElements = getManifestElements(bundle, Constants.BUNDLE_CLASSPATH);
    for (ManifestElement prereq: classPathElements) {
      for (String value: prereq.getValueComponents()) {
        if (".".equals(value)) continue;
        URL entry = bundle.getEntry(value);
        if (entry == null) continue;
        classPath.add(jarURLToJarFileURL(FileLocator.resolve(entry)));
      }
    }
    if (recursive) {
      for (Bundle requiredBundle: getRequiredBundles(bundle, true)) {
        getClassPath(requiredBundle, false, true, classPath);
      }
    }
    return classPath;
  }
  
  public static Set<URL> getClassPath(Bundle bundle, boolean recursive) throws BundleException, IOException {
    return getClassPath(bundle, recursive, true, new LinkedHashSet<URL>());
  }
  
  /**
   * Gets the url to the jar-file used in a jar-url.
   *
   * Note: the inner path is ignored
   * @throws IOException
   */
  public static URL jarURLToJarFileURL(URL url) throws IOException {
    if (!"jar".equals(url.getProtocol())) return url;
    String spec = url.getFile();
    
    int separator = spec.indexOf("!/");
    if (separator == -1) {
      throw new IllegalArgumentException("no !/ found in url spec:" + spec);
    }
    
    try {
      return new URL(spec.substring(0, separator));
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException(e);
    }
  }
  
}
