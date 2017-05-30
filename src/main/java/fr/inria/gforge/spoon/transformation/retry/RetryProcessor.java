package fr.inria.gforge.spoon.transformation.retry;

import fr.inria.gforge.spoon.transformation.retry.template.RetryTemplate;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.declaration.CtMethod;

/**
 * Created by nicolas on 22/01/2015.
 */
public class RetryProcessor extends AbstractAnnotationProcessor<RetryOnFailure, CtMethod<?>> {

	@Override
	public void process(RetryOnFailure retryOnFailure, CtMethod<?> ctMethod) {
		RetryTemplate template = new RetryTemplate(
				ctMethod.getBody(),
				retryOnFailure.attempts(),
				retryOnFailure.delay(),
				retryOnFailure.verbose()
		);

		CtBlock newBody = template.apply(ctMethod.getDeclaringType());
		ctMethod.setBody(newBody);
	}
}
