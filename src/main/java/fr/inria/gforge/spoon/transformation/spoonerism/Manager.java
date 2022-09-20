package fr.inria.gforge.spoon.transformation.spoonerism;


/**
 * A class that orders the operations provided by Spoonerism.
 * Great to fit on a slide and to execute via ide.
 */
public class Manager {
    public static void main(String[] args) {
        new Spoonerism()
                .readClasses()
                .display()
                .enumerateTestingClasses()
                .determineBaseTestingClassPackage()
                .createBaseTestingClass()
                .extendTestingClasses()
                .writeTransformedClasses();
    }
}
