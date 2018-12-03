package fr.inria.gforge.spoon;

import org.junit.Test;

import spoon.Launcher;

public class HelloWorldProcessorTest {
	@Test
	public void testName() throws Exception {
		Launcher l = new Launcher();
		l.getEnvironment().setNoClasspath(true);
		l.addInputResource("src/main/java");
		l.addProcessor(new HelloWorldProcessor());
		l.run();
	}
}