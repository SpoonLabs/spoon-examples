package fr.inria.gforge.spoon.assertgenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Benjamin DANGLOT
 * benjamin.danglot@inria.fr
 * on 21/06/17
 */
public class Logger {

	public static Map<String, Object> observations = new HashMap<>();

	public static void observe(String name, Object object) {
		observations.put(name, object);
	}
}
