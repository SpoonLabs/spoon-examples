package fr.inria.gforge.spoon.transformation;

import org.junit.Test;
import spoon.MavenLauncher;
import spoon.compiler.Environment;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtComment;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtThrow;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.Filter;
import spoon.reflect.visitor.filter.NamedElementFilter;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.ArrayList;
import java.util.List;

// more complex transformation which adds FIXME's in code
public class BigTransfoScenarioTest {
    @SuppressWarnings("all")
    @Test
    public void main() {
        MavenLauncher launcher = new MavenLauncher(
                "./src/test/resources/project/",
                MavenLauncher.SOURCE_TYPE.APP_SOURCE);

        CtModel model = launcher.buildModel();
        List<CtMethod> methodList = model.
                filterChildren(new NamedElementFilter<CtPackage>(CtPackage.class, "ow2con")).
                filterChildren(new NamedElementFilter<CtPackage>(CtPackage.class, "publicapi")).
                filterChildren(new TypeFilter<CtMethod>(CtMethod.class)).
                filterChildren(new Filter<CtMethod>() {
                    @Override
                    public boolean matches(CtMethod element) {
                        boolean isPublic = element.isPublic();
                        CtTypeReference returnType = element.getType();
                        String privateApiPackage = "ow2con.privateapi";
                        boolean isTypeFromPrivateApi = returnType.getQualifiedName().contains(privateApiPackage);
                        return isPublic && isTypeFromPrivateApi;
                    }
                }).list();

        Factory factory = launcher.getFactory();
        CtClass<? extends Throwable> exceptionClass = factory.createClass("ow2con.PrivateAPIException");
        CtConstructorCall<? extends Throwable> exceptionInstance = factory.createConstructorCall(exceptionClass.getReference());

        for (CtMethod method : methodList) {
            CtBlock methodBody = method.getBody();
            List<CtComment> bodyComments = new ArrayList<>();

            ArrayList<CtStatement> ctStatements = new ArrayList<>(methodBody.getStatements());

            for (CtStatement ctStatement : ctStatements) {
                String statement = ctStatement.toString();
                CtComment statementAsComment = factory.createInlineComment(statement);
                bodyComments.add(statementAsComment);
                methodBody.removeStatement(ctStatement);
            }

            CtThrow throwMyException = factory.createThrow();
            CtConstructorCall<? extends Throwable> constructorCall = exceptionInstance.clone();
            throwMyException.setThrownExpression(constructorCall);
            methodBody.addStatement(throwMyException);

            bodyComments.add(
                    factory.createInlineComment(
                    "FIXME: The private API type should never be return in a public API."
                    )
            );

            for (CtComment bodyComment : bodyComments) {
                throwMyException.addComment(bodyComment);
            }
        }

        Environment environment = launcher.getEnvironment();
        environment.setCommentEnabled(true);
        environment.setAutoImports(true);
        // the transformation must produce compilable code
        environment.setShouldCompile(true);
        launcher.prettyprint();

        // look in folder spooned/ow2con/publicapi/ the transformed code
    }
}
