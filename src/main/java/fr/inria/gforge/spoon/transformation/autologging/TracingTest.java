package fr.inria.gforge.spoon.transformation.autologging;

import org.junit.Test;
import spoon.Launcher;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.factory.Factory;

import java.lang.annotation.Annotation;
import java.util.List;

// example of tracing based on annotations
// author: Thomas Durieux
public class TracingTest {
    @Test
    public void main() {

        Launcher spoon = new Launcher();
        spoon.addInputResource("./src/");

        spoon.addProcessor(new AbstractProcessor<CtMethod>() {
            private CtAnnotation<? extends Annotation> getRESTAnnotation(CtMethod element) {
                List<CtAnnotation<? extends Annotation>> annotations = element.getAnnotations();
                for (CtAnnotation<? extends Annotation> annotation : annotations) {
                    if (annotation.getAnnotationType().getQualifiedName().contains("javax.ws.rs")) {
                        return annotation;
                    }
                }
                return null;
            }
            @Override
            public boolean isToBeProcessed(CtMethod element) {
                // log all methods that have a REST (javax.ws.rs) annotation
                CtAnnotation<? extends Annotation> annotation = getRESTAnnotation(element);
                return annotation != null;
            }

            @Override
            public void process(CtMethod element) {
                // add the call to the tracing method at the beginning of the method
                element.getBody().insertBegin(getFactory().createCodeSnippetStatement("System.out.println(\"Trace " + element.getSignature() + "\")"));
            }
        });

        spoon.run();
    }
}
