package org.jbenchx;

import java.util.List;

import javax.annotation.CheckForNull;

import org.jbenchx.monitor.*;
import org.jbenchx.result.*;
import org.jbenchx.run.*;
import org.jbenchx.util.*;
import org.jbenchx.vm.*;

public class BenchmarkContext implements IBenchmarkContext {
  
  public static final String        VERSION = "0.3.0";
  
  private final IProgressMonitor    fProgressMonitor;
  
  private final BenchmarkParameters fDefaultParams;
  
  @CheckForNull
  private final SystemInfo          fSystemInfo;
  
  public BenchmarkContext(IProgressMonitor progressMonitor, @CheckForNull SystemInfo systemInfo) {
    this(progressMonitor, systemInfo, BenchmarkParameters.getDefaults());
  }
  
  public BenchmarkContext(IProgressMonitor progressMonitor, @CheckForNull SystemInfo systemInfo, BenchmarkParameters defaultParams) {
    fProgressMonitor = progressMonitor;
    fSystemInfo = systemInfo;
    fDefaultParams = defaultParams;
  }
  
  @Override
  public BenchmarkParameters getDefaultParams() {
    return fDefaultParams;
  }
  
  @Override
  public IProgressMonitor getProgressMonitor() {
    return fProgressMonitor;
  }
  
  @Override
  public SystemInfo getSystemInfo() {
    return fSystemInfo;
  }
  
  @Override
  public String getVersion() {
    return VERSION;
  }
  
  public static IBenchmarkContext create(IProgressMonitor progressMonitor) {
    IBenchmarkContext systemBenchmarkContext = new BenchmarkContext(IProgressMonitor.DUMMY, null);
    systemBenchmarkContext.getDefaultParams().setTargetTimeNs(50 * TimeUtil.MS);
    BenchmarkRunner runner = new BenchmarkRunner();
    runner.add(SystemBenchmark.class);
    
    IBenchmarkResult result = runner.run(systemBenchmarkContext);
    List<BenchmarkFailure> errors = result.getGeneralErrors();
    if (!errors.isEmpty()) {
      throw errors.get(0);
    }
    
    IBenchmarkTask emptyTask = result.findTask(SystemBenchmark.class.getSimpleName() + ".empty");
    ITaskResult emptyResult = result.getResult(emptyTask);
    
    IBenchmarkTask calculateTask = result.findTask(SystemBenchmark.class.getSimpleName() + ".calculate");
    ITaskResult calculateResult = result.getResult(calculateTask);
    
    IBenchmarkTask memoryTask = result.findTask(SystemBenchmark.class.getSimpleName() + ".memory");
    ITaskResult memoryResult = result.getResult(memoryTask);
    
    double systemBenchMark = calculateResult.getEstimatedBenchmark() + memoryResult.getEstimatedBenchmark();
    long timerGranularity = TimeUtil.estimateTimerGranularity(new Timer());
    long methodInvoke = Math.round(emptyResult.getEstimatedBenchmark());
    
    SystemInfo systemInfo = SystemInfo.create(timerGranularity, methodInvoke, systemBenchMark);
    progressMonitor.systemInfo(systemInfo);
    return new BenchmarkContext(progressMonitor, systemInfo);
  }

  @Override
  public ClassLoader getClassLoader() {
    return ClassUtil.createClassLoader(); 
    //return Thread.currentThread().getContextClassLoader();
  }
  
}
