package org.jbenchx.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Bench {
  
  int minRunCount() default -1;
  
  int maxRunCount() default -1;
  
  double maxDeviation() default -1;
  
  int divisor() default -1;
  
  int minSampleCount() default -1;

  long targetTimeNs() default -1;
  
}
