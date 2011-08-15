package org.jbenchx.ui.eclipse;

import org.eclipse.core.expressions.*;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.jbenchx.*;
import org.jbenchx.annotations.*;

public class JBenchXPropertyTester extends PropertyTester {
  
  public JBenchXPropertyTester() {}
  
  @Override
  public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
    IAdaptable adaptable = (IAdaptable)receiver;
    IResource resource = (IResource)adaptable.getAdapter(IResource.class);
    if (resource == null) return false;
    
    if ("containsBench".equals(property)) {
      return containsBench(resource);
    }
    
    return false;
  }
  
  private boolean containsBench(IResource resource) {
    IJavaElement javaElement = JavaCore.create(resource);
    if (javaElement instanceof ITypeRoot) {
      return containsBench((ITypeRoot)javaElement);
    }
    return false;
  }
  
  private boolean containsBench(ITypeRoot typeRoot) {
    IType primaryType = typeRoot.findPrimaryType();
    if (primaryType == null || !isSubclassOfBenchmark(primaryType)) return false;
    
    try {
      for (IMethod method: primaryType.getMethods()) {
        
        for (IAnnotation annotation: method.getAnnotations()) {
          String name = annotation.getElementName();
          if (Bench.class.getSimpleName().equals(name) || Bench.class.getName().equals(name)) {
            return true;
          }
        }
        
      }
    } catch (JavaModelException e) {
    }
    
    return false;
  }
  
  private boolean isSubclassOfBenchmark(IType primaryType) {
    try {
      IRegion region = JavaCore.newRegion();
      region.add(primaryType);
      ITypeHierarchy typeHierarchy = JavaCore.newTypeHierarchy(region, null, null);
      for (IType type: typeHierarchy.getSupertypes(primaryType)) {
        if (Benchmark.class.getName().equals(type.getFullyQualifiedName())) return true;
      }
    } catch (JavaModelException e) {
    }
    return false;
  }
  
}
