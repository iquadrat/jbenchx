/*
 * Created on 09.10.2011
 *
 */
package org.jbenchx.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * FIMXE use to divide the result time
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface DivideBy {
  
}
