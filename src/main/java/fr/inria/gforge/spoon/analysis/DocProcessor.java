package fr.inria.gforge.spoon.analysis;

import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtModifiable;
import spoon.reflect.declaration.CtNamedElement;
import spoon.reflect.declaration.ModifierKind;

import java.util.ArrayList;
import java.util.List;

/**
 * Reports warnings when undocumented elements are found.
 */
public class DocProcessor extends AbstractProcessor<CtElement> {
	public final List<CtElement> undocumentedElements = new ArrayList<CtElement>();

	public void process(CtElement element) {
		if (element instanceof CtNamedElement || element instanceof CtField || element instanceof CtExecutable) {
			if (((CtModifiable) element).getModifiers().contains(ModifierKind.PUBLIC) || ((CtModifiable) element).getModifiers().contains(ModifierKind.PROTECTED)) {
				if (element.getDocComment() == null || element.getDocComment().equals("")) {
					undocumentedElements.add(element);
				}
			}
		}
	}

}
