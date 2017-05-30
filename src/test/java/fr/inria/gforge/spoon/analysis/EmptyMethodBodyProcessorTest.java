package fr.inria.gforge.spoon.analysis;

import org.junit.Test;
import spoon.Launcher;
import spoon.processing.ProcessingManager;
import spoon.reflect.factory.Factory;
import spoon.support.QueueProcessingManager;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EmptyMethodBodyProcessorTest {
	@Test
	public void testEmptyMethodProcessor() throws Exception {
		final String[] args = {
				"-i", "src/test/resources/src/DocTest.java",
				"-o", "target/spooned/"
		};

		final Launcher launcher = new Launcher();
		launcher.setArgs(args);
		launcher.run();

		final Factory factory = launcher.getFactory();
		final ProcessingManager processingManager = new QueueProcessingManager(factory);
		final EmptyMethodBodyProcessor processor = new EmptyMethodBodyProcessor();
		processingManager.addProcessor(processor);
		processingManager.process(factory.Class().getAll());

		assertThat(processor.emptyMethods.size(), is(4));
	}
}
