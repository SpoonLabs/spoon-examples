package fr.inria.gforge.spoon.analysis;

import org.junit.Test;
import spoon.MavenLauncher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.Filter;
import spoon.reflect.visitor.filter.NamedElementFilter;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.List;

public class QueryExampleTest {
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
    }
}
