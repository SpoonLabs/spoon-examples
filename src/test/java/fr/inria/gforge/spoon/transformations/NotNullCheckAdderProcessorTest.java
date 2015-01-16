package fr.inria.gforge.spoon.transformations;

import org.eclipse.jdt.internal.compiler.batch.Main;
import org.junit.Test;
import spoon.Launcher;
import spoon.support.compiler.FileSystemFolder;

import java.io.File;
import java.io.PrintWriter;

import static org.junit.Assert.assertTrue;

public class NotNullCheckAdderProcessorTest {
	@Test
	public void testCompileSourceCodeAfterProcessSourceCodeWithNotNullCheckAdderProcessor() throws Exception {
		final Launcher launcher = new Launcher();
		launcher.addInputResource(new FileSystemFolder(new File("src/main/java/fr/inria/gforge/spoon/analysis/src/")));
		launcher.addProcessor("fr.inria.gforge.spoon.transformations.NotNullCheckAdderProcessor");
		launcher.run();

		assertTrue(Main.compile(Main.tokenize("-1.6 spooned/"), new PrintWriter(System.out), new PrintWriter(System.err), null));
	}
}
