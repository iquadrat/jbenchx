package org.jbenchx.annotations;

import java.lang.annotation.*;

/**
 * Benchmark methods must be annotated with the {@link Bench} annotation.
 * Each method which has this annotation or one of its overridden method
 * of super class contains this annotation is considered a benchmark method
 * of the class.
 * <p>
 * The {@link Bench} also allows to specify certain parameters for the benchmark.
 * If some option is not set, the option's value is taken from the overridden method of
 * the super-class or if the method is not an overriding method, the default
 * value for the option is chosen.
 * 
 * @author micha
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Bench {

  /**
   * Minimum number of runs that are performed for benchmarking the method.
   */
  int minRunCount() default -1;
  
  /**
   * Maximum number of runs that are performed for benchmarking the method.
   */
  int maxRunCount() default -1;

  /**
   * Minimum number of valid samples that are needed.
   */
  int minSampleCount() default -1;

  /**
   * Maximum deviation from the minimal time allowed to consider a sample valid.
   */
  double maxDeviation() default -1;
  
  /**
   * Benchmark value divisor. The measured time is divided by this value.
   */
  int divisor() default -1;

  /**
   * Target time for a single run of the benchmark.
   */
  long targetTimeNs() default -1;
  
}
