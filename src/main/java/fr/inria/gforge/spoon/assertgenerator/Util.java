package fr.inria.gforge.spoon.assertgenerator;

import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtVariableAccess;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtExecutableReference;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Benjamin DANGLOT
 * benjamin.danglot@inria.fr
 * on 26/06/17
 */
public class Util {


	public static String getKey(CtMethod method) {
		return method.getParent(CtClass.class).getSimpleName() + "#" + method.getSimpleName();
	}

	public static CtInvocation invok(CtMethod method, CtLocalVariable localVariable) {
		final CtExecutableReference reference = method.getReference();
		final CtVariableAccess variableRead = method.getFactory().createVariableRead(localVariable.getReference(), false);
		return method.getFactory().createInvocation(variableRead, reference);
	}

	public static List<CtMethod> getGetters(CtLocalVariable localVariable) {
		return ((Set<CtMethod<?>>) localVariable.getType().getDeclaration().getMethods()).stream()
				.filter(method -> method.getParameters().isEmpty() &&
						method.getType() != localVariable.getFactory().Type().VOID_PRIMITIVE &&
						(method.getSimpleName().startsWith("get") ||
								method.getSimpleName().startsWith("is"))
				).collect(Collectors.toList());
	}

}
