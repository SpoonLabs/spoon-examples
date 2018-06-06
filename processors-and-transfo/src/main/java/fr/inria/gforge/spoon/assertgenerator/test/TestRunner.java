package fr.inria.gforge.spoon.assertgenerator.test;

import org.junit.runner.Request;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Created by Benjamin DANGLOT
 * benjamin.danglot@inria.fr
 * on 23/05/17
 */
public class TestRunner {

    private static Function<String[], URL[]> arrayStringToArrayUrl = (arrayStr) ->
            Arrays.stream(arrayStr)
                    .map(File::new)
                    .map(File::toURI)
                    .map(uri -> {
                        try {
                            return uri.toURL();
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toArray(URL[]::new);

    //TODO should maybe run the Listener

    public static List<Failure> runTest(String fullQualifiedName, String testCaseName, String[] classpath) throws MalformedURLException, ClassNotFoundException {
        ClassLoader classLoader = new URLClassLoader(
                arrayStringToArrayUrl.apply(classpath),
                ClassLoader.getSystemClassLoader()
        );
        Request request = Request.method(classLoader.loadClass(fullQualifiedName), testCaseName);
        Runner runner = request.getRunner();
        RunNotifier fNotifier = new RunNotifier();
        final TestListener listener = new TestListener();
        fNotifier.addFirstListener(listener);
        fNotifier.fireTestRunStarted(runner.getDescription());
        runner.run(fNotifier);
        return listener.getTestFails();
    }

    public static List<Failure> runTest(String fullQualifiedName, String[] classpath) throws MalformedURLException, ClassNotFoundException {
        ClassLoader classLoader = new URLClassLoader(
                arrayStringToArrayUrl.apply(classpath),
                ClassLoader.getSystemClassLoader()
        );
        Request request = Request.classes(classLoader.loadClass(fullQualifiedName));
        Runner runner = request.getRunner();
        RunNotifier fNotifier = new RunNotifier();
        final TestListener listener = new TestListener();
        fNotifier.addFirstListener(listener);
        fNotifier.fireTestRunStarted(runner.getDescription());
        runner.run(fNotifier);
        return listener.getTestFails();
    }

}
