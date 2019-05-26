package fr.inria.gforge.spoon.transformation.apichecking;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtComment;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtThrow;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

import java.util.ArrayList;
import java.util.List;


public class APICheckingProcessor extends AbstractProcessor<CtMethod> {
    private static final String PACKAGE_PUBLIC_API = "ow2con.publicapi";
    private static final String PACKAGE_PRIVATE_API = "ow2con.privateapi";
    private static final String EXCEPTION_FQN = "ow2con.PrivateAPIException";

    @Override
    public boolean isToBeProcessed(CtMethod method) {
        // get the CtClass the method is attached to
        CtType parentClass = method.getParent(CtType.class);

        // check that the class belongs to the public API
        if (parentClass.getQualifiedName().contains(PACKAGE_PUBLIC_API)) {
            // check that the method is public
            if (method.isPublic()) {
                // check that the return type belongs to the private API
                CtTypeReference returnType = method.getType();
                return returnType.getQualifiedName().contains(PACKAGE_PRIVATE_API);
            }
        }
        return false;
    }

    @Override
    public void process(CtMethod method) {
        final Factory factory = method.getFactory();

        CtBlock methodBody = method.getBody();

        List<CtComment> bodyComments = new ArrayList<>();

        ArrayList<CtStatement> ctStatements = new ArrayList<>(methodBody.getStatements());
        for (CtStatement ctStatement : ctStatements) {
            String statement = ctStatement.toString();
            bodyComments.add(factory.createInlineComment(statement));
            methodBody.removeStatement(ctStatement);
        }

        CtClass<? extends Throwable> myExceptionClass = factory.Class().get(EXCEPTION_FQN);
        CtConstructorCall<? extends Throwable> myNewException = factory.createConstructorCall(myExceptionClass.getReference());

        CtThrow throwMyException = factory.createThrow();
        throwMyException.setThrownExpression(myNewException);
        methodBody.addStatement(throwMyException);

        bodyComments.add(factory.createInlineComment("FIXME: The private API type should never be return in a public API."));

        for (CtComment bodyComment : bodyComments) {
            throwMyException.addComment(bodyComment);
        }
    }
}
