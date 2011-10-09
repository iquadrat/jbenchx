package org.jbenchx;

import org.jbenchx.monitor.*;
import org.jbenchx.result.*;
import org.jbenchx.run.*;
import org.jbenchx.util.*;
import org.jbenchx.vm.*;

public class BenchmarkContext implements IBenchmarkContext {
  
  private final VmInfo              fVmInfo = VmInfo.getVmInfo();
  
  private final IProgressMonitor    fProgressMonitor;
  
  private final long                fTimerGranularity;
  
  private final long                fMethodInvokeTime;
  
  private final BenchmarkParameters fDefaultParams;
  
  public BenchmarkContext(IProgressMonitor progressMonitor, long timerGranularity, long methodInvokeTime) {
    this(progressMonitor, timerGranularity, methodInvokeTime, BenchmarkParameters.getDefaults());
  }
  
  public BenchmarkContext(IProgressMonitor progressMonitor, long timerGranularity, long methodInvokeTime, BenchmarkParameters defaultParams) {
    fProgressMonitor = progressMonitor;
    fTimerGranularity = timerGranularity;
    fMethodInvokeTime = methodInvokeTime;
    fDefaultParams = defaultParams;
  }
  
  @Override
  public VmInfo getVmInfo() {
    return fVmInfo;
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
  public long getTimerGranularity() {
    return fTimerGranularity;
  }
  
  @Override
  public long getMethodInvokeTime() {
    return fMethodInvokeTime;
  }
  
  public static IBenchmarkContext create(IProgressMonitor progressMonitor) {
    IBenchmarkContext systemBenchmarkContext = new BenchmarkContext(IProgressMonitor.DUMMY, -1, -1);
    systemBenchmarkContext.getDefaultParams().setTargetTimeNs(50 * TimeUtil.MS);
    BenchmarkRunner runner = new BenchmarkRunner();
    runner.add(SystemBenchmark.class);
    IBenchmarkResult result = runner.run(systemBenchmarkContext);
    
    IBenchmarkTask sqrtTask = result.findTask(SystemBenchmark.class.getSimpleName() + ".sqrt");
    IBenchmarkTask sqrt1Task = result.findTask(SystemBenchmark.class.getSimpleName() + ".sqrt1");
    if (sqrtTask == null || sqrt1Task == null) {
      throw new RuntimeException("Failed to run SystemBenchmark");
    }
    
    ITaskResult sqrtResult = result.getResult(sqrt1Task);
    ITaskResult sqrt1Result = result.getResult(sqrtTask);
    
//    System.out.println("sqrt="+sqrtResult.getEstimatedBenchmark()+" sqrt1="+sqrt1Result.getEstimatedBenchmark());
    long methodInvokeTime = Math.round(sqrtResult.getEstimatedBenchmark() - sqrt1Result.getEstimatedBenchmark());
    
//    long methodInvokeTime = -1;
//    try {
//      methodInvokeTime = SystemUtil.estimateMethodInvokeTime();
//    } catch (Exception e) {
//      // leave at -1
//    }
    
    long timerGranularity = TimeUtil.estimateTimerGranularity(new Timer());
    
//    System.out.println("Timer Granularity: " + TimeUtil.toString(timerGranularity) + ", invoke: " + TimeUtil.toString(methodInvokeTime));
//      BenchmarkContext systemContext = new BenchmarkContext(new ConsoleProgressMonitor());
//      BenchmarkTask task = new BenchmarkTask(SystemBenchmark.class.getSimpleName(), SystemBenchmark.class.getCanonicalName(), "estimateTimerGranularity", 
//          new BenchmarkParameters(0, 1, 5, 50, 5, 0.1));
//      BenchmarkResult result = new BenchmarkResult();
//      task.run(result, systemContext);
    return new BenchmarkContext(progressMonitor, timerGranularity, methodInvokeTime);
  }
  
}
