package fr.inria.gforge.spoon.assertgenerator;

import fr.inria.gforge.spoon.assertgenerator.workflow.Analyzer;
import fr.inria.gforge.spoon.assertgenerator.workflow.AssertionAdder;
import fr.inria.gforge.spoon.assertgenerator.workflow.Collector;
import org.junit.Test;
import spoon.Launcher;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtVariableAccess;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtExecutableReference;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
		Process p = null;
		try {
			p = Runtime.getRuntime().exec("mvn dependency:build-classpath -Dmdep.outputFile=.cp");
			Process finalP = p;
			new Thread() {
				@Override
				public void run() {
					while (finalP.isAlive()) {
						try {
							System.out.print((char) finalP.getInputStream().read());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
			p.waitFor();
			BufferedReader buffer = new BufferedReader(new FileReader(".cp"));
			final String classpath = buffer.lines().collect(Collectors.joining(System.getProperty("path.separator")));
			buffer.close();
			return classpath.split(System.getProperty("path.separator"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}


}
