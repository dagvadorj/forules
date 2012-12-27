package org.ubdynamics.rulesapp.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;

public class DroolsUtil {

	public static List<String> runRules(Object s, ServletContext servletContext) {

		try {

			// load up the knowledge base
			KnowledgeBase kbase = readKnowledgeBase(servletContext);

			StatefulKnowledgeSession ksession = kbase
					.newStatefulKnowledgeSession();

			KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory
					.newFileLogger(ksession, "test");

			List<String> errors = new ArrayList<String>();

			ksession.insert(s);
			ksession.insert(errors);

			ksession.fireAllRules();

			logger.close();

			return errors;

		} catch (Throwable t) {
			System.out.println("Error in send()");
			t.printStackTrace();
			return null;
		}

	}

	private static KnowledgeBase readKnowledgeBase(ServletContext servletContext)
			throws FileNotFoundException {

		File file = new File(servletContext.getRealPath("changeset.xml"));

		// file.
		//
		InputStream inputStream = new FileInputStream(
				servletContext.getRealPath("changeset.xml"));

		// load package
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
				.newKnowledgeBuilder();

		kbuilder.add(ResourceFactory.newInputStreamResource(inputStream),
				ResourceType.CHANGE_SET);
		// kbuilder.add(
		// ResourceFactory
		// .newUrlResource("http://dagvadorj:abc@localhost:8082/guvnor-distribution-wars-5.4.0-20120516-jboss-as-7.0/org.drools.guvnor.Guvnor/package/defaultPackage/LATEST"),
		// ResourceType.PKG);

		// create the knowledge base
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();

		// add the package to the kbase
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

		return kbase;

	}

	// private static KnowledgeBase readKnowledgeBase() throws Exception {
	//
	// KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
	// .newKnowledgeBuilder();
	// kbuilder.add(ResourceFactory.newClassPathResource("Test.drl"),
	// ResourceType.DRL);
	//
	// KnowledgeBuilderErrors errors = kbuilder.getErrors();
	//
	// System.out.println(errors);
	//
	// if (errors.size() > 0) {
	// for (KnowledgeBuilderError error : errors) {
	// System.out.println(error);
	// }
	// throw new IllegalArgumentException("Could not parse knowledge.");
	// }
	//
	// KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
	// kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
	//
	// return kbase;
	// }
}
