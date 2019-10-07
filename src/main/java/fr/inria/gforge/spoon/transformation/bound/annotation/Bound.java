package fr.inria.gforge.spoon.transformation.bound.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
public @interface Bound {
	/** the minimun acceptable value of an integer parameter */
	double max();

	/** the maximum acceptable value of an integer parameter */
	double min();
}
