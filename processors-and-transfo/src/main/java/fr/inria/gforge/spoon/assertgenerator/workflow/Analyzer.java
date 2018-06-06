package fr.inria.gforge.spoon.assertgenerator.workflow;

import org.junit.Test;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Benjamin DANGLOT
 * benjamin.danglot@inria.fr
 * on 22/06/17
 */
public class Analyzer {

	List<CtLocalVariable> analyze(CtMethod testMethod) {
		TypeFilter filterLocalVar =
				new TypeFilter(CtLocalVariable.class) {
					public boolean matches(CtLocalVariable localVariable) {
						return !localVariable.getType().isPrimitive();
					}
				};
		return testMethod.getElements(filterLocalVar);
	}


	public Map<CtMethod, List<CtLocalVariable>> analyze(CtType<?> ctClass) {
		CtTypeReference<Test> reference =
				ctClass.getFactory().Type().createReference(Test.class);
		return ctClass.getMethods().stream()
				.filter(ctMethod -> ctMethod.getAnnotation(reference) != null)
				.collect(Collectors.toMap(
						ctMethod -> ctMethod,
						this::analyze)
				);
	}
}
