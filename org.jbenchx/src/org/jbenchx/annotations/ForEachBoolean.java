/*
 * Created on 09.10.2011
 *
 */
package org.jbenchx.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ForEachBoolean {

  boolean[] value();

}
