package fr.inria.gforge.spoon.transformation.bound.template;

import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtTypeReference;
import spoon.template.AbstractTemplate;
import spoon.template.Local;
import spoon.template.Parameter;
import spoon.template.Substitution;

public class BoundTemplate extends AbstractTemplate<CtStatement> {

	@Parameter
	public CtTypeReference<Double> typeReference;

	@Parameter
	public String _parameter_;

	@Parameter
	public double _minBound_;

	@Parameter
	public double _maxBound_;

	@Local
	public BoundTemplate(CtTypeReference<Double> typeReference, String parameterName, double minBound, double maxBound) {
		this.typeReference = typeReference;
		_parameter_ = parameterName;
		_maxBound_ = maxBound;
		_minBound_ = minBound;
	}

	public void test(Double _parameter_) throws Throwable {
		if (_parameter_ > _maxBound_) {
			throw new RuntimeException("out of max bound (" + _parameter_ + ">" + _maxBound_);
		}
		if (_parameter_ < _minBound_) {
			throw new RuntimeException("out of min bound (" + _parameter_ + "<" + _minBound_);
		}
	}

	@Override
	public CtStatement apply(CtType<?> targetType) {
		return Substitution.substituteMethodBody((CtClass) targetType, this, "test", typeReference);
	}
}
