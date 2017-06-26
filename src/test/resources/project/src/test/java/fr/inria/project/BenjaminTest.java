package fr.inria.project;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Benjamin DANGLOT
 * benjamin.danglot@inria.fr
 * on 20/06/17
 */
public class BenjaminTest {

	@Test
	public void doYouKnowBenjamin() throws Exception {
		Benjamin benjamin = new Benjamin();
		assertEquals(24, benjamin.age());
	}

}