package fr.inria.gforge.spoon.transformation.bound.processing;

import fr.inria.gforge.spoon.transformation.bound.annotation.Bound;
import fr.inria.gforge.spoon.transformation.bound.template.BoundTemplate;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtParameter;
import spoon.template.Template;

public class BoundTemplateProcessor extends AbstractAnnotationProcessor<Bound, CtParameter<?>> {

	public void process(Bound annotation, CtParameter<?> element) {
		// Is to be process?
		CtExecutable<?> e = element.getParent();
		if (e.getBody() == null) {
			return;
		}

		// Use template.
		CtClass<?> type = e.getParent(CtClass.class);
		Template t = new BoundTemplate(getFactory().Type().createReference(Double.class), element.getSimpleName(), annotation.min(), annotation.max());
		final CtBlock apply = (CtBlock) t.apply(type);

		// Apply transformation.
		for (int i = apply.getStatements().size() - 1; i >= 0; i--) {
			final CtStatement statement = apply.getStatement(i);
			statement.delete();
			e.getBody().insertBegin(statement);
		}
	}
}
