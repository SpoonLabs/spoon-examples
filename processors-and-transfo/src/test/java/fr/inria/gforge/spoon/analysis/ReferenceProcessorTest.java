package fr.inria.gforge.spoon.analysis;

import org.junit.Test;
import spoon.Launcher;
import spoon.processing.ProcessingManager;
import spoon.reflect.factory.Factory;
import spoon.support.QueueProcessingManager;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ReferenceProcessorTest {
	@Test
	public void testReferenceProcessor() throws Exception {
		final String[] args = {
				"-i", "src/test/resources/src/",
				"-o", "target/spooned/",
		};

		final Launcher launcher = new Launcher();
		launcher.setArgs(args);
		launcher.run();

		final Factory factory = launcher.getFactory();
		final ProcessingManager processingManager = new QueueProcessingManager(factory);
		final ReferenceProcessor processor = new ReferenceProcessor();
		processingManager.addProcessor(processor);
		processingManager.process(factory.Package().getRootPackage());

		// implicit constructor is also counted
		assertThat(processor.circularPathes.size(), is(2));
	}
}
