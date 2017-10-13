package fr.inria.gforge.spoon.transformation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mdkt.compiler.InMemoryJavaCompiler;

import spoon.Launcher;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.visitor.Filter;
import spoon.reflect.visitor.filter.NamedElementFilter;
import spoon.reflect.visitor.filter.TypeFilter;

// shows how to test a transformation
// with assertions on the transformed AST
// and assertion on the resulted behavior
public class OnTheFlyTransfoTest {
	
  @Test
  public void example() throws Exception {
	  Launcher l = new Launcher();
	  
	  // required for having IFoo.class in the classpath in Maven
	  l.setArgs(new String[] {"--source-classpath","target/test-classes"});
	  
	  l.addInputResource("src/test/resources/transformation/");
	  l.buildModel();
	  
	  CtClass foo = l.getFactory().Package().getRootPackage().getElements(new NamedElementFilter<>(CtClass.class, "Foo1")).get(0);

	  // compiling and testing the initial class
	  Class<?> fooClass = InMemoryJavaCompiler.newInstance().compile(foo.getQualifiedName(), "package "+foo.getPackage().getQualifiedName()+";"+foo.toString());
	  IFoo x = (IFoo) fooClass.newInstance();
	  // testing its behavior
	  assertEquals(5, x.m());

	  // now we apply a transformation
	  // we replace "+" by "-"
	  for(Object e : foo.getElements(new TypeFilter(CtBinaryOperator.class))) {
		  CtBinaryOperator op = (CtBinaryOperator)e;
		  if (op.getKind()==BinaryOperatorKind.PLUS) {
			  op.setKind(BinaryOperatorKind.MINUS);
		  }
	  }
	  
	  // first assertion on the results of the transfo
	  
	  // there are no more additions in the code
	  assertEquals(0, foo.getElements(new Filter<CtBinaryOperator<?>>() {
		@Override
		public boolean matches(CtBinaryOperator<?> arg0) {
			return arg0.getKind()==BinaryOperatorKind.PLUS;
		}		  
	  }).size());

	  // second assertions on the behavior of the transformed code
	  
	  // compiling and testing the transformed class
	  fooClass = InMemoryJavaCompiler.newInstance().compile(foo.getQualifiedName(), "package "+foo.getPackage().getQualifiedName()+";"+foo.toString());
	  IFoo y = (IFoo) fooClass.newInstance();
	  // testing its behavior with subtraction
	  assertEquals(1, y.m());
	  
	  System.out.println("yes y.m()="+y.m());
  }
}
