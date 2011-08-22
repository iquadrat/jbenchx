package org.jbenchx.ui.eclipse.launch;

import java.util.*;

import org.eclipse.debug.ui.*;
import org.eclipse.debug.ui.sourcelookup.SourceLookupTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.*;

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
