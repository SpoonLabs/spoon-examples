package fr.inria.gforge.spoon.analysis;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;

import java.util.ArrayList;
import java.util.List;

public class FactoryProcessor extends AbstractProcessor<CtConstructorCall<?>> {

	public List<CtConstructorCall> listWrongUses = new ArrayList<>();
	private CtTypeReference factoryTypeRef;

	public FactoryProcessor(CtTypeReference factoryTypeRef) {
		this.factoryTypeRef = factoryTypeRef;
	}

	public void process(CtConstructorCall<?> newClass) {
		// skip factory creation
		if (newClass.getExecutable().getDeclaringType().isSubtypeOf(getFactoryType()))
			return;
		// skip creations in factories
		if (newClass.getParent(CtClass.class).isSubtypeOf(getFactoryType()))
			return;
		// only report for types created by the factory
		for (CtTypeReference<?> t : getCreatedTypes()) {
			if (newClass.getType().isSubtypeOf(t)) {
				this.listWrongUses.add(newClass);
			}
		}

	}

	protected CtTypeReference<?> getFactoryType() {
		return this.factoryTypeRef;
	}

	List<CtTypeReference<?>> createdTypes;

	private List<CtTypeReference<?>> getCreatedTypes() {
		if (createdTypes == null) {
			createdTypes = new ArrayList<CtTypeReference<?>>();
			for (CtExecutableReference<?> m : getFactoryType().getDeclaredExecutables()) {
				createdTypes.add(m.getType());
			}
		}
		return createdTypes;
	}

}
