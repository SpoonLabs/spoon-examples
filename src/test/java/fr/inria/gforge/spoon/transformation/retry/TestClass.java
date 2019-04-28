package fr.inria.gforge.spoon.transformation.retry;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by nicolas on 22/01/2015.
 */
public class TestClass {

	private Collection<Long> result = new ArrayList<>();

	@RetryOnFailure(attempts = 3, delay = 10, verbose = false)
	public void retry() {
		result.add(System.currentTimeMillis());
		String nullObject = null;
		nullObject.toLowerCase();
	}
}