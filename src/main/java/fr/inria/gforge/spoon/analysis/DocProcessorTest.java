package fr.inria.gforge.spoon.analysis;

import org.junit.Test;
import spoon.Launcher;
import spoon.processing.ProcessingManager;
import spoon.reflect.factory.Factory;
import spoon.support.QueueProcessingManager;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DocProcessorTest {
	@Test
	public void testDocProcessor() throws Exception {
		final String[] args = {
				"-i", "src/test/resources/src/DocTest.java",
				"-o", "target/spooned/",
				"-c" // It's mandatory here to enable comments
		};

		final Launcher launcher = new Launcher();
		launcher.setArgs(args);
		launcher.run();

		final Factory factory = launcher.getFactory();
		final ProcessingManager processingManager = new QueueProcessingManager(factory);
		final DocProcessor processor = new DocProcessor();
		processingManager.addProcessor(processor);
		processingManager.process(factory.Class().getAll());

		// implicit constructor is also counted
		assertThat(processor.undocumentedElements.size(), is(4));
	}
}
