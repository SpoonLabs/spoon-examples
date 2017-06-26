package factory.src.impl1;

import factory.src.A;
import factory.src.B;
import factory.src.Factory;

public class FactoryImpl1 implements Factory {

	public A createA() {
		return new AImpl1();
	}

	public B createB() {
		return new BImpl1();
	}

}
