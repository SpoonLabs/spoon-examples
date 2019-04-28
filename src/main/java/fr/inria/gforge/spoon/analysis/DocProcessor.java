package fr.inria.gforge.spoon.analysis;

import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtModifiable;
import spoon.reflect.declaration.CtNamedElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import spoon.reflect.declaration.ModifierKind;

import static spoon.reflect.declaration.ModifierKind.PUBLIC;
import static spoon.reflect.declaration.ModifierKind.PROTECTED;
/**
 * Reports warnings when undocumented elements are found.
 */
public class DocProcessor extends AbstractProcessor<CtElement> {
	public final List<CtElement> undocumentedElements = new ArrayList<>();

	public void process(CtElement element) {
		if (element instanceof CtNamedElement || element instanceof CtField || element instanceof CtExecutable) {
                    Set<ModifierKind> modifiers = ((CtModifiable) element).getModifiers();
			if (modifiers.contains(PUBLIC) || modifiers.contains(PROTECTED)) {
                            String docComment = element.getDocComment();
				if (docComment == null || docComment.equals("")) {
					undocumentedElements.add(element);
				}
			}
		}
	}

}
