package pl.containerbuilder;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

/**
 * @author radek
 */
@Retention(RUNTIME)
public @interface InnerCols {
	
	InnerCol[] value();
}
