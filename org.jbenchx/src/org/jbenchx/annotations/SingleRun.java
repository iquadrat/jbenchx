package org.jbenchx.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * If a benchmark method gets annotated with the {@link SingleRun} annotation
 * the benchmark will be invoked only once during a benchmark run. Normally, the
 * number of invocations will be calculated dynamically such that the benchmark
 * runtime approximately matches the given target time.
 * <p>
 * If you use this annotation you must ensure yourself that the benchmark runs
 * a meaningful time.
 *
 * @author micha
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SingleRun {

}
