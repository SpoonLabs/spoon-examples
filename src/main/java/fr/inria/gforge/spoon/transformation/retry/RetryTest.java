package fr.inria.gforge.spoon.transformation.retry;

import fr.inria.gforge.spoon.transformation.retry.template.RetryTemplate;
import fr.inria.gforge.spoon.utils.TestSpooner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collection;

/**
 * Created by nicolas on 22/01/2015.
 */
public class RetryTest {
	TestSpooner spooner;

	private static final String TEST_CLASS = TestClass.class.getName();

	@Before
	public void setUp() throws Exception {
		spooner = new TestSpooner()
				.addSource(new File("src/main/java/" + TEST_CLASS.replaceAll("\\.", "/") + ".java"))
				.addTemplate(
						new File("src/main/java/" + RetryTemplate.class.getName().replaceAll("\\.", "/") + ".java"));
	}

	@Test
	public void testRetry() throws Exception {
		spooner.process(RetryProcessor.class);
		spooner.print(new File("target/spooned"));
		Assert.assertTrue(spooner.compile());

		Class clz = spooner.getSpoonedClass(TEST_CLASS);
		Object instance = clz.newInstance();
		try {
			clz.getMethod("retry").invoke(instance);
			Assert.fail("retry method should always fail");
		} catch (ReflectiveOperationException | IllegalArgumentException | SecurityException ex) {
			// always fail
		}
		Field field = clz.getDeclaredField("result");
		field.setAccessible(true);
		Collection<Long> result = (Collection<Long>) field.get(instance);
		Assert.assertEquals(3, result.size());
	}
}
