package fr.inria.gforge.spoon.spoonerism;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ExistingBase {
    @Test
    public void test() {
        assertEquals('s', getFixture().charAt(0));
    }

    public String getFixture() {
        return TestUtils.STANDARD;
    }
}
