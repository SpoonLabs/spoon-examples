package fr.inria.gforge.spoon.transformation.bound.src;

import fr.inria.gforge.spoon.transformation.bound.annotation.Bound;

public class Main {

	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			try {
				new Main().m(i);
			} catch (RuntimeException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public void m(@Bound(min = 2d, max = 8d) int a) {
		System.out.println("Great method!");
	}

}
