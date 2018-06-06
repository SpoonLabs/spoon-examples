package fr.inria.gforge.spoon.assertgenerator.workflow;

import fr.inria.gforge.spoon.assertgenerator.Logger;
import org.junit.Assert;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtTypeAccess;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtExecutableReference;

import java.util.List;

import static fr.inria.gforge.spoon.assertgenerator.Util.getGetters;
import static fr.inria.gforge.spoon.assertgenerator.Util.getKey;
import static fr.inria.gforge.spoon.assertgenerator.Util.invok;

/**
 * Created by Benjamin DANGLOT
 * benjamin.danglot@inria.fr
 * on 22/06/17
 */
public class AssertionAdder {

	private Factory factory;

	public AssertionAdder(Factory factory) {
		this.factory = factory;
	}

	@SuppressWarnings("unchecked")
	public void addAssertion(CtMethod<?> testMethod, List<CtLocalVariable> ctLocalVariables) {
		ctLocalVariables.forEach(ctLocalVariable -> this.addAssertion(testMethod, ctLocalVariable));
		System.out.println(testMethod);
	}

	@SuppressWarnings("unchecked")
	void addAssertion(CtMethod testMethod, CtLocalVariable localVariable) {
		List<CtMethod> getters =
				getGetters(localVariable);
		getters.forEach(getter -> {
			String key = getKey(getter);
			CtInvocation invocationToGetter =
					invok(getter, localVariable);
			CtInvocation invocationToAssert = createAssert("assertEquals",
					factory.createLiteral(Logger.observations.get(key)), // expected
					invocationToGetter); // actual
			testMethod.getBody().insertEnd(invocationToAssert);
		});
	}

	public static CtInvocation createAssert(String name, CtExpression... parameters) {
		final Factory factory = parameters[0].getFactory();
		CtTypeAccess accessToAssert =
				factory.createTypeAccess(factory.createCtTypeReference(Assert.class));
		CtExecutableReference assertEquals = factory.Type().get(Assert.class)
				.getMethodsByName(name).get(0).getReference();
		return factory.createInvocation(
				accessToAssert,
				assertEquals,
				parameters[0],
				parameters[1]
		);
	}

}
