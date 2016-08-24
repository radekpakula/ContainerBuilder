package pl.containerbuilder;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;

/**
 * 
 * @author radek
 *
 */
@Retention(RUNTIME)
@Repeatable(Cols.class)
public @interface Col {

	/**
	 * Display name of column
	 * @return
	 */
	String value() default ""; 

	/**
	 * order column
	 * @return
	 */
	int position() default 0;

	/**
	 * visible col on container
	 * @return
	 */
	boolean visible() default true;
	
	String schema() default "";
}
