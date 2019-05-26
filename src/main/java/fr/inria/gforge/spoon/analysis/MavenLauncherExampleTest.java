package fr.inria.gforge.spoon.analysis;

import org.junit.Test;
import spoon.MavenLauncher;
import spoon.reflect.CtModel;

// we can analyze a project directly, Spoon takes the dependencies from pom.xml
public class MavenLauncherExampleTest {
    @Test
    public void main() {
        MavenLauncher launcher = new MavenLauncher(
                "./src/test/resources/project/",
                 MavenLauncher.SOURCE_TYPE.APP_SOURCE);

        CtModel model = launcher.buildModel();


    }
}
