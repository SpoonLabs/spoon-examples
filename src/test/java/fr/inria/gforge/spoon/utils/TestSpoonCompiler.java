package fr.inria.gforge.spoon.utils;

import spoon.reflect.declaration.CtPackage;
import spoon.reflect.declaration.CtSimpleType;
import spoon.reflect.factory.Factory;
import spoon.reflect.visitor.DefaultJavaPrettyPrinter;
import spoon.support.compiler.jdt.JDTBasedSpoonCompiler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicolas on 02/09/2014.
 */
public class TestSpoonCompiler extends JDTBasedSpoonCompiler {

	public TestSpoonCompiler(Factory factory) {
		super(factory);
	}

	protected void generateProcessedSourceFilesUsingCUs() throws Exception {

		getFactory().getEnvironment().debugMessage(
				"Generating source using compilation units...");
		// Check output directory
		if (getOutputDirectory() == null) {
			throw new RuntimeException(
					"You should set output directory before generating source files");
		}
		// Create spooned dir
		if (getOutputDirectory().isFile()) {
			throw new RuntimeException("Output must be a directory");
		}
		if (!getOutputDirectory().exists()) {
			if (!getOutputDirectory().mkdirs()) {
				throw new RuntimeException("Error creating output directory");
			}
		}
		setOutputDirectory(getOutputDirectory().getCanonicalFile());

		getFactory().getEnvironment().debugMessage(
				"Generating source files to: " + getOutputDirectory());

		List<File> printedFiles = new ArrayList<File>();
		printing:
		for (spoon.reflect.cu.CompilationUnit cu : getFactory().CompilationUnit()
				.getMap().values()) {

			getFactory().getEnvironment().debugMessage(
					"Generating source for compilation unit: " + cu.getFile());

			CtSimpleType<?> element = cu.getMainType();

			CtPackage pack = element.getPackage();

			// create package directory
			File packageDir;
			if (pack.getQualifiedName()
					.equals(CtPackage.TOP_LEVEL_PACKAGE_NAME)) {
				packageDir = new File(getOutputDirectory().getAbsolutePath());
			} else {
				char dot = '.';
				// Create current package dir
				packageDir = new File(getOutputDirectory().getAbsolutePath()
						+ File.separatorChar
						+ pack.getQualifiedName().replace(dot, File.separatorChar));
			}
			if (!packageDir.exists()) {
				if (!packageDir.mkdirs()) {
					throw new RuntimeException(
							"Error creating output directory");
				}
			}

			File file = new File(packageDir.getAbsolutePath()
					+ File.separatorChar + element.getSimpleName()
					+ DefaultJavaPrettyPrinter.JAVA_FILE_EXTENSION);
			file.createNewFile();

			// the path must be given relatively to to the working directory
			InputStream is = getCompilationUnitInputStream(cu.getFile()
					.getPath());

			IOUtils.copy(is, new FileOutputStream(file));

			if (!printedFiles.contains(file)) {
				printedFiles.add(file);
			}

		}
	}
}
