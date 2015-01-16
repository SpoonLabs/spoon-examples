package fr.inria.gforge.spoon.analysis.processing;

import org.junit.Test;
import spoon.Launcher;
import spoon.processing.ProcessingManager;
import spoon.reflect.factory.Factory;
import spoon.support.QueueProcessingManager;
import spoon.support.compiler.FileSystemFolder;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class CatchProcessorTest {
	@Test
	public void testCatchProcessor() throws Exception {
		final Launcher launcher = new Launcher();
		launcher.addInputResource(new FileSystemFolder(new File("src/main/java/fr/inria/gforge/spoon/analysis/src/")));
		launcher.run();

		final Factory factory = launcher.getFactory();
		final ProcessingManager processingManager = new QueueProcessingManager(factory);
		final CatchProcessor processor = new CatchProcessor();
		processingManager.addProcessor(processor);
		processingManager.process(factory.Class().getAll());

		assertEquals(2, processor.emptyCatchs.size());
	}
}
