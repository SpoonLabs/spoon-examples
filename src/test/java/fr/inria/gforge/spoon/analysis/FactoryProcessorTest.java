package fr.inria.gforge.spoon.analysis;

import org.junit.Test;
import spoon.Launcher;
import spoon.processing.ProcessingManager;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.factory.Factory;
import spoon.reflect.visitor.filter.NamedElementFilter;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.support.QueueProcessingManager;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FactoryProcessorTest {
	@Test
	public void testReferenceProcessor() throws Exception {
		final String[] args = {
				"-i", "src/test/resources/factory/",
				"-o", "target/spooned/"
		};

		final Launcher launcher = new Launcher();
		launcher.setArgs(args);
		launcher.run();

		final Factory factory = launcher.getFactory();
		final ProcessingManager processingManager = new QueueProcessingManager(factory);
		List<CtInterface> listFactoryItf = factory.getModel().getElements(new NamedElementFilter<>(CtInterface.class, "Factory"));
		assertThat(listFactoryItf.size(), is(1));

		final FactoryProcessor processor = new FactoryProcessor(listFactoryItf.get(0).getReference());
		processingManager.addProcessor(processor);

		List<CtConstructorCall> ctNewClasses = factory.getModel().getElements(new TypeFilter<>(CtConstructorCall.class));
		processingManager.process(ctNewClasses);

		// implicit constructor is also counted
		assertThat(processor.listWrongUses.size(), is(2));
	}
}
