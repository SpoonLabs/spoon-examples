package fr.inria.gforge.spoon.transformation.mutation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mdkt.compiler.InMemoryJavaCompiler;

import spoon.Launcher;
import spoon.processing.Processor;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.visitor.Filter;
import spoon.reflect.visitor.filter.TypeFilter;

/** mutates and kills mutants of type T.
 * 
 *  @See {@link MutationTesterTest} for an example usage
 */
public class MutationTester<T> {

	/** the content of the Java source code file to be mutated */
	private String sourceCodeToBeMutated;
	/** responsible for killing the mutants */
	private TestDriver testDriver;
	/** mutation operator */
	private Processor mutator;
	
	/** the produced mutants */
	private final List<CtClass> mutants = new ArrayList<>();

	// public for testing
	public final List<T> mutantInstances = new ArrayList<>();

	public MutationTester(String src, TestDriver tester, Processor mutator) {
		this.sourceCodeToBeMutated = src;
		this.testDriver = tester;
		this.mutator = mutator;
	}

	/** returns a list of mutant classes */
	public void generateMutants() {
		Launcher l = new Launcher();
		l.addInputResource(sourceCodeToBeMutated);
		l.buildModel();

		CtClass origClass = (CtClass) l.getFactory().Package().getRootPackage()
				.getElements(new TypeFilter(CtClass.class)).get(0);

		// now we apply a transformation
		// we replace "+" and "*" by "-"
		List<CtElement> elementsToBeMutated = origClass.getElements(new Filter<CtElement>() {

			@Override
			public boolean matches(CtElement arg0) {
				return mutator.isToBeProcessed(arg0);
			}
		});
		
		for (CtElement e : elementsToBeMutated) {
			// this loop is the trickiest part
			// because we want one mutation after the other
			
			// cloning the AST element
			CtElement op = l.getFactory().Core().clone(e);
			
			// mutate the element
			mutator.process(op);
			
			// temporarily replacing the original AST node with the mutated element 
			replace(e,op);

			// creating a new class containing the mutating code
			CtClass klass = l.getFactory().Core()
					.clone(op.getParent(CtClass.class));
			// setting the package
			klass.setParent(origClass.getParent());

			// adding the new mutant to the list
			mutants.add(klass);

			// restoring the original code
			replace(op, e);
		}
	}

	public List<CtClass> getMutants() {
		return Collections.unmodifiableList(mutants);
	}
	
	private void replace(CtElement e, CtElement op) {
		if (e instanceof CtStatement  && op instanceof CtStatement) {
			e.replace(op);
			return;
		}
		if (e instanceof CtExpression && op instanceof CtExpression) {
			e.replace(op);
			return;
		}
		throw new IllegalArgumentException(e.getClass()+" "+op.getClass());
	}

	/** tries to kill all generated mutants, throws an AssertionError if one mutant is not killed */
	public void killMutants() throws Exception {
		
		List<Class<?>> compiledMutants = compileMutants(mutants);

		List<T> mutantInstances = instantiateMutants(compiledMutants);

		runTestsOnEachMutantInstance(mutantInstances);
		
	}

	/** applies the test driver of this mutation tester on each mutant instance */
	public void runTestsOnEachMutantInstance(List<T> mutantInstances) throws Exception {
		// now we run the mutants against the test class
		for (T t : mutantInstances) {
			try {
				testDriver.test(t);
				throw new MutantNotKilledException();
			} catch (AssertionError expected) {
				System.out.println("mutant killed!");
			}
		}
	}

	/** instantiate the mutant classes using the default zero-arg constructor */
	public List<T> instantiateMutants(List<Class<?>> compiledMutants)
			throws Exception {
		// we run each mutant one by one and check whether they are killed

		// instantiating the mutant classes
		for (Class mutantClass : compiledMutants) {
			mutantInstances.add((T) mutantClass.newInstance());
		}
		return mutantInstances;
	}

	/** compiles the mutants on the fly */
	public List<Class<?>> compileMutants(List<CtClass> mutants) throws Exception {
		List<Class<?>> compiledMutants = new ArrayList<>();
		for (CtClass mutantClass : mutants) {
			Class<?> klass = InMemoryJavaCompiler.newInstance().compile(
					mutantClass.getQualifiedName(), "package "
							+ mutantClass.getPackage().getQualifiedName() + ";"
							+ mutantClass);
			compiledMutants.add(klass);
		}
		return compiledMutants;
	}

}
