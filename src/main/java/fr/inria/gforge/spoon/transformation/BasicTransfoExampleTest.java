package fr.inria.gforge.spoon.transformation;

import org.junit.Test;
import spoon.Launcher;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.factory.Factory;

// super simple transformation: creating a class and adding a method
public class BasicTransfoExampleTest {
    @Test
    public void main() {
        Launcher launcher = new Launcher();


        Factory factory = launcher.getFactory();
        CtClass aClass = factory.createClass("my.org.MyClass");

        aClass.setSimpleName("myNewName");
        CtMethod myMethod = factory.createMethod();
        aClass.addMethod(myMethod);
    }
}
