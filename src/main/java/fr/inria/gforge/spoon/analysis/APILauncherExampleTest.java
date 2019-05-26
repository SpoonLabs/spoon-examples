package fr.inria.gforge.spoon.analysis;

import org.junit.Test;
import spoon.Launcher;
import spoon.reflect.CtModel;

// basic eaxmple of using the launcher
public class APILauncherExampleTest {
    @Test
    public void main() {
        Launcher launcher = new Launcher();
        launcher.addInputResource("src/test/resources/src");
        launcher.getEnvironment().setNoClasspath(true);
        // optional
        // launcher.getEnvironment().setSourceClasspath(
        //        "lib1.jar:lib2.jar".split(":"));
        launcher.getEnvironment().setComplianceLevel(7);
        CtModel model = launcher.buildModel();
    }
}
