package fr.inria.gforge.spoon.assertgenerator.workflow;

import fr.inria.gforge.spoon.assertgenerator.Logger;
import fr.inria.gforge.spoon.assertgenerator.test.TestRunner;
import spoon.Launcher;
import spoon.SpoonModelBuilder;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtTypeAccess;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtExecutableReference;

import java.util.List;

import static fr.inria.gforge.spoon.assertgenerator.Util.getGetters;
import static fr.inria.gforge.spoon.assertgenerator.Util.getKey;
import static fr.inria.gforge.spoon.assertgenerator.Util.invok;
import java.net.MalformedURLException;

/**
 * Created by Benjamin DANGLOT
 * benjamin.danglot@inria.fr
 * on 22/06/17
 */
public class Collector {

	private Factory factory;

	public Collector(Factory factory) {
		this.factory = factory;
	}

	public void collect(Launcher launcher, CtMethod<?> testMethod, List<CtLocalVariable> localVariables) {
		CtClass testClass = testMethod.getParent(CtClass.class);
		testClass.removeMethod(testMethod);
		CtMethod<?> clone = testMethod.clone();
		instrument(clone, localVariables);
		testClass.addMethod(clone);
		System.out.println(clone);
		run(launcher, testClass, clone);
		testClass.removeMethod(clone);
		testClass.addMethod(testMethod);
	}

	public void run(Launcher launcher, CtClass testClass, CtMethod<?> clone) {
		String fullQualifiedName = testClass.getQualifiedName();
		String testMethodName = clone.getSimpleName();
		try {
			final SpoonModelBuilder compiler = launcher.createCompiler();
			compiler.compile(SpoonModelBuilder.InputType.CTTYPES);
			TestRunner.runTest(fullQualifiedName, testMethodName, new String[]{"spooned-classes"});
		} catch (ClassNotFoundException | MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public void instrument(CtMethod<?> testMethod, List<CtLocalVariable> ctLocalVariables) {
		ctLocalVariables.forEach(ctLocalVariable -> this.instrument(testMethod, ctLocalVariable));
	}

	void instrument(CtMethod testMethod, CtLocalVariable localVariable) {
		List<CtMethod> getters = getGetters(localVariable);
		getters.forEach(getter -> {
			CtInvocation invocationToGetter =
					invok(getter, localVariable);
			CtInvocation invocationToObserve =
					createObserve(getter, invocationToGetter);
			testMethod.getBody().insertEnd(invocationToObserve);
		});
	}

	CtInvocation createObserve(CtMethod getter, CtInvocation invocationToGetter) {
		CtTypeAccess accessToLogger =
				factory.createTypeAccess(factory.createCtTypeReference(Logger.class));
		CtExecutableReference refObserve = factory.Type().get(Logger.class)
				.getMethodsByName("observe").get(0).getReference();
		return factory.createInvocation(
				accessToLogger,
				refObserve,
				factory.createLiteral(getKey(getter)),
				invocationToGetter
		);
	}
}
