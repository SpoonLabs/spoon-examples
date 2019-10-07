package fr.inria.gforge.spoon.transformation.retry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Methods annotated with @RetryOnFailure will be re-executed three times
 *
 * Annotation inspired by JCabi retry annotation.
 * 
 * http://aspects.jcabi.com/annotation-retryonfailure.html
 *
 * Created by Nicolas Petitprez on 22/01/2015.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RetryOnFailure {
	/**
	 * How many times to retry.
	 */
	int attempts() default 3;

	/**
	 * Delay between attempts, in time units.
	 */
	long delay() default 50l;

	/**
	 * Shall it be fully verbose (show full exception trace) or just exception message?
	 */
	boolean verbose() default false;

}
