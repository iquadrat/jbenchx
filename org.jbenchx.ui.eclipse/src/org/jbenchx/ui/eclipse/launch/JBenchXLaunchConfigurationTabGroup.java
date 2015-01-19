package org.jbenchx.ui.eclipse.launch;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.EnvironmentTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.debug.ui.RefreshTab;
import org.eclipse.debug.ui.sourcelookup.SourceLookupTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaArgumentsTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaClasspathTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaJRETab;

public class JBenchXLaunchConfigurationTabGroup extends AbstractLaunchConfigurationTabGroup {
  
  public JBenchXLaunchConfigurationTabGroup() {}
  
  @Override
  public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
    // TODO create own tab
    List<ILaunchConfigurationTab> tabs = new ArrayList<ILaunchConfigurationTab>();
    tabs.add(new JavaArgumentsTab());
    tabs.add(new JavaClasspathTab());
    tabs.add(new JavaJRETab());
    tabs.add(new SourceLookupTab());
    tabs.add(new CommonTab());
    tabs.add(new EnvironmentTab());
    tabs.add(new RefreshTab());
    setTabs(tabs.toArray(new ILaunchConfigurationTab[tabs.size()]));
  }
  
}
