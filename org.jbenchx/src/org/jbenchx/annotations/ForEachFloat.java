/*
 * Created on 09.10.2011
 *
 */
package org.jbenchx.annotations;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ForEachFloat {

  float[] value();

}
