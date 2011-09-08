package org.jbenchx.ui.eclipse.classpath;

import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.ui.wizards.*;
import org.eclipse.jface.wizard.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

import edu.umd.cs.findbugs.annotations.*;

public class JBenchXContainerPage extends WizardPage implements IClasspathContainerPage, IExecutableExtension {

  @CheckForNull
  private IClasspathEntry fEntry = null;

  public JBenchXContainerPage() {
    super(JBenchXContainerPage.class.getSimpleName());
    setTitle("JBenchX Container");
  }

  @Override
  public void createControl(Composite parent) {
    Label label = new Label(parent, SWT.NONE);
    if (fEntry == null) {
      label.setText("Nothing to configure. Press 'Finish' to add new entry");
    } else {
      label.setText("Nothing to configure.");
      setPageComplete(false);
    }
    setControl(label);
  }

  @Override
  public boolean finish() {
    return true;
  }

  @Override
  public IClasspathEntry getSelection() {
    if (fEntry == null) {
      fEntry = JavaCore.newContainerEntry(new Path("org.jbenchx.JBENCHX_CONTAINER"));
    }
    return fEntry;
  }

  @Override
  public void setSelection(IClasspathEntry containerEntry) {
    fEntry = containerEntry;
  }

  @Override
  public void setInitializationData(IConfigurationElement config, String propertyName, Object data) {

  }

}
