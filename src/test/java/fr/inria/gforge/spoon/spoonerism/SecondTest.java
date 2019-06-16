package fr.inria.gforge.spoon.spoonerism;

import java.util.Locale;

public class SecondTest extends ExistingBase {

    @Override
    public String getFixture() {
        class StrangeFixture {}
        StrangeFixture f = new StrangeFixture();
        String name = f.getClass().getSimpleName().toLowerCase(Locale.ROOT);
        System.out.println(name);
        return name;
    }
}
