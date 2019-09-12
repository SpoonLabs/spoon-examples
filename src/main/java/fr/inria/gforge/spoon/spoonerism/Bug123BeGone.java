package fr.inria.gforge.spoon.spoonerism;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class Bug123BeGone {
    @Test
    public void test() {
        assertEquals(123, 12 * 10 + 3);
    }
}
