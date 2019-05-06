package fr.inria.gforge.spoon.assertgenerator;

import fr.inria.gforge.spoon.assertgenerator.workflow.Analyzer;
import fr.inria.gforge.spoon.assertgenerator.workflow.AssertionAdder;
import fr.inria.gforge.spoon.assertgenerator.workflow.Collector;
import org.junit.Test;
import spoon.Launcher;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Created by Benjamin DANGLOT
 * benjamin.danglot@inria.fr
 * on 20/06/17
 */
public class AssertionGenerationTest {

	@Test
	public void test() {
		Launcher launcher = new Launcher();
		launcher.getEnvironment().setAutoImports(true);
		launcher.getEnvironment().setLevel(Level.ALL.toString());
		launcher.addInputResource("src/test/resources/project/src/main/java/");
		launcher.addInputResource("src/test/resources/project/src/test/java/");
		launcher.addInputResource("src/main/java/fr/inria/gforge/spoon/assertgenerator/Logger.java");
		launcher.getEnvironment().setSourceClasspath(getPathToJunit());
		launcher.buildModel();

		final Analyzer analyzer = new Analyzer();
		final Collector collector = new Collector(launcher.getFactory());
		final AssertionAdder assertionAdder = new AssertionAdder(launcher.getFactory());

		launcher.getFactory().Class().getAll().forEach(ctClass -> {
			// Analyze
			Map<CtMethod, List<CtLocalVariable>> localVariablesPerTestMethod = analyzer.analyze(ctClass);
			localVariablesPerTestMethod.keySet().stream().forEach(key -> System.out.println("{"+ key.getParent(CtClass.class).getSimpleName() + "#" + key.getSimpleName() + "=["+ localVariablesPerTestMethod.get(key) +"]"));
			if (!localVariablesPerTestMethod.isEmpty()) {
				// Collect
				localVariablesPerTestMethod.keySet().forEach(ctMethod -> collector.collect(launcher, ctMethod, localVariablesPerTestMethod.get(ctMethod)));
				// Generate
				localVariablesPerTestMethod.keySet().forEach(ctMethod -> assertionAdder.addAssertion(ctMethod, localVariablesPerTestMethod.get(ctMethod)));
			}
		});
	}

	private static String[] getPathToJunit() {
		File file = new File(".cp");
		try {
			final String cmd;
			if (System.getProperty("os.name").startsWith("Windows")) {
				cmd = "cmd /C mvn dependency:build-classpath -Dmdep.outputFile=.cp";
			} else {
				cmd = "mvn dependency:build-classpath -Dmdep.outputFile=.cp";
			}
			Process p = Runtime.getRuntime().exec(cmd);
			new Thread() {
				@Override
				public void run() {
					while (p.isAlive()) {
						try {
							System.out.print((char) p.getInputStream().read());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
			p.waitFor();
			final String classpath;
			try (BufferedReader buffer = new BufferedReader(new FileReader(file))) {
				classpath = buffer.lines().collect(Collectors.joining(System.getProperty("path.separator")));
			}
			file.delete();
			return classpath.split(System.getProperty("path.separator"));
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException(e);
		}

	}


}
