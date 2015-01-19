package org.jbenchx.ui.eclipse;

import java.util.Collections;
import java.util.List;

import javax.annotation.CheckForNull;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;


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
