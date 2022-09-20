package fr.inria.gforge.spoon.transformation.apichecking;

import org.junit.Test;
import spoon.MavenLauncher;
import spoon.compiler.Environment;

public class ProcessorMainTest {
    @Test
    public void main() {
        String projectPath = ".";

        MavenLauncher launcher = new MavenLauncher(projectPath, MavenLauncher.SOURCE_TYPE.APP_SOURCE);
        Environment environment = launcher.getEnvironment();
        environment.setNoClasspath(true);
        environment.setCommentEnabled(true);
        environment.setAutoImports(true);

        launcher.addProcessor(new APICheckingProcessor());
        launcher.run();
    }
}
