package fr.inria.gforge.spoon.transformation;

import org.eclipse.jdt.internal.compiler.batch.Main;
import org.junit.Test;
import spoon.Launcher;

import java.io.PrintWriter;

import static org.junit.Assert.assertTrue;

public class NotNullCheckAdderProcessorTest {
	@Test
	public void testCompileSourceCodeAfterProcessSourceCodeWithNotNullCheckAdderProcessor() throws Exception {
		final String[] args = {
				"-i", "src/test/resources/src/",
				"-o", "target/spooned/",
				"-p", "fr.inria.gforge.spoon.transformation.NotNullCheckAdderProcessor"
		};

		final Launcher launcher = new Launcher();
		launcher.setArgs(args);
		launcher.run();

		assertTrue(Main.compile(Main.tokenize("-1.6 target/spooned/"), new PrintWriter(System.out), new PrintWriter(System.err), null));
	}
}
