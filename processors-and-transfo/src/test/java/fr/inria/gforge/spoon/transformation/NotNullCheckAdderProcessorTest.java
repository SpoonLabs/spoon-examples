package fr.inria.gforge.spoon.transformation;

import org.eclipse.jdt.internal.compiler.batch.Main;
import org.junit.Test;
import spoon.Launcher;
import spoon.reflect.declaration.CtClass;

import java.io.PrintWriter;

import static org.junit.Assert.assertTrue;

public class NotNullCheckAdderProcessorTest {
	@Test
	public void testCompileSourceCodeAfterProcessSourceCodeWithNotNullCheckAdderProcessor() throws Exception {
		final String[] args = {
				"-i", "src/test/resources/src/",
				"-o", "target/spooned/",
				"-p", "fr.inria.gforge.spoon.transformation.NotNullCheckAdderProcessor",
				"--compile"
		};

		final Launcher launcher = new Launcher();
		launcher.setArgs(args);
		launcher.run();
	}
}
