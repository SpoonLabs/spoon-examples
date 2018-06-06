package fr.inria.gforge.spoon.nton.processing;

import fr.inria.gforge.spoon.nton.annotation.Nton;
import fr.inria.gforge.spoon.nton.template.NtonCodeTemplate;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.reflect.declaration.CtClass;

public class NtonProcessor extends AbstractAnnotationProcessor<Nton, CtClass<?>> {

	public void process(Nton nton, CtClass<?> cl) {
		NtonCodeTemplate template = new NtonCodeTemplate(cl.getReference(),
				nton.n());

		template.apply(cl);
	}

}
