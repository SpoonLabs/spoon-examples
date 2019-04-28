package fr.inria.gforge.spoon.assertgenerator.test;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Benjamin DANGLOT
 * benjamin.danglot@inria.fr
 * on 23/05/17
 */
class TestListener extends RunListener {

    private final List<Description> testRun = new ArrayList<>();
    private final List<Failure> testFails = new ArrayList<>();

    @Override
    public synchronized void testFinished(Description description) throws Exception {
        this.testRun.add(description);
    }

    @Override
    public synchronized void testFailure(Failure failure) throws Exception {
        this.testFails.add(failure);
    }

    @Override
    public synchronized void testAssumptionFailure(Failure failure) {
        //empty
    }

    List<Failure> getTestFails() {
        return testFails;
    }
}
