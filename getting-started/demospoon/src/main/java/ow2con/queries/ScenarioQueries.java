package ow2con.queries;

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

public class ScenarioQueries {
    @SuppressWarnings("all")
    public static void main(String[] args) {
        MavenLauncher launcher = new MavenLauncher(
                "path/to/my/project",
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
