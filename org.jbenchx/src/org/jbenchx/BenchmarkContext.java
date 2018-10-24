package org.jbenchx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.CheckForNull;

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

	public static final List<Pattern> RUN_ALL;
	static {
		RUN_ALL = new ArrayList<>(1);
		RUN_ALL.add(StringUtil.wildCardToRegexpPattern("*"));
	}

	public static final String VERSION = "0.3.0";

	private final IProgressMonitor fProgressMonitor;

	private final BenchmarkParameters fDefaultParams;

	@CheckForNull
	private final SystemInfo fSystemInfo;

	private final List<Pattern> fTagPatterns;

	public BenchmarkContext(IProgressMonitor progressMonitor, @CheckForNull SystemInfo systemInfo) {
		this(progressMonitor, systemInfo, RUN_ALL);
	}

	public BenchmarkContext(IProgressMonitor progressMonitor, @CheckForNull SystemInfo systemInfo,
			List<Pattern> patterns) {
		this(progressMonitor, systemInfo, patterns, BenchmarkParameters.getDefaults());
	}

	public BenchmarkContext(IProgressMonitor progressMonitor, @CheckForNull SystemInfo systemInfo,
			List<Pattern> patterns, BenchmarkParameters defaultParams) {
		fProgressMonitor = progressMonitor;
		fSystemInfo = systemInfo;
		fDefaultParams = defaultParams;
		fTagPatterns = new ArrayList<>(patterns);
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
		IBenchmarkContext systemBenchmarkContext = new BenchmarkContext(IProgressMonitor.DUMMY, null, RUN_ALL);
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

		return new BenchmarkContext(progressMonitor, systemInfo, tagPatterns);
	}

	@Override
	public ClassLoader getClassLoader() {
		return ClassUtil.createClassLoader();
		// return Thread.currentThread().getContextClassLoader();
	}

	@Override
	public List<Pattern> getTagPatterns() {
		return fTagPatterns;
	}

}
