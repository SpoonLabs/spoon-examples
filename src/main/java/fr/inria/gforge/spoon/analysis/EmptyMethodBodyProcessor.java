package fr.inria.gforge.spoon.analysis;

import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.ModifierKind;

import java.util.ArrayList;
import java.util.List;

/**
 * Reports warnings when empty methods are found.
 */
public class EmptyMethodBodyProcessor extends AbstractProcessor<CtMethod<?>> {

	public final List<CtMethod> emptyMethods = new ArrayList<>();
	public void process(CtMethod<?> element) {
		if (element.getParent(CtClass.class) != null && !element.getModifiers().contains(ModifierKind.ABSTRACT) && element.getBody().getStatements().isEmpty()) {
			emptyMethods.add(element);
		}
	}

}
