package fr.inria.gforge.spoon.analysis.processing;

import spoon.processing.AbstractProcessor;
import spoon.processing.Severity;
import spoon.reflect.code.CtCatch;

import java.util.ArrayList;
import java.util.List;

/**
 * Reports warnings when empty catch blocks are found.
 */
public class CatchProcessor extends AbstractProcessor<CtCatch> {

	public List<CtCatch> emptyCatchs = new ArrayList<CtCatch>();

	public void process(CtCatch element) {
		if (element.getBody().getStatements().size() == 0) {
			emptyCatchs.add(element);
			getFactory().getEnvironment().report(this, Severity.WARNING, element, "empty catch clause");
		}
	}

}