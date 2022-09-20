package fr.inria.gforge.spoon.transformation.bound.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Declaratively add min and max value on method parameters.
 *
 * Example: void m(@Bound(min = 2d, max = 8d) int a)
 *
 */
@Target(ElementType.PARAMETER)
public @interface Bound {
	/** the minimun acceptable value of an integer parameter */
	double min();

	/** the maximum acceptable value of an integer parameter */
	double max();
}
