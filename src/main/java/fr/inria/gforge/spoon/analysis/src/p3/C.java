package fr.inria.gforge.spoon.analysis.src.p3;

import fr.inria.gforge.spoon.analysis.src.Main;
import fr.inria.gforge.spoon.analysis.src.p1.A;

public class C {
	void m() {
		new A();
		try {
			Main.m2();
		} catch (Exception ignored) {
		}
	}
}
