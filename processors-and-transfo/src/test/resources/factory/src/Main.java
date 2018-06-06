package factory.src;

import factory.src.impl1.AImpl1;
import factory.src.impl1.BImpl1;
import factory.src.impl1.FactoryImpl1;
import factory.src.impl2.FactoryImpl2;

public class Main {

	public static void main(String[] args) {
		execute(new FactoryImpl1());
		execute(new FactoryImpl2());
	}

	public static void execute(Factory f) {
		System.out.println("======= running program with factory "+f);
		A a = f.createA();
		a.m1();
		B b = f.createB();
		b.m2();
		// that's a mistake: the factory should be used!
		new BImpl1().m2();
		new AImpl1().m1();
		// non-factory instantiation
		new Object();
	}
	
}
