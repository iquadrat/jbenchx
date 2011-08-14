package org.jbenchx.ui.eclipse;

import org.eclipse.debug.ui.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.ui.*;

public class RunBenchmark implements ILaunchShortcut {
  
  @Override
  public void launch(ISelection selection, String mode) {
    System.out.println("launch in mode " + mode);
  }
  
  @Override
  public void launch(IEditorPart editor, String mode) {
    System.out.println("launch in mode " + mode);
  }
  
}
