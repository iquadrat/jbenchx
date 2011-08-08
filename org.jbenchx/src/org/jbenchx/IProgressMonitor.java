package org.jbenchx;

public interface IProgressMonitor {
  
  public static final IProgressMonitor DUMMY = new Stub();
  
  public void beginTasks(int count);
  
  public void worked(int count);
  
  public void endTasks();
  
  public class Stub implements IProgressMonitor {
    
    @Override
    public void beginTasks(int count) {

    }
    
    @Override
    public void worked(int count) {

    }
    
    @Override
    public void endTasks() {

    }
    
  }
  
}
