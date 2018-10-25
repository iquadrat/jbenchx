package org.jbenchx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.CheckForNull;

import org.jbenchx.Benchmark.Parameters;
import org.jbenchx.monitor.IProgressMonitor;
import org.jbenchx.result.ResultUtil;
import org.jbenchx.util.ClassUtil;
import org.jbenchx.util.StringUtil;
import org.jbenchx.util.SystemBenchmark;
import org.jbenchx.util.TimeUtil;
import org.jbenchx.vm.SystemInfo;

// TODO create builder
public class BenchmarkContext implements IBenchmarkContext {
  
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
		this(progressMonitor, systemInfo, patterns, Benchmarks.getDefaultParameters());
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
    Parameters systemParams = Benchmarks.getDefaultParameters().toBuilder().setTargetTimeNs(50 * TimeUtil.MS).build();
    IBenchmarkContext systemBenchmarkContext = 
            new BenchmarkContext(IProgressMonitor.DUMMY, null, RUN_ALL, systemParams);
    
		BenchmarkRunner runner = new BenchmarkRunner();
		runner.add(SystemBenchmark.class);

		Benchmark.Result result = runner.run(systemBenchmarkContext);
		List<Benchmark.Error> errors = result.getGeneralErrorList();
		if (!errors.isEmpty()) {
			throw new RuntimeException(errors.get(0).getMessage());
		}

    Benchmark.TaskResult emptyResult = ResultUtil.getTaskResult(result, SystemBenchmark.class.getSimpleName() + ".empty");
    Benchmark.TaskResult calculateResult = ResultUtil.getTaskResult(result, SystemBenchmark.class.getSimpleName() + ".calculate");
    Benchmark.TaskResult memoryResult = ResultUtil.getTaskResult(result, SystemBenchmark.class.getSimpleName() + ".memory");
    
    double systemBenchMark = 
        calculateResult.getEstimatedBenchmark() / SystemBenchmark.CALCULATE_ITERATIONS
        + memoryResult.getEstimatedBenchmark() / SystemBenchmark.MEM_BENCH_SIZE;
    long timerGranularity = TimeUtil.estimateTimerGranularity(new Timer());
		long methodInvoke = Math.round(emptyResult.getEstimatedBenchmark());

		SystemInfo systemInfo = SystemInfo.create(timerGranularity, methodInvoke, systemBenchMark);
		progressMonitor.systemInfo(systemInfo);

		return new BenchmarkContext(progressMonitor, systemInfo, tagPatterns);
	}

	@Override
	public ClassLoader createClassLoader() {
		return ClassUtil.createClassLoader();
	}

	@Override
	public List<Pattern> getTagPatterns() {
		return fTagPatterns;
	}

}
