package fr.inria.gforge.spoon.utils;

import org.eclipse.jdt.internal.compiler.batch.Main;
import spoon.OutputType;
import spoon.SpoonModelBuilder;
import spoon.processing.ProcessingManager;
import spoon.processing.Processor;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.factory.FactoryImpl;
import spoon.support.DefaultCoreFactory;
import spoon.support.QueueProcessingManager;
import spoon.support.StandardEnvironment;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicolas on 18/11/2014.
 */
public class TestSpooner {
	final SpoonModelBuilder compiler;

	public TestSpooner() throws Exception {
		compiler = new TestSpoonCompiler(
				new FactoryImpl(new DefaultCoreFactory(), new StandardEnvironment()));
	}

	public TestSpooner addSource(File... files) throws Exception {
		for (File file : files) {
			compiler.addInputSource(file);
		}
		return this;
	}

	public TestSpooner addTemplate(File... files) throws Exception {
		for (File file : files) {
			compiler.addTemplateSource(file);
		}
		return this;
	}

	spoon.reflect.factory.Factory getFactory() {
		return compiler.getFactory();
	}

	public TestSpooner process(CtElement element, Class<? extends Processor>... processors) throws Exception {
		// Build spoon model
		compiler.build();

		ProcessingManager processing = new QueueProcessingManager(compiler.getFactory());

		for (Class<? extends Processor> processorClz : processors) {
			processing.addProcessor(processorClz.getName());
		}
		processing.process(element);

		return this;
	}

	public TestSpooner process(Class<? extends Processor>... processors) throws Exception {
		// Build spoon model
		compiler.build();
		
		List<Processor<?>> processorsNames = new ArrayList<>();
		for (Class<? extends Processor> processor : processors) {
			processorsNames.add(processor.newInstance());
		}
		compiler.process(processorsNames);

		return this;
	}

	public TestSpooner print(File dest) throws Exception {
		dest.mkdirs();
		for (File file : dest.listFiles()) {
			file.delete();
		}

		//   compiler.getFactory().getEnvironment().setPreserveLineNumbers(true);
		compiler.getFactory().getEnvironment().setSourceOutputDirectory(dest);

		compiler.generateProcessedSourceFiles(OutputType.COMPILATION_UNITS);
		return this;
	}

	public boolean compile() {
		File target = compiler.getSourceOutputDirectory();
		final boolean compile = Main.compile(new String[]{"-1.7 " , "-proc:none", target.toString()}, new PrintWriter(System.out), new PrintWriter(System.out),null);
		return compile;
	}

	public Class getSpoonedClass(String classname) throws ClassNotFoundException {
		File target = compiler.getSourceOutputDirectory();

		ClassLoader classLoader = new SpoonClassLoader(getClass().getClassLoader(), target);
		return classLoader.loadClass(classname);
	}

}
