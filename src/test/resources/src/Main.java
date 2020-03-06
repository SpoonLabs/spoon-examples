package src;

import src.p3.C;

import java.util.Vector;

public class Main {
	public static void main(String[] args) {
		try {
			@SuppressWarnings("unused")
			Vector<?> v = null;
			m1();
		} catch (Exception ignored) {
		}
	}

	public static void m1() throws Exception {
		m2();
	}

	public static void m2() throws Exception {
		throw new RuntimeException();
	}

	public void m(C c) throws Exception {
		c.toString();
	}

	public void m5(Object c) throws Exception {
		c.toString();
	}

	public void m6(Object d) throws Exception {
		d.toString();
	}
}
