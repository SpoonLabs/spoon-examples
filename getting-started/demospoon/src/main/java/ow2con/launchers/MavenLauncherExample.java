package ow2con.launchers;

import spoon.MavenLauncher;
import spoon.reflect.CtModel;

public class MavenLauncherExample {
    public static void main(String[] args) {
        MavenLauncher launcher = new MavenLauncher(
                "path/to/my/project",
                 MavenLauncher.SOURCE_TYPE.APP_SOURCE);

        CtModel model = launcher.buildModel();


    }
}
