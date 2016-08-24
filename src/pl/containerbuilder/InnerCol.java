package pl.containerbuilder;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;

/**
 * @author radek
 */
@Retention(RUNTIME)
@Repeatable(InnerCols.class)
public @interface InnerCol {
	
	String schema() default "";
}
