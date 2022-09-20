package fr.inria.gforge.spoon.transformation.bound.processing;

import spoon.processing.AbstractAnnotationProcessor;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtThrow;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import fr.inria.gforge.spoon.transformation.bound.annotation.Bound;

/**
 * we only process method parameters (CtParameter) annotated with @Bound
 *
 * void m(@Bound(min = 2d, max = 8d) int a) { ...(method body) .... }
 *
 * is transformed into
 *
 *  void m(int a} {
 *      if (a < 2d) { throw new RuntimeException("out of min bound (a < 2d)"); }
 *      if (a > 8d) { throw new RuntimeException("out of max bound (a > 8d)"); }
 *      // rest of the method body
 *  }
 *
 * @author Nicolas Petitprez
 */
public class BoundProcessor extends AbstractAnnotationProcessor<Bound, CtParameter<?>> {
	public void process(Bound annotation, CtParameter<?> element) {
		final CtMethod parent = element.getParent(CtMethod.class);

		// Build if check for min.
		CtIf anIf = getFactory().Core().createIf();
		anIf.setCondition(getFactory().Code().<Boolean>createCodeSnippetExpression(element.getSimpleName() + " < " + annotation.min()));
		CtThrow throwStmt = getFactory().Core().createThrow();
		throwStmt.setThrownExpression((CtExpression<? extends Throwable>) getFactory().Core().createCodeSnippetExpression().setValue("new RuntimeException(\"out of min bound (\" + " + element.getSimpleName() + " + \" < " + annotation.min() + "\")"));
		anIf.setThenStatement(throwStmt);
		parent.getBody().insertBegin(anIf);
		anIf.setParent(parent);

		// Build if check for max.
		anIf = getFactory().Core().createIf();
		anIf.setCondition(getFactory().Code().<Boolean>createCodeSnippetExpression(element.getSimpleName() + " > " + annotation.max()));
		anIf.setThenStatement(getFactory().Code().createCtThrow("new RuntimeException(\"out of max bound (\" + " + element.getSimpleName() + " + \" > " + annotation.max() + "\")"));
		parent.getBody().insertBegin(anIf);
		anIf.setParent(parent);
	}
}
