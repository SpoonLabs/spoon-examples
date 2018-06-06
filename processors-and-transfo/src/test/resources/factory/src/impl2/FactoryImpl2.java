package factory.src.impl2;

import factory.src.A;
import factory.src.B;
import factory.src.Factory;

public class FactoryImpl2 implements Factory {

	public A createA() {
		return new AImpl2();
	}

	public B createB() {
		return new BImpl2();
	}

}
