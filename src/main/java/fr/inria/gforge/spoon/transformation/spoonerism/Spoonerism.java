package fr.inria.gforge.spoon.transformation.spoonerism;

import spoon.Launcher;
import spoon.SpoonAPI;
import spoon.compiler.Environment;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.support.gui.SpoonModelTree;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Class providing simple, common operations on a codebase
 * It is organised as a fluent interface in order to not need
 * passing parameters to methods.  There is no other real need to
 * organise code this way but it fits nicely on slides.
 *
 * Try to get a codebase and then create a common base test
 * class that all other test classes extend.
 */
class Spoonerism {
    String IN_DIR = "src/main/java/";
    String OUT_DIR = "target/spoonerism/";
    // Main API - main place to go into spoon
    SpoonAPI spoonUniverse;
    // place to put temporary results between invocations
    HashSet<CtClass<?>> testingClasses;
    String baseTestingClassPackage;
    CtClass<?> baseTestingClass;

    Spoonerism readClasses() {
        // Command line launcher.  There are more for launching via maven, gradle...
        spoonUniverse = new Launcher();

        // Call it many times if needed - on directories and files
        spoonUniverse.addInputResource(IN_DIR);

        // Build and wait - but generally fast enough.
        spoonUniverse.buildModel();
        return this;
    }

    Spoonerism display() {
        // Will not work:
        // System.out.println(spoonUniverse);

        // Get a graphical overview, constructing is enough
        SpoonModelTree tree = new SpoonModelTree(
                spoonUniverse.getFactory());

        /* You get the same via command line:
        java -jar spoon-core-...-with-dependencies.jar -g -i src/test/java
        */
        return this;
    }

    Spoonerism enumerateTestingClasses() {
        // This will only filter classes - not enough
        TypeFilter<CtClass<?>> isClass =
                new TypeFilter<>(CtClass.class);

        // If we need something - we obtain it via factory
        Factory factory = spoonUniverse.getFactory();
        // Create a reference to what we need
        CtTypeReference<?> juTestRef =
                factory.Type() .createReference("org.junit.Test");
        // can also use class literal: factory.Type().createReference(org.junit.Test.class);
        // however beware that then you need the class due to static linking

        // Now lets try to get classes with methods annotated with org.junit.Test -
        // still not enough
        TypeFilter<CtClass<?>> isWithTestAnnotatedMethods =
                new TypeFilter<CtClass<?>>(CtClass.class) {
                    @Override
                    public boolean matches(CtClass<?> ctClass) {
                        return super.matches(ctClass) &&
                                !ctClass
                                    .getMethodsAnnotatedWith(juTestRef)
                                    .isEmpty();
                    }
                };

        // The proper way - the annotated method may come from an extended class
        TypeFilter<CtClass<?>> isRealTestingClass =
                new TypeFilter<CtClass<?>>(CtClass.class) {
            @Override
            public boolean matches(CtClass<?> ctClass) {
                // First step is to reuse standard filtering
                if (!super.matches(ctClass)) {
                    return false;
                }
                CtTypeReference<?> current = ctClass.getReference();
                // Walk up the chain of inheritance and find whether there is a method annotated as test
                do {
                    if (!current.getTypeDeclaration()
                            .getMethodsAnnotatedWith(juTestRef).isEmpty()) {
                        return true;
                    }
                } while ((current = current.getSuperclass()) != null);
                return false;
            }
        };
        testingClasses = new HashSet<>(spoonUniverse.getModel().getRootPackage()
                .getElements(isRealTestingClass));
        return this;
    }

    Spoonerism determineBaseTestingClassPackage(){
        // Find the package to place it - basically package covering all tests
        // Get one of the classes to start, first one will do
        CtClass<?> firstClass = testingClasses.iterator().next();
        String qualifiedName = firstClass.getPackage().getQualifiedName();
        List<String> commonComponents = Arrays.asList(
                qualifiedName.split("[.]"));
        // For all the testing classes find the common subsequence of package names
        for (CtClass<?> ctClass: testingClasses) {
            List<String> currentComponents = Arrays.asList(
                    ctClass.getPackage().getQualifiedName().split("[.]"));
            int max = Math.min(currentComponents.size(), commonComponents.size());
            for (int i = 0; i < max; i++ ) {
                if (!currentComponents.get(i).equals(commonComponents.get(i))) {
                    commonComponents = commonComponents.subList(0, i);
                    break;
                }
            }
        }
        baseTestingClassPackage = String.join(".", commonComponents);
        return this;
    }

    Spoonerism createBaseTestingClass() {
        // Creating a class - again use a factory
        String baseClassFqn = baseTestingClassPackage + ".BaseTest";
        baseTestingClass = spoonUniverse.getFactory().createClass(baseClassFqn);
        return this;
    }

    Spoonerism extendTestingClasses() {
        // Now we need to extend the classes.
        // WARNING: There is a small problem with this, can you spot it?
        // Answer at bottom
        CtTypeReference<?> baseTestingClassRef =
                baseTestingClass.getReference();
        for (CtClass<?> ctClass: testingClasses) {
            if (ctClass.getSuperclass() == null) {
                ctClass.setSuperclass(baseTestingClassRef);
            }
        }
        return this;
    }

    Spoonerism writeTransformedClasses () {
        // And now just write the transformed classes
        Environment env = spoonUniverse.getEnvironment();
        // replace FQN with imports and short names
        env.setAutoImports(true);
        // include comments - source is for human consumption
        env.setCommentEnabled(true);

        // where to write
        spoonUniverse.setSourceOutputDirectory(OUT_DIR);
        // default printing, use a different printer for even fancier formatting
        spoonUniverse.prettyprint();
        return this;
    }

    // And now answer to the riddle in extendTestingClasses
    // Some test classes may extend a class that you do not have the sources for.
    // You will not be able to extend the class created by createBaseTestingClass -
    // Java is single inheritance.
    // A very simple though contrived example would be:
    // `TestClass extends Object`, remember Spoon works at source text level!
    // You should then decide whether to be silent, issue a warning or abort mission.
}
