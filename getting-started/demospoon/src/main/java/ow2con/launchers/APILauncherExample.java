package ow2con.launchers;

import spoon.Launcher;
import spoon.reflect.CtModel;

public class APILauncherExample {
    public static void main(String[] args) {
        Launcher launcher = new Launcher();
        launcher.addInputResource("/path/to/sources");
        launcher.getEnvironment().setNoClasspath(true);
        launcher.getEnvironment().setSourceClasspath(
                "lib1.jar:lib2.jar".split(":")
        );
        launcher.getEnvironment().setComplianceLevel(7);
        CtModel model = launcher.buildModel();
    }
}
