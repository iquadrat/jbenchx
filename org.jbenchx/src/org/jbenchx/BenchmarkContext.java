package org.jbenchx;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Pattern;

import javax.annotation.CheckForNull;

import org.jbenchx.Benchmark.Parameters;
import org.jbenchx.Benchmark.Parameters.Builder;
import org.jbenchx.annotations.Bench;
import org.jbenchx.annotations.MemoryBench;
import org.jbenchx.monitor.IProgressMonitor;
import org.jbenchx.result.BenchmarkFailure;
import org.jbenchx.result.IBenchmarkResult;
import org.jbenchx.result.ITaskResult;
import org.jbenchx.run.IBenchmarkTask;
import org.jbenchx.util.ClassUtil;
import org.jbenchx.util.StringUtil;
import org.jbenchx.util.SystemBenchmark;
import org.jbenchx.util.TimeUtil;
import org.jbenchx.vm.SystemInfo;

// TODO create builder
public class BenchmarkContext implements IBenchmarkContext {
  
  public static Benchmark.Parameters getDefaultParameters() {
     return Benchmark.Parameters.newBuilder()
         .setTargetTimeNs(250 * TimeUtil.MS)
         .setMinRunCount(10)
         .setMaxRunCount(100)
         .setMinSampleCount(8)
         .setMaxDeviation(0.05)
         .setMaxRestartCount(15)
         .build();
  }
  
  @CheckForNull
  public static Benchmark.Parameters getParamsFrom(Method method) {
    List<Bench> annotations = ClassUtil.findMethodAnnotations(method, Bench.class);
    List<MemoryBench> memoryAnnotations = ClassUtil.findMethodAnnotations(method, MemoryBench.class);
    if (annotations.isEmpty() && memoryAnnotations.isEmpty()) {
      return null;
    }
    ListIterator<Bench> iterator = annotations.listIterator(annotations.size());
    Benchmark.Parameters.Builder builder = Benchmark.Parameters.newBuilder();
    while (iterator.hasPrevious()) {
      builder.mergeFrom(getParamsFrom(iterator.previous()));
    }
    if (!memoryAnnotations.isEmpty()) {
      builder.setMeasureMemory(true);
    }
    return builder.build();
  }
  
  private static Benchmark.Parameters getParamsFrom(Bench annotation) {
    Builder builder = Benchmark.Parameters.newBuilder();
    if (annotation.targetTimeNs() != -1) {
      builder.setTargetTimeNs(annotation.targetTimeNs());
    }
    if (annotation.minRunCount() != -1) {
      builder.setMinRunCount(annotation.minRunCount());
    }
    if (annotation.maxRunCount() != -1) {
      builder.setMaxRunCount(annotation.maxRunCount());
    }
    if (annotation.minSampleCount() != -1) {
      builder.setMinSampleCount(annotation.minSampleCount());
    }
    if (annotation.maxDeviation() != -1) {
      builder.setMaxDeviation(annotation.maxDeviation());
    }
    return builder.build();
  }

	public static final List<Pattern> RUN_ALL;
	static {
		RUN_ALL = new ArrayList<>(1);
		RUN_ALL.add(StringUtil.wildCardToRegexpPattern("*"));
	}

	public static final String VERSION = "0.3.1";

	private final IProgressMonitor fProgressMonitor;

	private final Benchmark.Parameters fDefaultParams;

	@CheckForNull
	private final SystemInfo fSystemInfo;

	private final List<Pattern> fTagPatterns;

	public BenchmarkContext(IProgressMonitor progressMonitor, @CheckForNull SystemInfo systemInfo) {
		this(progressMonitor, systemInfo, RUN_ALL);
	}

	public BenchmarkContext(IProgressMonitor progressMonitor, @CheckForNull SystemInfo systemInfo,
			List<Pattern> patterns) {
		this(progressMonitor, systemInfo, patterns, getDefaultParameters());
	}

	public BenchmarkContext(IProgressMonitor progressMonitor, @CheckForNull SystemInfo systemInfo,
			List<Pattern> patterns, Benchmark.Parameters defaultParams) {
		fProgressMonitor = progressMonitor;
		fSystemInfo = systemInfo;
		fDefaultParams = defaultParams;
		fTagPatterns = new ArrayList<>(patterns);
	}

	@Override
	public Benchmark.Parameters getDefaultParams() {
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
		return create(progressMonitor, "*");
	}

	public static IBenchmarkContext create(IProgressMonitor progressMonitor, String tagPattern) {
		return create(progressMonitor, toPattern(Arrays.asList(tagPattern)));
	}

	public static List<Pattern> toPattern(Collection<String> strings) {
		List<Pattern> patterns = new ArrayList<>(strings.size());
		for (String tagPattern : strings) {
			patterns.add(StringUtil.wildCardToRegexpPattern(tagPattern));
		}
		return patterns;
	}

	public static IBenchmarkContext create(IProgressMonitor progressMonitor, List<Pattern> tagPatterns) {
    Parameters systemParams = getDefaultParameters().toBuilder().setTargetTimeNs(50 * TimeUtil.MS).build();
    IBenchmarkContext systemBenchmarkContext = 
            new BenchmarkContext(IProgressMonitor.DUMMY, null, RUN_ALL, systemParams);
    
		BenchmarkRunner runner = new BenchmarkRunner();
		runner.add(SystemBenchmark.class);

		IBenchmarkResult result = runner.run(systemBenchmarkContext);
		List<BenchmarkFailure> errors = result.getGeneralErrors();
		if (!errors.isEmpty()) {
			throw errors.get(0);
		}

		IBenchmarkTask emptyTask = result.findTask(SystemBenchmark.class.getSimpleName() + ".empty");
		ITaskResult emptyResult = result.getResult(emptyTask);

		IBenchmarkTask calculateTask = result.findTask(SystemBenchmark.class.getSimpleName() + ".calculate(1000)");
		ITaskResult calculateResult = result.getResult(calculateTask);

		IBenchmarkTask memoryTask = result.findTask(SystemBenchmark.class.getSimpleName() + ".memory(8388608)");
		ITaskResult memoryResult = result.getResult(memoryTask);

		double systemBenchMark = calculateResult.getEstimatedBenchmark() + memoryResult.getEstimatedBenchmark();
		long timerGranularity = TimeUtil.estimateTimerGranularity(new Timer());
		long methodInvoke = Math.round(emptyResult.getEstimatedBenchmark());

		SystemInfo systemInfo = SystemInfo.create(timerGranularity, methodInvoke, systemBenchMark);
		progressMonitor.systemInfo(systemInfo);

		return new BenchmarkContext(progressMonitor, systemInfo, tagPatterns);
	}

	@Override
	public ClassLoader getClassLoader() {
		return ClassUtil.createClassLoader();
	}

	@Override
	public List<Pattern> getTagPatterns() {
		return fTagPatterns;
	}

}
