package org.jbenchx.ui.eclipse;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.jbenchx.annotations.Bench;

public class JBenchXPropertyTester extends PropertyTester {

  public JBenchXPropertyTester() {}

  @Override
  public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
    IAdaptable adaptable = (IAdaptable)receiver;
    IResource resource = (IResource)adaptable.getAdapter(IResource.class);
    if (resource == null) {
      return false;
    }

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
    try {
      IType primaryType = typeRoot.findPrimaryType();
      if (primaryType == null || Flags.isAbstract(primaryType.getFlags())) {
        return false;
      }
      
      if (containsBenchMethod(primaryType)) return true;
      
      for(IType superclass: primaryType.newSupertypeHierarchy(new NullProgressMonitor()).getAllSuperclasses(primaryType)) {
        if (containsBenchMethod(superclass)) return true;
      }
      
    } catch (JavaModelException e) {
    }

    return false;
  }

  private boolean containsBenchMethod(IType primaryType) throws JavaModelException {
    for (IMethod method: primaryType.getMethods()) {
      for (IAnnotation annotation: method.getAnnotations()) {
        String name = annotation.getElementName();
        if (Bench.class.getSimpleName().equals(name) || Bench.class.getName().equals(name)) {
          return true;
        }
      }
    }
    return false;
  }

}
