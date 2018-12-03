package fr.inria.gforge.spoon;

import spoon.processing.AbstractManualProcessor;

/** outputs hello world by self compile-time introspection */
public class HelloWorldProcessor extends AbstractManualProcessor {

	@Override
	public void process() {
		System.out.println(getFactory().Class().get(getClass().getCanonicalName()).getField("msg").getDefaultExpression());
	}

	String msg = "hello world";
	
}