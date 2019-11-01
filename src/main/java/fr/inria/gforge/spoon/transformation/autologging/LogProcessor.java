package fr.inria.gforge.spoon.transformation.autologging;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtExecutable;

/**
 * Example of tracing
 *
 * class A{
 *   void m(Object o} { ...(method body) .... }
 * }
 *
 * is transformed into
 *
 *  void m(Object o} {
 *      System.out.println("enter in method m from class A");
 *      // rest of the method body
 *  }
 * Use with
 * $ java -jar spoon.jar -i src/main/java -o spooned -p fr.inria.gforge.spoon.transformation.autologging.LogProcessor
 *
 * Of with https://github.com/SpoonLabs/spoon-maven-plugin
 * 
 */
public class LogProcessor extends AbstractProcessor<CtExecutable> {

	@Override
	public void process(CtExecutable element) {
		CtCodeSnippetStatement snippet = getFactory().Core().createCodeSnippetStatement();

		// Snippet which contains the log.
		final String value = String.format("System.out.println(\"Enter in the method %s from class %s\");",
				element.getSimpleName(),
				element.getParent(CtClass.class).getSimpleName());
		snippet.setValue(value);

		// Inserts the snippet at the beginning of the method body.
		if (element.getBody() != null) {
			element.getBody().insertBegin(snippet);
		}
	}
}
