package fr.inria.gforge.spoon.utils;

import spoon.OutputType;
import spoon.compiler.SpoonCompiler;
import spoon.processing.ProcessingManager;
import spoon.processing.Processor;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.factory.FactoryImpl;
import spoon.support.DefaultCoreFactory;
import spoon.support.QueueProcessingManager;
import spoon.support.StandardEnvironment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicolas on 18/11/2014.
 */
public class TestSpooner {
	final SpoonCompiler compiler;

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
		
		List<String> processorsNames = new ArrayList<String>();
		for (Class<? extends Processor> processor : processors) {
			processorsNames.add(processor.getName());
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
		compiler.setOutputDirectory(dest);

		compiler.generateProcessedSourceFiles(OutputType.COMPILATION_UNITS);

		return this;
	}

	public boolean compile() {
		// spoon generated files
		File target = compiler.getOutputDirectory();
		return org.eclipse.jdt.internal.compiler.batch.Main.compile("-1.7 " + target);
	}

	public Class getSpoonedClass(String classname) throws ClassNotFoundException {
		File target = compiler.getOutputDirectory();

		ClassLoader classLoader = new SpoonClassLoader(getClass().getClassLoader(), target);
		return classLoader.loadClass(classname);
	}

}
