package fr.inria.gforge.spoon.transformation;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import spoon.reflect.declaration.CtClass;
import fr.inria.gforge.spoon.transformation.mutation.BinaryOperatorMutator;
import fr.inria.gforge.spoon.transformation.mutation.MutantNotKilledException;
import fr.inria.gforge.spoon.transformation.mutation.MutationTester;
import fr.inria.gforge.spoon.transformation.mutation.TestDriver;

public class MutationTesterTest {

	@Test
	public void testMutationTester() throws Exception {
		// mutation testing requires three things
		// 1. the code to be mutated
		// 2. the test driver to kill the mutants 
		// 3. the mutation operator
		String codeToBeMutated = "src/test/resources/transformation/fr/inria/gforge/spoon/transformation/Foo2.java";
		
		TestDriver<IFoo> testDriverForIFooObjects = new TestDriver<IFoo>() {
			@Override
			public void test(IFoo t) {
				assertEquals(2, t.m());
				assertEquals(6, t.n());		
			}
		};
		
		BinaryOperatorMutator mutationOperator = new BinaryOperatorMutator();

		// we instantiate the mutation tester
		MutationTester<IFoo> mutationTester = new MutationTester<>(codeToBeMutated, testDriverForIFooObjects, mutationOperator);
		
		// generating the mutants
		mutationTester.generateMutants();
		List<CtClass> mutants = mutationTester.getMutants();
		assertEquals(2, mutants.size());
		
		// killing the mutants, no exception should be thrown
		try {
			mutationTester.killMutants();				
		} catch (MutantNotKilledException e) {
			Assert.fail();
		}
		
		// another couple of assertions for testing that mutants are actually mutants
		// testing the first mutant
		// 1-1 = 0
		assertEquals(0, mutationTester.mutantInstances.get(0).m());
		assertEquals(6, mutationTester.mutantInstances.get(0).n());
		
		// testing the second mutant
		assertEquals(2, mutationTester.mutantInstances.get(1).m());
		// 2-3 = -1
		assertEquals(-1, mutationTester.mutantInstances.get(1).n());
	}
}
