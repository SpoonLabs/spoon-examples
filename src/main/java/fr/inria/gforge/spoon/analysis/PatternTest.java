package fr.inria.gforge.spoon.analysis;

import org.junit.Test;
import spoon.Launcher;
import spoon.pattern.Match;
import spoon.pattern.Pattern;
import spoon.pattern.PatternBuilder;
import spoon.pattern.Quantifier;
import spoon.processing.ProcessingManager;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.factory.Factory;
import spoon.reflect.path.CtRole;
import spoon.reflect.visitor.Filter;
import spoon.support.QueueProcessingManager;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

// Examples of Spoon patterns
// http://spoon.gforge.inria.fr/pattern.html
public class PatternTest {
	@Test
	public void pattern() throws Exception {
		final String[] args = {
				"-i", "src/test/resources/src/",
		};

		final Launcher launcher = new Launcher();
		launcher.setArgs(args);
		launcher.buildModel();
		Factory spoon = launcher.getFactory();

		// creating an simple template from a method
		CtType<?> mainClass = spoon.Type().get("src.Main");
		CtMethod<?> m1 = mainClass.getMethodsByName("m1").get(0);

		{
			// creating the pattern with no parameter
			Pattern t1 = PatternBuilder.create(m1.clone()).build();
			List<Match> matches = t1.getMatches(spoon.getModel().getRootPackage());
			// we match itself, only 1 match
			assertEquals(1, matches.size());
			for (Match m : matches) {
				System.out.println(m.getMatchingElement().getPosition());
			}
		}

		{
			// now we take the body
			Pattern t2 = PatternBuilder.create(mainClass.getMethodsByName("m1").get(0).getBody())
					.build();
			List<Match> matches2 = t2.getMatches(spoon.getModel().getRootPackage());
			for (Match m : matches2) {
				// one variable is not in the scope it is automatically a parameter
				assertEquals(1, m.getParametersMap().size());
			}
			assertEquals(2, matches2.size());
		}

		{
			// now we take the method call only
			// and we absract over the receiver
			Pattern t3 = PatternBuilder.create(mainClass.getMethodsByName("m").get(0).getBody().getStatement(0).clone())
					.configurePatternParameters(pb -> pb.parameter("receiver").byReferenceName("c"))
					.build();
			List<Match> matches3 = t3.getMatches(spoon.getModel().getRootPackage());
			for (Match m : matches3) {
				System.out.println(m.getMatchingElement().getPosition());
			}
			// note that the one called "d" also matches!
			assertEquals(3, matches3.size());
		}


	}
}
