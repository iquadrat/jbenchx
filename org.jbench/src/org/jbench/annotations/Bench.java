package org.jbench.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Bench {
  
  int minRunCount() default 4;
  
  int maxRunCount() default 100;
  
  double maxDeviation() default 0.1;
  
  int divisor() default 1;

  int minSampleCount() default 8;
  
}
