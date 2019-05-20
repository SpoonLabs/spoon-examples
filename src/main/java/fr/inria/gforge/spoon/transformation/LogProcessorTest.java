package fr.inria.gforge.spoon.transformation;

import org.junit.Test;
import spoon.Launcher;


public class LogProcessorTest {
	@Test
	public void testCompileSourceCodeAfterProcessSourceCodeWithNotNullCheckAdderProcessor() throws Exception {
		final String[] args = {
				"-i", "src/test/resources/src/",
				"-o", "target/spooned/",
				"-p", "fr.inria.gforge.spoon.transformation.autologging.LogProcessor",
				"--compile"
		};

		final Launcher launcher = new Launcher();
		launcher.setArgs(args);
		launcher.run();
	}
}
