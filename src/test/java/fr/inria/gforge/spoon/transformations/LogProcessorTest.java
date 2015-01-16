package fr.inria.gforge.spoon.transformations;

import org.eclipse.jdt.internal.compiler.batch.Main;
import org.junit.Test;
import spoon.Launcher;

import java.io.PrintWriter;

import static org.junit.Assert.assertTrue;

public class LogProcessorTest {
	@Test
	public void testCompileSourceCodeAfterProcessSourceCodeWithNotNullCheckAdderProcessor() throws Exception {
		final String[] args = {
				"-i", "src/test/java/fr/inria/gforge/spoon/src/",
				"-o", "target/spooned/",
				"-p", "fr.inria.gforge.spoon.transformations.LogProcessor"
		};

		final Launcher launcher = new Launcher();
		launcher.setArgs(args);
		launcher.run();

		assertTrue(Main.compile(Main.tokenize("-1.6 target/spooned/"), new PrintWriter(System.out), new PrintWriter(System.err), null));
	}
}
