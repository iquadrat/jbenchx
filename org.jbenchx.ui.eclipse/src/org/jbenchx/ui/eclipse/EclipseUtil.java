package org.jbenchx.ui.eclipse;

import java.util.*;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.*;

import edu.umd.cs.findbugs.annotations.CheckForNull;


public class EclipseUtil {
  
  private EclipseUtil() {}
  
  @CheckForNull
  public static <T> T adapt(Object object, Class<T> clazz) {
    if (clazz.isInstance(object)) {
      return clazz.cast(object);
    }
    
    if (object instanceof IAdaptable) {
      IAdaptable adaptable = (IAdaptable)object;
      return clazz.cast(adaptable.getAdapter(clazz));
    }
    
    return null;
  }
  
  public static List<?> extractElements(ISelection selection) {
    if (!(selection instanceof IStructuredSelection)) return Collections.emptyList();
    return ((IStructuredSelection)selection).toList();
  }
  
}
