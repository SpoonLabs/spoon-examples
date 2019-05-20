package fr.inria.gforge.spoon.transformation.bound;

import fr.inria.gforge.spoon.transformation.bound.processing.BoundProcessor;
import fr.inria.gforge.spoon.transformation.bound.processing.BoundTemplateProcessor;
import fr.inria.gforge.spoon.transformation.bound.src.Main;
import org.junit.Test;
import spoon.Launcher;
import spoon.SpoonAPI;
import spoon.reflect.code.CtIf;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;

import static org.junit.Assert.assertTrue;

public class BoundTest {
	@Test
	public void testBoundTemplate() throws Exception {
		SpoonAPI launcher = new Launcher();
		launcher.getEnvironment().setNoClasspath(true);
		launcher.addInputResource("./src/main/java");
		launcher.setSourceOutputDirectory("./target/spoon-template");
		launcher.addProcessor(new BoundTemplateProcessor());
		launcher.run();

		final CtType<Main> target = launcher.getFactory().Type().get(Main.class);
		final CtMethod<?> m = target.getMethodsByName("m").get(0);

		assertTrue(m.getBody().getStatements().size() >= 2);
		assertTrue(m.getBody().getStatement(0) instanceof CtIf);
		assertTrue(m.getBody().getStatement(1) instanceof CtIf);
	}

	@Test
	public void testBoundProcessor() throws Exception {
		SpoonAPI launcher = new Launcher();
		launcher.getEnvironment().setNoClasspath(true);
		launcher.addInputResource("./src/main/java");
		launcher.setSourceOutputDirectory("./target/spoon-processor");
		launcher.addProcessor(new BoundProcessor());
		launcher.run();

		final CtType<Main> target = launcher.getFactory().Type().get(Main.class);
		final CtMethod<?> m = target.getMethodsByName("m").get(0);

		assertTrue(m.getBody().getStatements().size() >= 2);
		assertTrue(m.getBody().getStatement(0) instanceof CtIf);
		assertTrue(m.getBody().getStatement(1) instanceof CtIf);
	}
}
